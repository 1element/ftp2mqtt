FROM openjdk:10-jdk-slim
VOLUME /config
WORKDIR /app
ARG JAR_FILE=build/libs/ftp2mqtt-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} ftp2mqtt.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/ftp2mqtt.jar","--spring.location=file:/config/application.properties"]
EXPOSE 2121
