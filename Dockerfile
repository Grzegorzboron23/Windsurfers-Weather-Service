# Etap 1: Budowanie aplikacji
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Kopiowanie plików pom.xml i źródłowych do obrazu
COPY pom.xml .
COPY src ./src

# Budowanie aplikacji
RUN mvn clean package -DskipTests

# Etap 2: Tworzenie obrazu uruchomieniowego
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Kopiowanie wygenerowanego pliku JAR z etapu budowania
COPY --from=build /app/target/Windsurfer-0.0.1-SNAPSHOT.jar app.jar

# Eksponowanie portu 8080
EXPOSE 8080

# Uruchomienie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
