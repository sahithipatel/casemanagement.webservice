# Casemanagement.webservice

Backend REST API for a case tracker application. A frontend talks to this service to create, read, update, flag, and delete cases.

> 📸 **Screenshot slot — hero:** a screenshot of the running frontend's case list (once it exists), or of the API responding in Postman/Insomnia. Drop the file in `docs/screenshots/` and reference it below:
> `![Case tracker overview](./screenshots/overview.png)`

---

## 1. Overview

| | |
|---|---|
| **Purpose** | REST backend for a case tracking app — create, view, update, flag, and delete support/work cases |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.4.4 |
| **Data store** | In-memory (`ConcurrentHashMap`) — see [Architecture](#3-architecture) |
| **Build tool** | Maven (via `./mvnw`) |
| **Default port** | `8080` |

## 2. Tech Stack

- **Spring Web** — REST controllers
- **Spring Boot Actuator** — health/metrics endpoints
- **JUnit 5 + Mockito + AssertJ** — unit tests
- **Spring Boot Test (`TestRestTemplate`)** — end-to-end tests over real HTTP

No database dependency is wired up yet — see the [Roadmap](#8-roadmap) for swapping in a real one.

## 3. Architecture

Standard layered architecture. Each layer only knows about the one directly below it:

```
HTTP request
    │
    ▼
CaseController        (src/main/java/.../controller)
    │  calls
    ▼
CaseService            (interface)
CaseServiceImpl         — validation, id/date generation, orchestration
    │  calls
    ▼
CaseRepository          (interface)
CaseRepositoryImpl       — in-memory ConcurrentHashMap<String, Case>
                           ("dummy database" — swap for a JPA repo later
                            without touching controller or service code)
```

Errors flow the other way: `CaseNotFoundException` thrown in the service layer is caught by `CaseExceptionHandler` (`@ControllerAdvice`) and turned into a `404` with a structured JSON body.

> 📸 **Screenshot slot — architecture:** if you draw this as a diagram (e.g. in Excalidraw or draw.io), drop it here instead of the ASCII version above:
> `![Architecture diagram](./screenshots/architecture.png)`

## 4. Project Structure

```
Casemanagement.webservice/
├── pom.xml
├── mvnw, mvnw.cmd
├── docs/
│   └── PROJECT_DOCUMENTATION.md   (this file)
└── src/
    ├── main/java/com/casemanagement/webservice/
    │   ├── CaseManagementApplication.java
    │   ├── controller/CaseController.java
    │   ├── service/CaseService.java
    │   ├── service/impl/CaseServiceImpl.java
    │   ├── repository/CaseRepository.java
    │   ├── repository/impl/CaseRepositoryImpl.java
    │   ├── model/Case.java
    │   ├── exception/ (CaseException, CaseNotFoundException, CaseExceptionHandler)
    │   ├── response/ResponseHandler.java
    │   └── config/WebConfig.java        (CORS for the frontend)
    └── test/java/com/casemanagement/webservice/
        ├── service/impl/CaseServiceImplTest.java
        ├── repository/impl/CaseRepositoryImplTest.java
        └── e2e/CaseControllerE2ETest.java
```

## 5. Getting Started

```bash
git clone https://github.com/sahithipatel/casemanagement.webservice.git
cd casemanagement.webservice
./mvnw spring-boot:run
```

The API is now listening on `http://localhost:8080`.

CORS is pre-enabled for `http://localhost:3000` and `http://localhost:5173` (`WebConfig.java`) — add your frontend's dev origin there if it runs elsewhere.

## 6. API Reference

Base path: `/cases`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/cases` | Get all cases |
| `GET` | `/cases/{caseId}` | Get a case by id |
| `POST` | `/cases` | Create a case |
| `PUT` | `/cases` | Update a case |
| `DELETE` | `/cases/{caseId}` | Delete a case |

### Case object

```json
{
  "caseId": "b6f1f9c2-1e2a-4c3e-9a0d-2f6a8c1e0a11",
  "title": "Server down",
  "description": "Prod server unreachable",
  "status": "OPEN",
  "assignedTo": "NOC Team",
  "createdDate": "2026-08-19",
  "flagged": false
}
```

`caseId` and `createdDate` are generated automatically on create if you don't send them. `flagged` defaults to `false`.

### Example — create a case

```bash
curl -X POST http://localhost:8080/cases \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Server down",
        "description": "Prod server unreachable",
        "status": "OPEN",
        "assignedTo": "NOC Team"
      }'
```

Response: `"Case Created Successfully"`

### Example — get a case

```bash
curl http://localhost:8080/cases/b6f1f9c2-1e2a-4c3e-9a0d-2f6a8c1e0a11
```

```json
{
  "message": "Requested case details are given here",
  "httpStatus": "OK",
  "data": {
    "caseId": "b6f1f9c2-1e2a-4c3e-9a0d-2f6a8c1e0a11",
    "title": "Server down",
    "description": "Prod server unreachable",
    "status": "OPEN",
    "assignedTo": "NOC Team",
    "createdDate": "2026-08-19",
    "flagged": false
  }
}
```

Requesting an unknown `caseId` returns `404` with a `CaseException` body (`message`, `httpStatus`).

> 📸 **Screenshot slot — Postman:** a screenshot of these requests running in Postman/Insomnia with real responses is usually the most useful one for a README.
> `![Postman - create and fetch a case](./screenshots/postman-crud.png)`

## 7. Code Highlights

A few pieces of the codebase worth pointing to if you're walking someone through it or writing this up. Paste the actual snippet (or a GitHub permalink) into each slot below.

**The "dummy database"** — `repository/impl/CaseRepositoryImpl.java` holds every case in a `ConcurrentHashMap` in memory instead of a real database. `save` puts the record in by `caseId`, `findById` looks it up, and `findAll`/`deleteById`/`existsById` follow the same pattern. This is the piece to swap out first once a real database is available.

> 🧩 **Code slot:** `CaseRepositoryImpl` — save/findById
> ```
> (paste from GitHub here)
> ```

**Auto-generating id and created date on create** — `service/impl/CaseServiceImpl.java`'s `createCase` method fills in `caseId` (a random UUID) and `createdDate` (today) only if the caller didn't supply them, so a client can POST a minimal case and still get a usable id back.

> 🧩 **Code slot:** `CaseServiceImpl.createCase`
> ```
> (paste from GitHub here)
> ```

**End-to-end test driving the real HTTP endpoints** — `test/.../e2e/CaseControllerE2ETest.java` boots the app on a random port with `@SpringBootTest(webEnvironment = RANDOM_PORT)` and uses `TestRestTemplate` to run the full create → read → update (including `flagged`) → delete → 404 lifecycle over real HTTP, not mocks.

> 🧩 **Code slot:** `CaseControllerE2ETest.fullCrudLifecycle`
> ```
> (paste from GitHub here)
> ```

> 📸 **Screenshot slot — test run:** a screenshot of `./mvnw test` output (or your IDE's green test tree) is a quick way to show the suite is real and passing.
> `![Test suite passing](./screenshots/tests-passing.png)`

## 8. Testing

| Layer | File | What it covers |
|---|---|---|
| Repository (unit) | `CaseRepositoryImplTest` | The in-memory store directly — find/save/delete |
| Service (unit, mocked repo) | `CaseServiceImplTest` | Business logic — not-found handling, `flagged` defaulting/updating |
| End-to-end | `CaseControllerE2ETest` | Real HTTP calls against a running instance, full CRUD + 404 paths |

Run everything:

```bash
./mvnw clean test
```

## 9. Branching

This project uses short-lived feature branches merged into `main`:

- `feature/case-flag` — added the `flagged` boolean to `Case`
- `feature/e2e-tests` — added `CaseControllerE2ETest` (stacked on `case-flag`)
- `docs/project-documentation` — this document

## 10. Roadmap

- [ ] Swap `CaseRepositoryImpl` for a JPA-backed implementation once a real database is available (the `CaseRepository` interface doesn't need to change)
- [ ] Add validation on create/update (e.g. `title` required)
- [ ] Add pagination to `GET /cases`
- [ ] Wire up authentication once the frontend needs it
