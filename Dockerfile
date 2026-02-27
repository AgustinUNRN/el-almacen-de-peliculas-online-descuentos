# --- Etapa 1: Build (Maven + JDK 21) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# 1. Caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Build del proyecto
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Runtime (JRE 21) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Instalamos curl para los healthchecks del orquestador
RUN apk add --no-cache curl

# Seguridad: Usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Configuraciones de JVM para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# El puerto 8085 que definiste en el docker-compose
EXPOSE 8085

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]