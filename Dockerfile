# This is a multi-stage docker build file.
# The first stage, BUILD, uses the Maven base image to compile and package this Java app as websocket-example.war
# The second stage takes the WAR file created in the first stage and copies it into Tomcat image.
# The intermediate BUILD image is then discarded.  You will run the final image built off of Tomcat.

FROM maven:3.5.2-jdk-8-alpine AS BUILD
WORKDIR /websocket-example
COPY . .
RUN mvn clean package

FROM tomcat:9.0.4-jre8-alpine
# Remove the default 'root' application and replace it with my application (must be called ROOT)
RUN rm -fr webapps/ROOT
COPY --from=BUILD /websocket-example/target/websocket-example.war webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
