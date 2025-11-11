# Étape 1 : construire l'application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : exécuter l'application
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Port exposé par le service
EXPOSE 8086

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
