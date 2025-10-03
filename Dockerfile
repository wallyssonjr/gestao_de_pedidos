# -------------------
# Build Config Server # rebuild
# -------------------
FROM maven:3.8.5-openjdk-17 AS config-build
WORKDIR /app
COPY config-server/pom.xml .
RUN mvn dependency:go-offline
COPY config-server/src ./src
RUN mvn clean package -DskipTests

# -------------------
# Build Backend
# -------------------
FROM maven:3.8.5-openjdk-17 AS backend-build
WORKDIR /app
COPY gestao-pedidos-back/pom.xml .
RUN mvn dependency:go-offline
COPY gestao-pedidos-back/src ./src
RUN mvn clean package -DskipTests

# -------------------
# Final image
# -------------------
FROM eclipse-temurin:17-jre-focal
RUN apt-get update && apt-get install -y supervisor curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia os JARs já buildados
COPY --from=config-build /app/target/*.jar config-server.jar
COPY --from=backend-build /app/target/*.jar backend.jar

# Copia supervisord.conf
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080 8888

CMD ["/usr/bin/supervisord", "-n"]
