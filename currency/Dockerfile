FROM openjdk:8
VOLUME /tmp
EXPOSE 8280
ARG JAR_FILE
COPY ${JAR_FILE} currency-app-final.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud","-jar","/currency-app-final.jar"]
