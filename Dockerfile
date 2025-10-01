FROM eclipse-temurin:17-jre-focal

# Instala supervisor
RUN apt-get update && apt-get install -y supervisor curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia JARs
COPY gestao-pedidos-back/target/*.jar backend.jar
COPY config-server/target/*.jar config-server.jar

# Copia configuração do supervisord
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

EXPOSE 8080 8888

CMD ["/usr/bin/supervisord", "-n"]
