FROM openjdk:8
VOLUME /tmp
EXPOSE 8480
ARG JAR_FILE
COPY ${JAR_FILE} bill-app-final.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud","-jar","/bill-app-final.jar"]
