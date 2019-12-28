FROM openjdk:8
VOLUME /tmp
EXPOSE 8380
ARG JAR_FILE
COPY ${JAR_FILE} status-app-final.jar
ENTRYPOINT ["java","-Dspring.profiles.active=cloud","-Djava.security.egd=file:/dev/./urandom","-jar","/status-app-final.jar"]
