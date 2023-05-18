FROM openjdk:17

COPY scrapper/target/scrapper-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT exec java -jar /app.jar