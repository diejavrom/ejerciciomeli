FROM openjdk:8
VOLUME /tmp
EXPOSE 8180
ARG JAR_FILE
COPY ${JAR_FILE} charge-app-final.jar
ENTRYPOINT ["java","-Dspring.profiles.active=cloud","-Djava.security.egd=file:/dev/./urandom","-jar","/charge-app-final.jar"]
