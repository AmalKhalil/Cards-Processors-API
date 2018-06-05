# CardsProcessors API

## Deployment

### Build

Run `mvn clean install` to build the project. The build artifacts will be stored in the `target/` directory.

### Database

Application use liquibase to manage database creation and changes. Please configure database url, user and password in  application-local.proprties

### Running 

Run `java -jar -Dspring.profiles.active=local target/cards-processors-api-0.0.1-SNAPSHOT.jar` 

Application will be started on http://127.0.0.1:8080

## Architecture
This project is the backend implementation for cards processor. It was implemented using:
- Apache CXF for JAX-RS WS
- Apache Camel for integration, read/write batch file and parsing
- Spring Boot as core framework
- Spring JPA for database access, SQL database was chosen as model is simple and reorting may be needed in future
- Spring Security for securing  rest APIs. Basic authentication was used as a simple technique but in real case, I would go fo OAuth
- Spring WebSockets to provide push back notification to frontend client
- Postgres app was used as it is supported by Heroku




