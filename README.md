### ITConf Spring Boot - Solo project

This project is a web-based conference management system built with Spring Boot and Thymeleaf templating engine (HTML views with CSS styling). It provides a platform for managing IT conferences, including attendee registration, session scheduling, and administration via a web interface.

#### Project Structure

- `src/main/java/`
  - **`controller/`** – REST and MVC controllers handling web requests and routing.
  - **`domain/`** – Core domain models/entities (e.g., User, Conference, Session).
  - **`dto/`** – Data Transfer Objects for communication between layers, ensuring loose coupling.
  - **`repository/`** – Data access layer, interfaces for CRUD operations, and JPA entity management.
  - **`service/`** – Business logic, service classes orchestrating domain operations.
  - **`config/`** – Application configuration, security, and setup.
  - **`validation/`** – Custom validators and validation logic for entities and DTOs.

- `src/main/resources/`
  - **`templates/`** – Thymeleaf HTML templates for server-side rendering.
  - **`static/`** – Static resources (CSS, images, JavaScript).
  - **`application.properties`** – Main configuration file, including database connection and JPA settings.
  - **`messages_xx.properties`** – ResourceBundles for multilingual support.

- `src/test/` – Unit and integration tests for all application layers.

- `doc/` – Project documentation and generated Javadoc (if present).

#### Core Functionalities

- **User Registration & Management:** Users can register, log in, there are two roles for authenticated users; Admin and User. The homepage is public so accessible to anyone.
- **Events Scheduling:** Create, update, and view events and their schedules.
- **Add events as favorites:** Users can add events as favorites to easily consult them later on the favorites page.
- **Administration:** Admin users have access to advanced management features such as adding/updating events, chaning schedule times and adding new event rooms.
- **Multilingual Support:** The platform supports multiple languages via ResourceBundles.
- **Validation:** All input is validated both at the DTO and entity level, using Spring Boot built-in validators, annotations and custom validators.
- **Persistence:** Data is managed using Spring Data JPA, following the Repository and DAO patterns.
- **Security:** Spring Security for authentication and access control.

#### Technologies Used

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA (Jakarta Persistence API)**
- **Thymeleaf** (for HTML templates)
- **HTML & CSS**
- **MySQL** (configurable, via JPA)
- **JUnit, Mockito** (for testing)
