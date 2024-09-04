FROM maven:3.8.8-eclipse-temurin-17 AS build  # Użyj dostępnego obrazu Maven z JDK 17
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app

ARG JAR_FILE=target/Windsurfer-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
