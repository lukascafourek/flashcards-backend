# 1. Fáze: build pomocí Maven + JDK 21
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Pracovní adresář v kontejneru
WORKDIR /app

# Zkopírování celého projektu
COPY . .

# Build projektu a vytvoření .jar souboru
RUN mvn clean package -DskipTests

# 2. Fáze: běhové prostředí s JDK 21
FROM eclipse-temurin:21-jre

# Pracovní adresář
WORKDIR /app

# Přenos .jar souboru z první fáze
COPY --from=build /app/target/*.jar app.jar

# Otevření portu (nepovinné, informativní)
EXPOSE 8080

# Spuštění aplikace
CMD ["java", "-jar", "app.jar"]
