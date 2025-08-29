# Bajaj Finserv Health | Qualifier 1 | JAVA — Auto Runner

This Spring Boot app performs the entire flow **on startup** (no controllers):
1. Calls `POST /generateWebhook/JAVA` with your `name`, `regNo`, and `email`.
2. Receives `{ webhook, accessToken }`.
3. Decides which SQL question you got based on the last two digits of your `regNo` (odd → Q1, even → Q2).
4. Generates a **final SQL query** via a pluggable solver.
5. Persists it to `target/finalQuery.sql`.
6. Submits the JSON `{"finalQuery": "..."}` to the **returned `webhook` URL** with `Authorization: <accessToken>`.
   - If `webhook` is missing for any reason, it falls back to `POST /testWebhook/JAVA` as per the PDF.

> NOTE: You **must update** `src/main/resources/application.yml` with your real `name`, `regNo`, and `email`.
> Also, edit the solver(s) to return your actual query for your assigned question.

## Build & Run

```bash
# Java 17 + Maven required
mvn -v

# Build JAR
mvn -q -DskipTests package

# Run
java -jar target/bfh-java-qualifier-1.0.0.jar
```

On success, you'll see logs and a file:
- `target/finalQuery.sql` contains the exact query submitted.

## Where to put your SQL

- Implement your real solution in:
  - `com.example.bfhqualifier.solver.Q1Solver` (for odd regNo last two digits), or
  - `com.example.bfhqualifier.solver.Q2Solver` (for even).

Replace the placeholder `SELECT ...` with your final query.

## Repo Checklist (per PDF)
- Public GitHub repo with:
  - Source code
  - Built JAR
  - Raw downloadable link to JAR
- Public JAR link

Good luck!
