FROM tomcat:9.0.4-jre8-alpine
COPY target/websocket-example.war webapps/websocket-example.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
