# Individual Report – Gym Management System

## 3.1 My Contributions

For this project, I was responsible for designing and implementing a large portion of the **backend logic** and **console user interface** for the Gym Management System. My main contributions were:

### Application Structure and Menus
- Implemented the main application entry point in `GymManagementApp`, including:
    - The **main menu** (Register, Login, Exit).
    - The **role-based menus** for:
        - Admin (manage users, memberships, and merchandise).
        - Trainer (manage workout classes, view their own classes, and purchase memberships).
        - Member (browse classes, view membership expenses, and purchase memberships).
- Handled user input with `Scanner` and added validation for numeric and date/time values in all menus.

### User & Authentication System
- Implemented the **user registration and login flow** using `UserService` and `UserDAO`.
- Integrated **BCrypt password hashing** so that:
    - Plain-text passwords are never stored in the database.
    - Login compares the entered password against the stored hash.
- Implemented proper role handling using the `UserRole` enum and specialized user classes:
    - `Admin`, `Trainer`, and `Member` extending `User`.

### Membership Management
- Implemented the `Membership` model class to store membership type, description, cost, member ID, and start/end dates.
- Implemented `MembershipDAO` to:
    - Insert new memberships into the database.
    - Retrieve memberships by member ID.
    - Retrieve all memberships.
    - Calculate **total membership revenue** for the gym.
- Implemented `MembershipService` to:
    - Handle business logic for purchasing memberships (including duration and end date).
    - Calculate **total expenses per member**.
- Connected membership functionality into:
    - Admin menu – view all memberships and total revenue.
    - Trainer and Member menus – purchase memberships and view membership expenses.

### Workout Class Management
- Implemented the `WorkoutClass` model to represent classes with type, description, trainer ID, schedule time, and capacity.
- Built `WorkoutClassDAO` to:
    - Create, update, delete workout classes.
    - List all classes and classes for a specific trainer.
- Implemented `WorkoutClassService` to:
    - Handle creation, updating, deletion, and listing of classes.
- Integrated this into the Trainer and Member menus:
    - Trainers can create, update, delete, and view their own workout classes.
    - Members can browse all available workout classes.

### Gym Merchandise Management
- Created the `GymMerch` model to represent items such as clothing, drinks, and snacks.
- Implemented `GymMerchDAO` to:
    - Insert new merchandise items.
    - Retrieve all items.
    - Calculate the **total stock value** (price × quantity for all items).
- Implemented `GymMerchService` to:
    - Add new items.
    - List all merchandise.
    - Return the calculated stock value.
- Integrated this into:
    - Admin menu – add merchandise, list items, and view total stock value.
    - Trainer and Member menus – view the list of merchandise.

### Database Integration (PostgreSQL)
- Helped design and use the **SQL schema** in `sql/schema.sql`:
    - Created tables: `users`, `memberships`, `workout_classes`, and `gym_merch`.
    - Added foreign key relationships to link memberships and classes to users.
- Configured the `DBConnection` utility to connect to the PostgreSQL database using JDBC.
- Tested database connectivity and queries end-to-end through the DAOs and console menus.

### Logging
- Implemented centralized logging using `LoggerUtil` and `java.util.logging`.
- Configured logging to write to a text file `gym-app.log`.
- Added logging to:
    - User registration and login attempts.
    - Membership purchases.
    - Class creation, updates, and deletions.
    - Merchandise creation and stock value calculations.
- This provides a useful audit trail for debugging and tracking application events.

### GitHub / Version Control
- Used Git and GitHub to manage the project:
    - Cloned the repository and kept changes synchronized.
    - Made incremental commits describing the features implemented (e.g., “Add MembershipService and DAO”, “Implement Trainer menu and class management”).
    - Pushed changes regularly to keep the project backed up and reviewable.
- Ensured that `sql/` and `docs/` folders were included so the database schema and documentation are part of the repository.

---

## 3.2 Challenges Faced During Development

During development, I encountered several challenges:

### 1. Setting Up PostgreSQL and JDBC
Configuring PostgreSQL and connecting from Java using JDBC took some trial and error.  
I had to:
- Make sure the database URL, username, and password in `DBConnection` were correct.
- Confirm the PostgreSQL driver dependency in `pom.xml`.
- Fix errors related to missing tables and incorrect column names.

This process helped me better understand how Java applications talk to relational databases and how important it is for the schema and code to match exactly.

### 2. Secure Password Handling with BCrypt
Implementing BCrypt password hashing was new to me.  
I had to:
- Add the BCrypt dependency.
- Understand how to hash a password before saving it to the database.
- Verify passwords on login by comparing the entered password against the stored hash, instead of comparing plain text.

This was a good learning experience in applying real-world security practices rather than storing passwords directly.

### 3. Designing Role-Based Menus
It was a bit challenging to keep the code organized while adding three different role-based menus (Admin, Trainer, Member).  
I needed to:
- Make sure each role only had access to the correct features.
- Avoid code duplication where possible.
- Keep the menu options readable and simple for users.

I solved this by:
- Structuring the application logic in separate helper methods inside `GymManagementApp`.
- Using the service layer to keep database logic out of the menu code.

### 4. Handling Input and Validation
Another challenge was ensuring the program did not crash or behave strangely when a user entered invalid input (e.g., letters instead of numbers, wrong date formats).

To handle this, I:
- Used `try/catch` blocks around `Integer.parseInt()` and date parsing.
- Printed clear error messages to guide the user to re-enter the value correctly.
- Used a consistent date-time format (`yyyy-MM-dd HH:mm`) for all class schedules.

### 5. Time Management and Project Scope
Because the project includes users, memberships, classes, merchandise, logging, and database integration, it was easy for the scope to feel large and overwhelming.

To manage this:
- I focused on one feature area at a time (users → memberships → classes → merch).
- Tested each piece before moving on.
- Used Git commits as checkpoints, so I could always roll back if I broke something.