# Gestão de pedidos
Este projeto compõe de três módulos: **_gestao-de-pedidos-back_**, **_gestao-de-pedidos-front_** e **_config-server_** onde: 
- **_gestao-de-pedidos-back_**: responsável pela publicação das APIs e regras de negócio
- **_gestao-de-pedidos-front_**: responsável pelas telas e usabilidade do sistema
- **_config-server_**: responsável por ler as configurações _.properties_ de acordo com o profile que o projeto estiver subindo via spring cloud


## Decisões técnicas

- Todos os requisitos obrigatório e bônus deste teste foram cumpridos.


- Na raiz de cada projeto (**_config-server_** , **_gestao-de-pedidos-back_** e **_gestao-de-pedidos-front_**) há um **README.md** que contém a sessão **Decisões técnicas** com as explicações para cada.
	- [README.md do backend](https://github.com/wallyssonjr/gestao_de_pedidos/tree/main/gestao-pedidos-back)
	- [README.md do frontend](https://github.com/wallyssonjr/gestao_de_pedidos/tree/main/gestao-pedidos-front)
	- [README.md do config-server](https://github.com/wallyssonjr/gestao_de_pedidos/blob/main/config-server/README.md)

## Como rodar o projeto localmente

- Supondo que tenha git e Docker Compose instalados:
- Abrir um terminal e executar os comandos:
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
- As configurações identificarão que está rodando localmente e subirá instâncias dos 3 projetos utilizando profile **dev** e após finalizar todo o processo pode acessar no browser:
- [Swagger](http://localhost:8080/gestao-pedidos/api/swagger-ui/index.html)
- [Página do sistema](http://localhost)


## Como rodar os testes manualmente

- Supondo que tenha Java17+ e Maven instalados
- Abrir um terminal e abrir o diretório **_gestao-de-pedidos\gestao-pedidos-back_**
```
cd gestao-de-pedidos\gestao-pedidos-back
```
e rodar o comando:
```
mvn test
```
