# Gestão de pedidos Backend
Projeto responsável pela publicação das APIs e regras de negócio.


## Decisões técnicas

- Criação de fábrica para relatório supondo que no futuro novos formatos sejam pedidos. Utilizando factory abstrai o modo como cada tipo de relatório funciona;

- Utilização de interfaces para as services. Injetar interfaces e não classes desacopla as camadas permitindo múltiplas implementações e facilita em casos de dependencia ciclica entre serviços e testes;

- Responsabilidades separada. Controllers não tem acesso ao negócio e lida somente com chamadas e respostas, bem como os serviços não precisam saber a forma que os controllers lidam com as requisições;

- Separar as exceções por responsabilidade permite uma maior clareza e personalização do tratamento de cada uma a partir do GlobalExceptionHandler;

- Repository para separar as camadas do acesso ao banco de dados;

- Os repositorys só tem visibilidade dentro de seus pacotes separados por histórias de usuário (HU) package by feature. Isso força a service que quiser utilizar os dados de outro HU a fazer a chamada via serviço e não do repositório fora de seu domínio;

- Domain Driven Design DDD. Os nomes utilizadas estão em igualdade com os nomes que o cliente que escreveu a HU, mantendo coeso e facilitando a comunicação desde o desenvovedor até o PO;

- Criado dois profiles **dev** (desenvolvimento) e **prd** (produção), onde:
    - **dev**: sobe uma instância do banco H2 em memória para facilitar os testes;
    - **prd**: altera o nível de log para não pesar a máquina e somente logar _error_ e usa o BD PostgreSQL utilizando variáveis de ambiente configuradas no **Render** mantendo o segredo dos dados de conexão.

- Utilização do spring cloud. A aplicação **gestao-pedidos-back** se conecta com a aplicação **cloud-config** que carrega de outro repositório git as configurações de acordo com o profile **dev** ou **prd**;

- Utilização de flyway para versionamento e cuidar da evolução natural do banco de dados, neste caso seja no banco em memória H2 ou no PostgreSQL no Render;

- Utilizado RabbitMQ publicação de erros inesperados. A idéia é que pudesse haver um consumo por outro sistema que pluga nos erros postados de todos os sistemas de uma empresa e lida automaticamente segundo suas regras. Poderia cria uma monitoração, enviar e avisar a equipe responsável por sistema, etc.
    - Para este desafio eu publico o erro e eu mesmo o consumo e somente no profile **dev**, já que não tenho onde hospedar um Rabbit para exemplo de produção. O objetivo era cumprir o desafio bônus.

- Interceptor genérica para todos os controllers do sistema afim de logar a entrada e saída de cada requisicao. Isso ajuda no mapeamento de execuções e erros nos ambientes. Posteriormente eu gravaria estes logs no mongodb (pelo menos as requisições que tiveram erro).


## Como rodar apenas o projeto backend sem docker

- Se quiser subir o projeto backend de forma manual sem ser pelo docker siga os passos abaixo (se quiser rodar o projeto pelo docker leia o **README.md** da raiz da pasta **/gestao-de-pedidos** ):

- Supondo que tenha Java17+, Maven e Git instalados

- Abrir um terminal e executar os comandos:
```
git init .
```
```
git clone -b main https://gitlab.com/gestao-pedidos/gestao-de-pedidos.git
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
- Acessar no browser o [Swagger local](http://localhost:8080/gestao-pedidos/api/swagger-ui/index.html)

- Acessar no browser o [Swagger Render](https://gestao-pedidos-back.onrender.com/gestao-pedidos/api/swagger-ui/index.html)
    - **_OBSERVAÇÃO_**: A configuração realizada no _Render_ utiliza planos gratuítos e é possível que ao acessar o endereço acima não esteja disponível na hora, contudo o próprio _Render_ identifica a tentativa de acesso e sobe os serviços automaticamente uns minutos depois.

- **_OBSERVAÇÃO_**: Subindo de forma manual, o projeto _config-server_ é pré-requisito para o subir o backend e para usar o RabbitMQ subindo desta forma é preciso tê-lo instalado e configurado na máquina ou um docker mínimo com RabbitMQ.


## Como rodar os testes

- Só de rodar o comando **mvn clean install** os testes já serão executados automaticamente, mas alternativamente é possível rodar:
```
mvn test
```