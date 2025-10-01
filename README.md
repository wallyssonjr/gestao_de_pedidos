# Gestão de pedidos
Este projeto compõe de três módulos: **_gestao-de-pedidos-back_**, **_gestao-de-pedidos-front_** e **_config-server_** onde: 
- **_gestao-de-pedidos-back_**: responsável pela publicação das APIs e regras de negócio
- **_gestao-de-pedidos-front_**: responsável pelas telas e usabilidade do sistema
- **_config-server_**: responsável por ler as configurações _.properties_ de acordo com o profile que o projeto estiver subindo via spring cloud


## Decisões técnicas

- Todos os requisitos obrigatório e bônus deste teste foram cumpridos.


- Na raiz de cada projeto (**_config-server_** , **_gestao-de-pedidos-back_** e **_gestao-de-pedidos-front_**) há um **README.md** que contém a sessão **Decisões técnicas** com as explicações para cada.
	- [README.md do backend](https://gitlab.com/gestao-pedidos/gestao-de-pedidos/-/blob/main/gestao-pedidos-back/README.md?ref_type=heads)
	- [README.md do frontend](https://gitlab.com/gestao-pedidos/gestao-de-pedidos/-/blob/main/gestao-pedidos-front/README.md?ref_type=heads)
	- [README.md do config-server](https://gitlab.com/gestao-pedidos/gestao-de-pedidos/-/blob/main/config-server/README.md?ref_type=heads)


- Foram configurados jobs no [GitLab](https://gitlab.com/gestao-pedidos/gestao-de-pedidos/-/pipelines) para que todo push realizado passe por esses 3 jobs afim de garantir a qualidade do desenvolvimento:
	- **test**: realiza todos os testes unitários e de integração para garantir que nenhum tenha sido quebrado
	- **build**: passado com sucesso da etapa anterior, realiza o build dos projetos backend e frontend
	- **dockerize**: cria as imagens docker de cada projeto para que possa ser levada a um job de deploy


- Foi configurado o **Render** para deploy automático. Ao rodar o *docker-compose up --build* localmente o ambiente vai subir o _config-server_ , _RabbitMQ_ , _back_ e _front_ com profile **_dev_** automaticamente, contudo ao realizar um *push* o **Render** iniciará automaticamente o job de implantação _config-server_ , _back_ e _front_ com profile **_prd_**
	- O apontamento para swagger do **Render** é: https://gestao-pedidos-back.onrender.com/gestao-pedidos/api/swagger-ui/index.html
	- O apontamento para a página do sistema pelo **Render** é: https://gestao-pedidos-front.onrender.com/
	- **_OBSERVAÇÃO_**: A configuração realizada no _Render_ utiliza planos gratuítos e é possível que ao acessar os endereços acima não estejam disponíveis na hora, contudo o próprio _Render_ identifica a tentativa de acesso e sobe os serviços automaticamente uns minutos depois.


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