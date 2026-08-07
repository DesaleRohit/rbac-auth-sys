# Role-Based Access Control (RBAC)

Role-Based Access Control (RBAC) system built with Spring Boot, Spring Security, JWT, and MySQL - implements token-based authentication and role-based authorization for securing REST APIs

## Features

- Lombok-powered entities and DTOs - clean, boilerplate-free code with `@Getter`,
  `@Setter`, `@Builder`, and `@NoArgsConstructor`/`@AllArgsConstructor`.
- One small, well-commented class per responsibility (JWT creation, JWT filter,
  security rules, service, controllers) so you can trace the whole login → token → access
  flow easily.
- Passwords hashed with BCrypt before they're stored — plaintext passwords are never saved.
- Centralized exception handling that returns clean, consistent JSON error responses.
- Token expiry handling with clear, meaningful error messages on expired/invalid JWTs.
- Role-based method and URL security using Spring Security's `SecurityFilterChain`.


## How the RBAC flow works

- `POST /api/auth/register` — create an account and choose a role (`ADMIN` or `USER`).
- `POST /api/auth/login` — send username + password, get back a JWT token.
- Add that token to the `Authorization: Bearer <token>` header on later requests.
- Spring Security reads the role embedded in your token and decides whether you're
  allowed to hit `/api/admin/**` (ADMIN only) or `/api/user/**` (USER and ADMIN).

```
Register  ──►  Login  ──►  Get JWT  ──►  Call protected endpoint with the JWT
                                          │
                                          ▼
                             Server checks role inside JWT
                                          │
                              ┌───────────┴───────────┐
                              ▼                       ▼
                       Role matches?             Role doesn't match?
                       200 OK response            403 Forbidden
```

## Project structure

```
src/main/java/com/rbacdemo/
├── RbacDemoApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── GlobalExceptionHandler.java
├── controller/
│   ├── AuthController.java
│   ├── AdminController.java
│   ├── UserController.java
├── dto/
│   ├── ApiResponse.java
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   └── RegisterRequest.java
├── entity/
│   ├── User.java
│   └── Role.java
├── repository/
│   └── UserRepository.java
├── security/
│   ├── JwtUtil.java
│   └── JwtAuthFilter.java
└── service/
    └── UserService.java
```

## Tech stack

- Java 17+
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- MySQL 8+
- Lombok
- JWT (jjwt)
- Maven

## Requirements

- Java 17+
- Maven 3.8+
- MySQL 8+
- Postman

## Setup

1. Create a database in MySQL:
   ```sql
   CREATE DATABASE authSys_db;
   ```
2. Update `src/main/resources/application.properties` with your MySQL username and password:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/authSys_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

## How to run

From the project root, run:

```bash
mvn spring-boot:run
```

or build a jar and run it:

```bash
mvn clean package -DskipTests
java -jar target/rbac-demo-1.0.0.jar
```

The app starts on `http://localhost:8080`

Visit `http://localhost:8080/` in your browser — you should see a plain text message
confirming the server is running.

## Testing with Postman

**1. Register an Admin**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "fullName": "Aditi Sharma",
  "username": "admin1",
  "password": "Admin@123",
  "role": "ADMIN"
}
```

**2. Register a normal User**
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "fullName": "Rohit Desale",
  "username": "user1",
  "password": "User@123",
  "role": "USER"
}
```

**3. Login as Admin** (copy the `token` value from the response)
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "admin1",
  "password": "Admin@123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin1",
  "role": "ADMIN"
}
```

**4. Call the Admin-only endpoint**
```
GET http://localhost:8080/api/admin/dashboard
Authorization: Bearer <paste admin token here>
```
Expect: `200 OK` with a welcome message.

**5. Login as the normal User, then try the Admin endpoint with the User's token**
```
GET http://localhost:8080/api/admin/dashboard
Authorization: Bearer <paste user token here>
```
Expect: `403 Forbidden` — this proves RBAC is working, since only ADMIN can access it.

**6. Call the shared User endpoint with either token**
```
GET http://localhost:8080/api/user/profile
Authorization: Bearer <admin or user token>
```
Expect: `200 OK` for both roles, since this endpoint allows `USER` and `ADMIN`.

**7. Call any protected endpoint with no token**
```
GET http://localhost:8080/api/user/profile
```
Expect: `401/403` — access is blocked without a valid token.

## Endpoint summary

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/auth/register` | Public | Create a new account with a role |
| POST | `/api/auth/login` | Public | Get a JWT token |
| GET | `/api/admin/dashboard` | ADMIN only | Sample admin-protected resource |
| GET | `/api/user/profile` | USER or ADMIN | Sample shared resource |


## Author

**Rohit Desale**
GitHub: [@DesaleRohit](https://github.com/DesaleRohit)