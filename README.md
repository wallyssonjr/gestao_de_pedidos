# Order Management

- This project consists of three modules: gestao-de-pedidos-back, gestao-de-pedidos-front, and config-server, where:
- gestao-de-pedidos-back: responsible for publishing APIs and implementing business rules
- gestao-de-pedidos-front: responsible for the system screens and usability
- config-server: responsible for reading .properties configurations according to the profile the project is running with via Spring Cloud

## Technical Decisions

- All mandatory and bonus requirements of this test have been fulfilled.


- At the root of each project (config-server, gestao-de-pedidos-back, and gestao-de-pedidos-front), there is a README.md containing the Technical Decisions section with explanations for each:
	- [README.md do backend](https://github.com/wallyssonjr/gestao_de_pedidos/tree/main/gestao-pedidos-back)
	- [README.md do frontend](https://github.com/wallyssonjr/gestao_de_pedidos/tree/main/gestao-pedidos-front)
	- [README.md do config-server](https://github.com/wallyssonjr/gestao_de_pedidos/blob/main/config-server/README.md)

- Jobs have been configured on GitLab so that every push goes through these three jobs in order to ensure development quality:
  - **test**: runs all unit and integration tests to ensure none are broken
  - **build**: if the previous step succeeds, builds the backend and frontend projects
  - **dockerize**: creates Docker images for each project so they can be used in a deployment job

- Render has been configured for automatic deployment. When running docker-compose up --build locally, the environment will automatically start config-server, RabbitMQ, back, and front with the dev profile. However, when performing a push, Render will automatically start the deployment job for config-server, back, and front with the prd profile.
  - The Swagger endpoint on Render is: https://gestao-pedidos-back.onrender.com/gestao-pedidos/api/swagger-ui/index.html
  - The system page endpoint on Render is: https://gestao-de-pedidos-1.onrender.com/dashboard
  - **_NOTE_**: The configuration on Render uses free plans, so it is possible that the above addresses may not be available immediately. However, Render itself detects access attempts and starts the services automatically a few minutes later.

## How to Run the Project Locally

- Assuming Git and Docker Compose are installed:
- Open a terminal and run the commands:
```
git init .
```
```
git clone -b main git@github.com:wallyssonjr/gestao_de_pedidos.git
```
```
cd gestao-de-pedidos
```
```
docker-compose up --build
```
- The configuration will detect that it is running locally and start instances of the three projects using the dev profile. Once the process finishes, you can access in your browser:
- [Swagger](http://localhost:8080/gestao-pedidos/api/swagger-ui/index.html)
- [Página do sistema](http://localhost)

## How to Run Tests Manually

- Assuming Java 17+ and Maven are installed
- Open a terminal and navigate to the gestao-de-pedidos\gestao-pedidos-back directory:
```
cd gestao-de-pedidos\gestao-pedidos-back
```
Run the command:
```
mvn test
```
