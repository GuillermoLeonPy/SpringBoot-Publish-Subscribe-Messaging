FROM openjdk:18
WORKDIR /app

COPY ./target/Kafka-Subscriber-1.0-SNAPSHOT.jar /app
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/Kafka-Subscriber-1.0-SNAPSHOT.jar"]
