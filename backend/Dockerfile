# --- Étape 1 : Construction (Build) ---
# On utilise une image avec Maven et JDK 17 (ou votre version)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# On copie d'abord le pom.xml pour profiter du cache Docker (téléchargement des dépendances)
COPY pom.xml .
# On télécharge les dépendances (sans copier le code source pour l'instant)
RUN mvn dependency:go-offline

# On copie le code source et on compile le .jar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape 2 : Exécution (Run) ---
# On prend une image légère juste avec le JRE (Java Runtime)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# On récupère le .jar généré à l'étape 1
COPY --from=build /app/target/*.jar app.jar

# Configuration pour attendre que la DB soit prête (optionnel mais recommandé)
# (Simple entrypoint ici)
ENTRYPOINT ["java", "-jar", "app.jar"]