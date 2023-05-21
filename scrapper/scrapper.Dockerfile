FROM openjdk:17

ENV JAVA_HOME "C:\Program Files\Java\jdk-17.0.7+7"

COPY scrapper/target/scrapper-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT exec java --enable-preview -jar /app.jar
