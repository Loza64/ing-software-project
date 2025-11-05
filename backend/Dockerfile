# Etapa de construcción
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
COPY run.sh /run.sh
RUN chmod +x /run.sh

# Para desarrollo: montar el código como volumen
VOLUME /app/target

EXPOSE 4000
ENTRYPOINT ["/run.sh"]
