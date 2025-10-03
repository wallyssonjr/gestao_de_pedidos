# Config server
Responsible for reading .properties configurations according to the profile the project is running with via Spring Cloud.

## Technical Decisions

- Simple project that is a prerequisite for running the backend project.
- This project reads configurations from a separate Git repository. Separating repositories increases security and enforces separation of responsibilities.
- 
## How to Run the Config-Server Project Without Docker
- If you want to run the config-server manually without Docker, follow these steps (for running via Docker, see the README.md at the root of /gestao-de-pedidos):
- Assuming Java 17+, Maven, and Git are installed.
- Open a terminal and run the commands:
```
git init .
```
```
git clone -b main git@github.com:wallyssonjr/gestao_de_pedidos.git
```
```
cd gestao-de-pedidos\config-server
```
```
mvn clean install
```
```
java -jar target\config-server-0.0.1.jar
```
- Note: This project is a prerequisite for running the backend.