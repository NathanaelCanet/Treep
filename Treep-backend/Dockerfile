# Dockerfile multi-stage pour un projet Spring Boot (Maven)
# Placez ce fichier à la racine du projet (au même niveau que pom.xml)

FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /workspace

# Copier uniquement le pom pour profiter du cache des dépendances
COPY pom.xml .
# Copier le code source et builder
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime minimal
FROM eclipse-temurin:21-jre
ARG JAR_FILE=target/*.jar
COPY --from=builder /workspace/${JAR_FILE} /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]