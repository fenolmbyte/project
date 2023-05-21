FROM openjdk:17

COPY bot/target/bot-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT exec java --enable-preview -jar /app.jar
