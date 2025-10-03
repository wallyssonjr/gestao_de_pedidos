# Order Management Backend
Project responsible for publishing APIs and implementing business rules.


## Technical Decisions

- Report Factory: Created a factory for reports assuming new formats may be required in the future. Using a factory abstracts how each report type works.

- Service Interfaces: Interfaces are used for services. Injecting interfaces instead of classes decouples layers, allowing multiple implementations and facilitating circular dependency management and testing.
 
- Separation of Responsibilities: Controllers do not access business logic and handle only requests and responses. Services do not need to know how controllers process requests.

- Exception Handling by Responsibility: Separating exceptions by responsibility provides greater clarity and allows custom handling via the GlobalExceptionHandler.

- Repository Layer: Separates the database access layer.

- Package-by-Feature Repositories: Repositories are only visible within their respective feature packages (HU – user story). This forces services that need data from another HU to call it via the service rather than accessing the repository outside its domain.

- Domain-Driven Design (DDD): Naming aligns with the terminology used by the client who wrote the HU, maintaining cohesion and facilitating communication from developers to the PO.

- Profiles: Two profiles created: dev (development) and prd (production), where:
  - dev: runs an in-memory H2 database instance for easier testing.
  - prd: changes log levels to only log error, and uses PostgreSQL with environment variables configured on Render, keeping connection credentials secure.

- Spring Cloud: The gestao-pedidos-back application connects to the cloud-config application, which loads configurations from another Git repository according to the dev or prd profile.

- Flyway: Used for versioning and managing database evolution, whether using the in-memory H2 or PostgreSQL on Render.

- RabbitMQ: Used for publishing unexpected errors. The idea is that another system could consume errors from all company systems and handle them automatically according to its rules. For this challenge, errors are published and consumed locally, only in the dev profile, since there is no production RabbitMQ available. The goal was to meet the bonus challenge.

- Generic Interceptor: Logs entry and exit of every request for all controllers. This helps trace executions and errors across environments. In the future, these logs could be stored in MongoDB (at least for requests that had errors).

## How to Run the Backend Project Without Docker

- If you want to run the backend manually without Docker, follow these steps (for running via Docker, see the README.md at the root of /gestao-de-pedidos):
- Assuming Java 17+, Maven, and Git are installed.
- Open a terminal and run the commands:

```
git init .
```
```
git clone -b main git@github.com:wallyssonjr/gestao_de_pedidos.git
```
```
cd gestao-de-pedidos\gestao-pedidos-back
```
```
mvn clean install
```
```
java -jar target\gestao-pedidos-back-0.0.1.jar --spring.profiles.active=dev
```
- Access in your browser: [Swagger local](http://localhost:8080/gestao-pedidos/api/swagger-ui/index.html)
- Note: Running manually requires the config-server to be running first. To use RabbitMQ this way, it must be installed and configured locally or via a minimal Docker setup.

## How to Run Tests

- Running mvn clean install automatically executes the tests. Alternatively, you can run:
```
mvn test
```