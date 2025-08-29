# BFH Java Qualifier — Auto Runner (Final)

This repo is ready to submit.

- Runs the whole flow **on startup** (no controllers).
- Uses **WebClient** to call the BFH APIs.
- Detects **Q1 (odd)** / **Q2 (even)** from the last-2 digits of `regNo`.
- **Q1 (MySQL)** solution is already integrated.
- Your details are set in `src/main/resources/application.yml`.
- A **GitHub Actions** workflow compiles the JAR and commits it to `/release/`.

## Your Details
- Name: **Aayush Singh**
- RegNo: **22BCE0769**
- Email: **atollsemzer@gmail.com**

## Build Locally (optional)
Requires Java 17 + Maven:
```bash
mvn -q -DskipTests package
java -jar target/bfh-java-qualifier-1.0.0.jar
```

## CI Build (no local Java/Maven needed)
GitHub Actions workflow at `.github/workflows/build-and-commit-jar.yml`:
- Trigger it from the **Actions** tab → **Run workflow**
- It builds and puts the jar at `release/bfh-java-qualifier.jar` (and commits it)

**Raw JAR link format (after CI run):**
```
https://github.com/<your-username>/bfh-java-qualifier/raw/main/release/bfh-java-qualifier.jar
```
