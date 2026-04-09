---

## How It Works

1. **Explore and Complete Quests**  
   Users select quests based on real-world locations. Completing a quest unlocks an artefact reward.

2. **Earn Artefacts**  
   Collect artefacts tied to completed quests and view them in your personal collection.

3. **Manage Your Account**  
   Update your account details and track your progress seamlessly within the app.

---

## Technical Overview (Simplified)

- **Frontend (The App)**  
  Built in Java using Android Studio, the app includes:
  - An interactive map to display quests.
  - Features for account management and progress tracking.
  - Asset caching, for offline use without backend access.
  - API integrations.

- **Backend (The Brain)**  
  Powered by a Spring Boot REST API (previously VPS-hosted). It handles:
  - Secure user authentication using JWT.
  - Storing and retrieving quests, artefacts, and user data from a database.

> **Hosting Note:**  
> The backend API and database used to be hosted on Hostinger (while I was in university), but that hosted environment is no longer active.

---

## Build & Run (Local Development)
Note: This project's backend and DB was previously hosted on Hostinger VPS, during my university studies, but that time is long gone now!

### Prerequisites

- Java 17+
- Android Studio (latest stable recommended)
- Gradle (or use the included Gradle wrapper)
- Maven (or use the included Maven wrapper)
- MySQL / MariaDB

### 1) Backend + Database

1. Create a local MySQL/MariaDB database.
2. Run the SQL scripts in this order:
   - `Backend/database/db_creation.sql`
   - `Backend/database/db_population.sql`
       - Note that this db population script populates location-based activities around Ayrshire, Scotland. You will want to edit the lat/lon combinations to run this yourself.
3. Configure backend database connection values in Spring Boot `application.properties`.
4. Build the API:
   - macOS/Linux: `cd Backend/historyhike-api && ./mvnw clean package`
   - Windows: `cd Backend/historyhike-api && mvnw.cmd clean package`
5. Run the API:
   - macOS/Linux: `./mvnw spring-boot:run`
   - Windows: `mvnw.cmd spring-boot:run`

### 2) Android App

1. Open the project in Android Studio.
2. Let Gradle sync finish.
3. Build the app with:
   - macOS/Linux: `./gradlew assembleDebug`
   - Windows: `gradlew.bat assembleDebug`
4. Run on an emulator or Android device from Android Studio.

---

## Why It’s Special

HistoryHike blends real-world exploration with gamification, creating an engaging experience for users. From navigating quests on an interactive map to collecting unique artefacts, it’s a fun and rewarding way to get outdoors and explore!

---

## Acknowledgements

This project represents months of work designing and implementing both the app and its backend. It combines advanced technical features with an accessible and fun user experience.

---
