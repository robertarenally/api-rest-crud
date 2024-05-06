# api-rest-crud

Essa é uma api rest de um CRUD de usuários, cada usuário tem os seguintes dados:

- Nome completo

- Data de nascimento

- Uma lista de Endereços e para cada endereço:
  - Logradouro
  - CEP
  - Número
  - Cidade
  - Estado
  - Flag que indica qual dos endereços é o principal

Essa api foi implementada utilizando

- Java 11
- Maven
- Spring Boot
- Spring Data
- Lombok
- Swagger
- MySql

## Para fazer o deploy no seu ambiente, basta configurar algumas coias

1. Alterar os dados de conexão com o banco, para o banco MySql do seu ambiente:
![conexao banco de dados](/images/conexao-mysql.png)

2. Exemplo de deploy da api no Eclipse IDE:
![deploy api no eclipse ide](/images/deploy-api-eclipse-ide.png)

3. Tela da API rodando com swagger no endereço(http://localhost:8080/swagger-ui.html)
![api running](/images/tela-api.png)

4. Execução dos testes pelo Junit no Eclipse
![execucao testes unitarios](/images/execucao-testes-junit.png)

5. Cobertura dos testes pelo Junit no Eclipse
![cobertura testes unitarios](/images/cobertura-testes-junit.png)
