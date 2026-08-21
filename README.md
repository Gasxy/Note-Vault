# Note-Vault

A role-based note management application built with Spring Boot. Note-Vault lets users create and manage their own private notes while administrators can view and manage every note in the system. The application exposes both a server-rendered Thymeleaf UI and a JSON REST API.

## Features

- Personal notes scoped to each authenticated user
- Administrative oversight — admins can view and delete any note
- Thymeleaf MVC UI for browser-based interactions
- REST API at `/api/notes` for programmatic access
- Form-based authentication with role-based authorization
- Pre-seeded H2 database for quick demo / development use
- H2 web console enabled at `/h2-console`

## Tech Stack

| Layer       | Technology                                      |
|-------------|-------------------------------------------------|
| Language    | Java 17                                         |
| Framework   | Spring Boot 3.3.4                               |
| Web         | Spring MVC + Thymeleaf                          |
| API         | Spring Web (`@RestController`)                  |
| Security    | Spring Security (form login + HTTP Basic)       |
| Persistence | Spring Data JPA + H2 in-memory database         |
| Build       | Maven                                           |

## Prerequisites

- JDK 17 or later
- Maven 3.8+

## Project Structure

```
src/main/java/com/example/notevault
├── NoteVaultApplication.java
├── config/SecurityConfig.java
├── controller/
│   ├── NoteMvcController.java
│   └── NoteRestController.java
├── model/
│   ├── Note.java
│   └── Role.java
├── repository/NoteRepository.java
└── service/NoteService.java

src/main/resources
├── application.properties
├── data.sql
└── templates/
    ├── admin.html
    ├── login.html
    └── notes.html
```

## URL Map

| Path            | Method        | Access                       | Description                          |
|-----------------|---------------|------------------------------|--------------------------------------|
| `/`             | GET           | Public                       | Redirects to `/notes`                |
| `/login`        | GET           | Public                       | Login page                           |
| `/notes`        | GET           | USER, ADMIN                  | List notes visible to current user   |
| `/notes`        | POST          | USER, ADMIN                  | Create a note (owner = current user) |
| `/notes/delete/{id}` | POST    | USER, ADMIN (owner or admin) | Delete a note                        |
| `/admin`        | GET           | ADMIN                        | Admin dashboard                      |
| `/h2-console`   | GET           | Public (dev)                 | H2 database web console             |
| `/api/notes`    | GET           | USER, ADMIN                  | List notes as JSON                   |
| `/api/notes`    | POST          | USER, ADMIN                  | Create a note as JSON                |
| `/api/notes/{id}` | DELETE      | USER, ADMIN (owner or admin) | Delete a note                        |

## REST API Examples

Get all notes visible to the authenticated user:

```bash
curl -u user:password http://localhost:8080/api/notes
```

Create a note:

```bash
curl -u user:password -H "Content-Type: application/json" \
     -d '{"title":"Grocery","content":"Apples, Pears"}' \
     http://localhost:8080/api/notes
```

Delete a note:

```bash
curl -u user:password -X DELETE http://localhost:8080/api/notes/1
```

## Configuration

Application settings live in `src/main/resources/application.properties`. The H2 datasource is in-memory and is re-seeded on every startup from `data.sql`. To use a persistent database, change the `spring.datasource.*` properties and update the JDBC driver.

## Security Notes

- Passwords are hashed with BCrypt.
- CSRF protection is enabled for browser endpoints and disabled for `/api/**` and `/h2-console/**` to ease API testing.
- HTTP Basic auth is enabled for the REST API; the UI uses form login.
- Frame options are disabled globally so the H2 console can render.
