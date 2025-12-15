Development Documentation
Project Directory Structure

The project follows a standard Maven Java layout:

gym-management/
│
├── src/
│   └── main/
│       └── java/com/gymmanagement/
│           ├── app/            # Application entry point (main menu)
│           ├── model/          # User, Membership, WorkoutClass, Merch classes
│           ├── dao/            # Data Access classes using JDBC
│           ├── service/        # Business logic
│           └── util/           # Utilities (logging, DB connection)
│
├── sql/
│   ├── schema.sql              # Database creation script
│   └── sample_data.sql         # Optional demo data
│
├── docs/
│   ├── UserDocumentation.md
│   ├── DevelopmentDocumentation.md
│   ├── IndividualReport.md
│   └── class-diagram.png
│
├── pom.xml                     # Maven build file
└── README.md

-- ============================================

Key Classes and Responsibilities

App Layer
Class	Responsibility
GymManagementApp	Main entry point, menus, user interaction
Model Layer

These classes represent entities stored in the database:

User (base class)

Admin, Trainer, Member (inherit from User)

Membership

WorkoutClass

GymMerch

DAO Layer

DAO (Data Access Object) classes handle communication with PostgreSQL:

UserDAO

MembershipDAO

WorkoutClassDAO

GymMerchDAO

They contain SQL queries and map database rows to Java objects.

Service Layer

These classes contain business logic:

UserService

MembershipService

WorkoutClassService

GymMerchService

They validate data, call DAOs, handle rules, and log events.

Utility Layer

DBConnection — Creates connections to PostgreSQL

LoggerUtil — Handles the system-wide logger

-- ============================================

Build Process & Dependencies
Build Tool: Maven

This project uses Maven for compiling, dependency management, and packaging.

Dependencies

Listed in pom.xml:

Dependency	Purpose
PostgreSQL JDBC	Connects Java to PostgreSQL database
BCrypt (jBCrypt)	Secure password hashing
JUnit 5	Unit testing
JDK 17+	Required for language features
How to Build

In IntelliJ:

Right-click project → Maven → Reload Project

To compile: Build → Build Project

To run: run GymManagementApp.main().

Using command line:

mvn clean install

-- ============================================

Database Setup for Development

Step 1 — Install PostgreSQL

Ensure PostgreSQL is running and you know your username/password.

Step 2 — Run schema.sql

In pgAdmin:

Open Query Tool

Paste contents of schema.sql

Execute

This creates the required tables:

users

memberships

workout_classes

gym_merch

Step 3 — Optional Demo Data

Run sample_data.sql to add starter merchandise.

Step 4 — Configure Connection

Update DBConnection.java:

private static final String URL = "jdbc:postgresql://localhost:5432/gym_management_db";
private static final String USER = "postgres";
private static final String PASSWORD = "your_password";

-- ============================================

How to Clone & Run the Project from GitHub

Step 1 — Clone the Repository
git clone https://github.com/YourGitHubUsername/gym-management.git

Step 2 — Open in IntelliJ

File → Open → Select the project folder

Step 3 — Build With Maven

IntelliJ will automatically import dependencies.

Step 4 — Configure PostgreSQL

Run schema SQL script

Update DBConnection.java with credentials

Step 5 — Run the App

Open:

src/main/java/com/gymmanagement/app/GymManagementApp.java


Right-click → Run 'GymManagementApp.main()'

-- ============================================