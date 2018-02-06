# This is a multi-stage docker build file.
# The first stage, BUILD, uses the Maven base image to compile and package this Java app as websocket-example.war
# The second stage takes the WAR file created in the first stage and copies it into Tomcat image.
# The intermediate BUILD image is then discarded.  You will run the final image built off of Tomcat.

###########################################

FROM maven:3.5.2-jdk-8-alpine AS BUILD
WORKDIR /websocket-example
COPY . .
RUN mvn clean package

###########################################

FROM tomcat:9.0.4-jre8-alpine
# Make keypair for TLS connections over 8443
RUN keytool -genkey \
 -alias websocket-example \
 -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" \
 -keyalg RSA -keysize 2048 \
 -keystore /root/keystore.pkcs12 \
 -storetype pkcs12 \
 -storepass changeit
 
# Update the server.xml with the TLS connection and reference to the keystore
COPY src/main/container/8443-connector.txt 8443-connector.txt
RUN sed -i '/<Service name="Catalina">/r 8443-connector.txt' conf/server.xml

# Remove the default 'root' application and replace it with my application (must be called ROOT)
RUN rm -fr webapps/ROOT
COPY --from=BUILD /websocket-example/target/websocket-example.war webapps/ROOT.war
EXPOSE 8080 8443
CMD ["catalina.sh", "run"]
