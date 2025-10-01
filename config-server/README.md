# Config server
Responsável por ler as configurações _.properties_ de acordo com o profile que o projeto estiver subindo via spring cloud


## Decisões técnicas

- Projeto simples que é pré-requisito para subir o projeto backend
- Este projeto lê as configurações a partir de um repositório git separado. Separar os repositórios aumenta a segurança além da separação de responsabilidades
- Endereço git do repositórico com as configurações: [GitLab configurações](https://gitlab.com/gestao-pedidos/gestao-de-pedidos-config.git)


## Como rodar apenas o projeto config-server sem docker
- Se quiser subir o projeto config-server de forma manual sem ser pelo docker siga os passos abaixo (se quiser rodar o projeto pelo docker leia o **README.md** da raiz da pasta **/gestao-de-pedidos** ):
- Supondo que tenha Java17+, Maven e Git instalados
- Abrir um terminal e executar os comandos:
```
git init .
```
```
git clone -b main https://gitlab.com/gestao-pedidos/gestao-de-pedidos.git
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
- **_OBSERVAÇÃO_**: Este projeto é pré-requisito para o rodar o backend 