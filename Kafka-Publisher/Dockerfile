FROM openjdk:18
WORKDIR /app

COPY ./target/Kafka-Publisher-1.0-SNAPSHOT.jar /app
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/Kafka-Publisher-1.0-SNAPSHOT.jar"]
