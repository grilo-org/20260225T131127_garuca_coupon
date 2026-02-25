# Coupon API

API RESTful para gerenciamento de cupons de desconto desenvolvida com Spring Boot e Clean Architecture.

## Visão Geral

Esta API permite criar, listar e deletar (soft delete) cupons de desconto com validações de regras de negócio robustas.

## Arquitetura

O projeto segue os princípios da **Clean Architecture** com as seguintes camadas:

- **Domain**: Entidades e regras de negócio puros
- **Application**: Casos de uso e lógica de aplicação
- **Infrastructure**: Persistência, controllers e configurações externas

## Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (banco de dados em memória)
- **OpenAPI/Swagger** (documentação da API)
- **Docker & Docker Compose**
- **JUnit 5** (testes unitários)
- **JaCoCo** (cobertura de testes)

## Regras de Negócio

- **Código**: Exatamente 6 caracteres alfanuméricos (especiais são removidos, convertido para maiúsculas)
- **Valor de Desconto**: Mínimo de 0.5
- **Data de Expiração**: Não pode ser no passado
- **Soft Delete**: Cupons não são removidos fisicamente, apenas marcados como deletados

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/coupons` | Lista todos os cupons |
| POST | `/api/v1/coupons` | Cria um novo cupom |
| DELETE | `/api/v1/coupons/{id}` | Deleta um cupom (soft delete) |

## Documentação

Acesse a documentação interativa (Swagger UI) em:
- Local: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Executando o Projeto

### Localmente (Maven)

```bash
./mvnw spring-boot:run
```

### Com Docker

```bash
docker-compose up --build
```

### Testes

```bash
./mvnw test
```

### Cobertura de Testes

```bash
./mvnw clean verify
```

Relatório gerado em: `target/site/jacoco/index.html`

## Exemplos de Uso

### Criar Cupom

```bash
curl -X POST http://localhost:8080/api/v1/coupons \
  -H "Content-Type: application/json" \
  -d '{
    "code": "ABC123",
    "description": "Desconto de verão",
    "discountValue": 10.50,
    "expirationDate": "2025-12-31T23:59:59",
    "published": true
  }'
```

### Listar Cupons

```bash
curl http://localhost:8080/api/v1/coupons
```

### Deletar Cupom

```bash
curl -X DELETE http://localhost:8080/api/v1/coupons/{id}
```

## Estrutura do Projeto

```
src/
├── main/java/com/example/coupon/
│   ├── application/
│   │   ├── dto/
│   │   └── usecase/
│   ├── controller/
│   ├── domain/
│   │   ├── exception/
│   │   ├── model/
│   │   └── repository/
│   └── infrastructure/
│       ├── config/
│       └── persistence/
└── test/
    └── java/com/example/coupon/
```

## Requisitos

- Java 17+
- Maven 3.8+
- Docker (opcional)

## Licença

Este projeto é de código aberto e está disponível para uso educacional.
