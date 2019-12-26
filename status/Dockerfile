FROM openjdk:8
VOLUME /tmp
EXPOSE 8380
ARG JAR_FILE
COPY ${JAR_FILE} status-app-final.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud","-jar","/status-app-final.jar"]
