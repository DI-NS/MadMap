# MedMap API

## Descrição
A **MedMap API** é uma aplicação Spring Boot que implementa autenticação e autorização via JWT (JSON Web Tokens). O objetivo é gerenciar UBSs (Unidades Básicas de Saúde) com endpoints para cadastro, autenticação e geração de tokens de acesso. A API segue princípios **SOLID** e **Clean Code** para facilitar manutenção e extensibilidade.

---

## Estrutura do Projeto

```plaintext
MedMap/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── MedMap/
│   │   │   │   ├── config/
│   │   │   │   │   ├── OpenApiConfig.java       # Configuração do Swagger/OpenAPI
│   │   │   │   │   └── SecurityConfig.java      # Configuração de segurança com Spring Security
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java      # Endpoints de autenticação (registro/login)
│   │   │   │   ├── model/
│   │   │   │   │   └── User.java                # Representação da entidade "User"
│   │   │   │   ├── repository/
│   │   │   │   │   ├── GenerateSecretKey.java   # Classe para gerar a chave JWT (HS512)
│   │   │   │   │   └── UserRepository.java      # Interface para interação com o banco
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthService.java         # Lógica de autenticação/geração de tokens
│   │   │   │   │   ├── JwtAuthenticationFilter.java # Filtro JWT para autenticar requisições
│   │   │   │   │   └── GlobalExceptionHandler.java # Tratamento global de exceções
│   │   │   │   └── MedMapApplication.java       # Classe principal do projeto
│   │   ├── resources/
│   │   │   ├── application.properties           # Configurações gerais do projeto
│   │   │   └── application.yml                  # Configurações específicas do JWT
├── test/                                        # Testes (diretório reservado)
├── target/                                      # Diretório gerado pela build
├── .gitignore                                   # Arquivo Git ignore
├── pom.xml                                      # Configuração Maven
└── README.md                                    # Documentação
```

---

## Configurações

### `application.yml`

Configuração de segurança e autenticação JWT.

```yaml
server:
  port: 8080

jwt:
  secret: "7qF0Yq6IBvKOcsmQI7PYZfCXlL50zi2vV9514w9CkBW5dWZx2oxDZqM2m98SiDH1h5ZfUvjyDtg2r7c12POPSg" # Substitua por sua chave secreta gerada
  expiration: 3600 # Expiração do token em segundos
```

---

## Configurações e Explicações por Arquivo

### Configurações

- **`OpenApiConfig.java`**
  - Configura a documentação Swagger para a API.
  - Acessível em: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- **`SecurityConfig.java`**
  - Configuração do Spring Security para gerenciar autenticação e autorização.
  - Filtro JWT adicionado para validar tokens em todas as requisições.

### Controladores

- **`AuthController.java`**
  - Expõe endpoints para registro e login:
    - `POST /auth/register`: Registra um novo usuário.
    - `POST /auth/login`: Gera um token JWT para o usuário.

### Modelos

- **`User.java`**
  - Representa a entidade do banco de dados `users`.
  - **Campos:**
    - `id`: Identificador único.
    - `nomeUbs`: Nome da unidade básica de saúde.
    - `cnes`: Código CNES da unidade.
    - `address`: Endereço (armazenado como hash).

### Repositórios

- **`UserRepository.java`**
  - Interface para acessar e manipular dados de usuários.
  - Método principal: `findByNomeUbsAndCnes`.

- **`GenerateSecretKey.java`**
  - Utilitário para gerar uma chave secreta JWT no algoritmo HS512.

### Serviços

- **`AuthService.java`**
  - Contém toda a lógica de autenticação:
    - Registro de usuários (com validação).
    - Login (valida dados e gera token JWT).

- **`JwtAuthenticationFilter.java`**
  - Filtro que intercepta todas as requisições e valida o token JWT no header `Authorization`.

- **`GlobalExceptionHandler.java`**
  - Lida com exceções de forma centralizada.
  - Retorna mensagens de erro padronizadas para o cliente.

---

## Rotas e Endpoints

### Endpoints de Autenticação

| Método | Endpoint         | Descrição                         |
|---------|------------------|-----------------------------------|
| POST    | `/auth/register` | Registra um novo usuário.         |
| POST    | `/auth/login`    | Gera token JWT para autenticação. |

### Swagger UI

Documentação interativa:

- URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Requisitos do Sistema

- **Java**: 17 ou superior.
- **Maven**: Para gerenciamento de dependências.
- **PostgreSQL**: Banco de dados relacional usado no projeto.

---

## Instruções para Execução

### Clonando o Repositório

```bash
git clone https://github.com/DI-NS/MedMap.git
cd MedMap
```

### Configurando o Banco de Dados

1. **Crie um banco no PostgreSQL:**

   ```sql
   CREATE DATABASE medmap;
   ```

2. **Atualize as credenciais do banco no arquivo `application.properties` ou `application.yml`:**

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/medmap
       username: <SEU_USUARIO>
       password: <SUA_SENHA>
   ```

3. **Execute o script de migração ou deixe o Hibernate criar automaticamente as tabelas.**

### Executando o Projeto

Compile e rode o projeto:

```bash
mvn spring-boot:run
```

Acesse a aplicação em: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## Testando a API

### Registro de Usuário

**Exemplo de Requisição:**

```json
{
  "nomeUbs": "UBS Teste",
  "cnes": "123456",
  "address": "Rua Exemplo, 123"
}
```

**Resposta Esperada:**

```json
{
  "message": "UBS registrada com sucesso!"
}
```

### Login

**Exemplo de Requisição:**

```json
{
  "nomeUbs": "UBS Teste",
  "cnes": "123456"
}
```

**Resposta Esperada:**

```json
{
  "token": "<JWT_TOKEN>"
}