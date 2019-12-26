FROM openjdk:8
VOLUME /tmp
EXPOSE 8180
ARG JAR_FILE
COPY ${JAR_FILE} charge-app-final.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud","-jar","/charge-app-final.jar"]
