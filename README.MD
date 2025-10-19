# Library Management System

A simple **Library Management System** built with **Java 17**, **Spring Boot**, and **Maven**, with a RESTful API for managing books. The project uses **H2 in-memory database** for easy setup and testing. Swagger UI is included for API documentation and testing.

---

## Features

- Add new books with validation
- Fetch books by ID
- Prevent duplicate ISBN entries
- Clean JSON responses (null fields omitted)
- Swagger UI documentation

---

## Technologies Used

- Java 17
- Spring Boot 3
- Maven
- H2 Database
- Jakarta Validation
- Jackson JSON
- Springdoc OpenAPI (Swagger UI)

---

## Prerequisites

- Java 17 installed
- Maven installed
- Git installed

---

## Getting Started

1. **Clone the repository:**
    - git clone <your-git-repo-url>
    - cd library-management-system

2. **Build the project using Maven:**
    - mvn clean install

3. **Run the application:**
    - mvn spring-boot:run

The application will start on:
    - http://localhost:8080

---

## API Endpoints

| Method | URL              | Description            |
|--------|-----------------|-----------------------|
| POST   | `/api/books`     | Add a new book        |
| GET    | `/api/books/{id}`| Fetch book by ID      |

### Swagger UI & API Documentation
    - http://localhost:8080/swagger-ui/index.html

---

## Database

This project uses **H2 in-memory database**, so no external database setup is required.

- H2 Console (for testing queries):

http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:librarydb`
- User: `sa`
- Password: *(leave blank)*

---

## Validation

- `@NotBlank` is used on `title`, `author`, and `isbn` fields to ensure mandatory values.
- Duplicate ISBN entries are blocked.
- `availability` defaults to `true` when creating a new book.

---

## JSON Response Example

{
"id": 1,
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884",
"availability": true
}


> Fields with `null` values are omitted automatically.

---

## Example Requests

### Add a Book (POST)

curl -X POST http://localhost:8080/api/books
-H "Content-Type: application/json"
-d '{
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884"
}'

makefile
Copy code

**Response:**

{
"id": 1,
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884",
"availability": true
}

vbnet
Copy code

### Get Book by ID (GET)

curl -X GET http://localhost:8080/api/books/1

makefile
Copy code

**Response:**

{
"id": 1,
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884",
"availability": true
}

yaml
Copy code

---

## Notes

- This project uses **Springdoc OpenAPI** for auto-generating Swagger documentation.
- Ideal for quick prototyping or interview projects.
- Can be extended with features like updating books, deleting books, or connecting to MySQL/PostgreSQL for production.

---