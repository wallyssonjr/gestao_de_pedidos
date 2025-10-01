# =========================
# Build do backend
# =========================
FROM maven:3.8.5-openjdk-17 AS backend-build
WORKDIR /app

# Copia o pom e baixa dependências
COPY gestao-pedidos-back/pom.xml .
RUN mvn dependency:go-offline

# Copia o código e builda
COPY gestao-pedidos-back/src ./src
RUN mvn clean package -DskipTests

# =========================
# Build do front
# =========================
FROM node:18 AS frontend-build
WORKDIR /app

# Copia package.json e instala dependências
COPY gestao-pedidos-front/package*.json ./
RUN npm ci

# Copia o front e builda
COPY gestao-pedidos-front/ .
RUN npm run build -- --configuration=production

# =========================
# Imagem final
# =========================
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copia o jar do backend
COPY --from=backend-build /app/target/*.jar app.jar

# Copia build do front para a pasta de recursos estáticos do Spring Boot
# (Spring Boot serve automaticamente arquivos dentro de src/main/resources/static)
COPY --from=frontend-build /app/dist/gestao-pedidos-front/browser /app/src/main/resources/static

# Variáveis de linguagem
ENV LANG=pt_BR.UTF-8
ENV LANGUAGE=pt_BR:pt:en
ENV LC_ALL=pt_BR.UTF-8

# Expor porta do backend
EXPOSE 8080

# Rodar o backend (que agora serve também o front)
ENTRYPOINT ["java", "-Duser.language=pt", "-Duser.region=BR", "-jar", "app.jar"]
