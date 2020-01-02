FROM openjdk:8
VOLUME /tmp
EXPOSE 8280
ARG JAR_FILE
COPY ${JAR_FILE} currency-app-final.jar
ENTRYPOINT ["java","-Dspring.profiles.active=cloud","-Djava.security.egd=file:/dev/./urandom,"-jar","/currency-app-final.jar"]
