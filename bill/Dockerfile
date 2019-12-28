FROM openjdk:8
VOLUME /tmp
EXPOSE 8480
ARG JAR_FILE
COPY ${JAR_FILE} bill-app-final.jar
ENTRYPOINT ["java","-Dspring.profiles.active=cloud","-Djava.security.egd=file:/dev/./urandom","-jar","/bill-app-final.jar"]
