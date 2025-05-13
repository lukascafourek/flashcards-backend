# Flashcards Backend

This is the **backend part of the Flash cards web application**, which is part of my bachelor's thesis at the Czech Technical University in Prague (CTU). The backend is developed using **Java Spring Boot** and serves as the API for the frontend application, handling user management, authentication, authorization, and CRUD operations for the project.

The project is **deployed on [Render.com](https://render.com)** and uses a PostgreSQL database for data storage which is **hosted on [Neon.tech](https://neon.tech)**. Generated API documentation is available at **[Swagger](https://flashcards-backend-4nwg.onrender.com/swagger-ui/index.html)**.

---

## Project Structure

The project uses the **package structure** based on the following base package:


### Main Packages

Located under: `src/main/java/cz/cvut/fel/cafoulu1/flashcards/backend`

| Package      | Description                                                                                                                                                                                       |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `config`     | Configuration classes for application setup.                                                                                                                                                      |
| `controller` | REST controllers handling API endpoints. Includes sub-packages for CORS configurations and interceptors.                                                                                          |
| `dto`        | Data Transfer Objects for requests, responses, and basic models.                                                                                                                                  |
| `mapper`     | Classes for mapping between entities and DTOs.                                                                                                                                                    |
| `model`      | Domain models representing database entities. Includes builder sub-package for building objects.                                                                                                  |
| `repository` | Spring Data JPA repositories for database access.                                                                                                                                                 |
| `security`   | Security-related configurations, JWT handling, and response objects.                                                                                                                              |
| `service`    | Interfaces and classes with business logic of entities. It contains sub-packages for chain of responsibility services, email handling, helper utilities, OAuth2 authentication, and user details. |

### Resources

Located under: `src/main/resources`

- `db/changelog`: Liquibase database migration scripts.
- `application.properties`: Main application configuration file.
- `application.yml`: YAML configuration file for application properties.
- `log4j2.properties`: Log4j2 configuration file for logging.

### Test Packages

Located under: `src/test/java/cz/cvut/fel/cafoulu1/flashcards/backend`

| Package            | Description                                                                                |
|--------------------|--------------------------------------------------------------------------------------------|
| `controller`       | Unit and integration tests for controller endpoints.                                       |
| `integrationTests` | Full integration tests simulating real application scenarios.                              |
| `processTests`     | Process flow tests verifying multi-step operations (e.g., authentication flows).           |
| `service`          | Service layer tests. Sub-packages for helpers, OAuth2 services, and user details services. |

---

## Deployment of the Application

The application is **deployed on Render.com** and is built and run using the provided **Dockerfile** in the project root.

### Environment Variables

The application requires several environment variables to be set for the proper configuration. These are configured on Render.com and are not included in the codebase due to their sensitivity.
