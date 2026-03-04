# Recipe Application API
## About

This project is a REST API built with Spring Boot for managing recipes and their ingredients.

The API supports:

- Creating recipes

- Retrieving all recipes

- Searching recipes by name

- Retrieving a recipe by ID

- Deleting recipes

Each recipe contains a name, description, cooking time, meal type, and a list of ingredients.

## Tech Stack

- Java 17

- Spring Boot 4.0.3

- Spring Data JPA

- PostgreSQL

- Testcontainers

- RestAssured

- OpenAPI 3 (Springdoc)

## Running the Application
### Prerequisites

- Java 17

- Maven

- Docker (required for database)

### Build
`mvn clean install` or `./mvnw clean install`

### Run
`mvn spring-boot:run` or `./mvnw spring-boot:run`

Application runs on:

http://localhost:8080

## API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html


OpenAPI JSON:

http://localhost:8080/v3/api-docs

## Example Endpoints
### Create Recipe

`POST /recipe`

`{
"name": "Pizza",
"description": "Classic Italian pizza",
"time": 45,
"mealType": "DINNER",
"ingredients": [
{ "name": "Dough", "amount": "1kg" },
{ "name": "Tomato", "amount": "1L" }
]
}`

### Get All Recipes

`GET /recipes`

Optional:

`/recipes?search=Pizza`

Get Recipe by ID

`GET /recipe/{id}`

Delete Recipe

`DELETE /recipe/{id}`

## Testing

This project includes two styles of integration testing:

### 1. Individual API response testing

Boot full application context, using Testcontainers with PostgreSQL, verifying individual API responses per test and validating with internal JPA repository

Class: `RecipeControllerTest.java`

### 2. API Flow Integration Tests

Execute multiple API calls per test, validating endpoint behavior across flows, and doesn't use the underlying JPA repository that the user also wouldn't have access to.

Class: `OtherRecipeControllerTest.java`

### This project also includes unit testing

Run tests with:

`mvn test` or `./mvnw test`

Docker must be running.

## Notes

- Validation is enforced using Jakarta Bean Validation.

- OpenAPI documentation is generated automatically.

- The project uses real PostgreSQL in tests for realistic behavior.