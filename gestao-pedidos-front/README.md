# Gestao de pedidos Front
Projeto responsável pelas telas e usabilidade do sistema.


## Decisões técnicas

- Utilização do Angular Material, apesar de ter sido o solicitado pelo desafio, é vantajoso por ser a biblioteca oficial de componentes do google bem como garante uma consistência visual e vários componentes já prontos

- Modularização dos componentes ajuda na separação lógica dos componentes da tela e também isola o código que é carregado sob demanda

- Modularizado também o componente de mensagens para ser utilizado no sistema. Ajuda a padronizar as mensagens do sistema com fácil mudança se necessário

- Modularizado o componente loading e criado um interceptor de requests específico para ele

- Criação do módulo *shared* ajuda a centralizar componentes que serão reutilizados nos módulos do sistema, além de evitar imports repetitivos

- Criação do módulo *core* é responsável por serviços e interceptadores que serão utilizados por todo o sistema e que são únicos. Além de centralizar esses comportamentos evita problemas de dependencia ciclica

- Gerenciamento de estado para o módulo de produto. Isso evita que cada componente tenha que buscar as mudanças nos produtos e centraliza a responsabilidade de modo que qualquer alteração que haja será transparente para o sistema, além da performance de chamadas desnecessárias à API

- Interceptadores http para os requests e responses. No caso do request simulei o envio automático de um token para qualquer chamada à API e o response o tratamento padronizado de erros retornados pelas APIs
  - O interceptor de request loading foi separado e não junto ao interceptor auth afim de criar responsabilidades únicas facilitando manutenção e reutilização

- Os formulários são reativos com os componentes do Angula Material que facilita a validação de componentes

- Utilizado scss com @media desta forma as telas são responsivas para desktop e mobile

## Como rodar apenas o projeto frontend sem docker

- Se quiser subir o projeto frontend de forma manual sem ser pelo docker siga os passos abaixo (se quiser rodar o projeto pelo docker leia o *README.md* da raiz da pasta */gestao-de-pedidos* ):

- Supondo que tenha Node.js e Git instalados

- Abrir um terminal e executar os comandos:
```
git init .
```
```
git clone -b main git@github.com:wallyssonjr/gestao_de_pedidos.git
```
```
cd gestao-de-pedidos\gestao-pedidos-front
```
caso não tenha instalado angular.v19.2.15+ na máquina faça o comando:
```
npm install -g @angular/cli@19.2.15
```
na raiz da pasta *gestao-de-pedidos\gestao-pedidos-front* rode o comando:
```
npm install
```
após finalizar rode o comando:
```
ng serve --port 80
```
- Acessar no browser a [Página do sistema](http://localhost)

- Observação: é necessário o backend estar rodando antes para o funcionamento correto do frontend
