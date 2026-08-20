Casemanagement.webservice

This is a Spring Boot-based RESTful API project that serves as the backend for a case tracker application. It includes full CRUD operations for cases and follows clean code architecture with separation of concerns across Controller, Service, and Repository layers.

Tech Stack
- Java 17
- Spring Boot
- Spring Web
- RESTful APIs
- Maven
- JUnit 5 / Mockito / AssertJ (testing)

Data storage
There is no real database wired up yet. `CaseRepositoryImpl` stores cases in an in-memory map, so data resets every time the app restarts. Swap it for a JPA-backed implementation once a real database is available — the `CaseRepository` interface and the rest of the layers won't need to change.

Features
- Create a new case
- Retrieve a list of cases
- Get a specific case by ID
- Update case details
- Delete a case

Project Structure
├── controller       // Handles HTTP requests
├── service          // Business logic
├── repository       // Data access layer (currently in-memory)
├── model            // Case entity
├── exception        // Custom exceptions
├── response         // Response envelope helper
├── config           // CORS config for the frontend
├── application.properties

API Endpoints
Method | Endpoint | Description
GET | /cases | Get all cases
GET | /cases/{caseId} | Get case by ID
POST | /cases | Add a new case
PUT | /cases | Update existing case
DELETE | /cases/{caseId} | Delete case by ID

Case fields: caseId, title, description, status, assignedTo, createdDate, flagged. caseId and createdDate are auto-generated on create if omitted; flagged defaults to false.

Frontend integration
CORS is enabled for `http://localhost:3000` and `http://localhost:5173` in `WebConfig`. Add your frontend's dev origin there if it runs elsewhere, and tighten `allowedOrigins` before deploying to production.

Running locally
    ./mvnw spring-boot:run

The API listens on port 8080 by default.
