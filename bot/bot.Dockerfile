FROM openjdk:17

ENV PATH "C:\Program Files\Java\jdk-17.0.7+7\bin;%PATH%"

ENV JAVA_HOME "C:\Program Files\Java\jdk-17.0.7+7"

ENV JAVA_VERSION "17.0.7"

COPY bot/target/bot-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT exec java --enable-preview -jar /app.jar
