# ==========================
# Etapa 1: Build de Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

ARG SERVICE_NAME=service-emisor

# Copiamos todo el monorepo y compilamos solo este servicio (+ sus dependencias)
COPY . .
RUN mvn clean package -pl ${SERVICE_NAME} -am -DskipTests

# ==========================
# Etapa 2: Imagen final (solo el JAR)
# ==========================
FROM eclipse-temurin:21-jre
WORKDIR /app

ARG SERVICE_NAME=service-emisor
COPY --from=builder /app/${SERVICE_NAME}/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
