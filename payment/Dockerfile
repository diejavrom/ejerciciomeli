FROM openjdk:8
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE
COPY ${JAR_FILE} payment-app-final.jar
ENTRYPOINT ["java","-Dspring.profiles.active=cloud","-Djava.security.egd=file:/dev/./urandom","-jar","/payment-app-final.jar"]
