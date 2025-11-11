# Étape 1 : build du JAR
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# ⚙️ Construire le projet Spring Boot
RUN mvn clean package -DskipTests

# Étape 2 : image finale pour exécution
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar donation.jar

EXPOSE 1920
ENTRYPOINT ["java", "-jar", "donation.jar"]