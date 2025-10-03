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
