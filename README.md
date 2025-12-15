User Documentation Gym Management System

-- ============================================

    Overview of the Application

The Gym Management System is a simple, easy-to-use program designed to help gym owners and staff manage:

Users (Admins, Trainers, and Members)

Gym memberships

Workout classes

Gym merchandise (shirts, drinks, protein bars, etc.)

The program runs in a menu-based interface, meaning you simply choose numbered options to perform actions. You do not need any technical background — the program guides you step-by-step.

Each person who uses the system logs in with their username and password, and depending on who they are, they will see different options. This is known as role-based access, and it ensures users only see what they should see.

-- ============================================ 2. How the System Works 2.1 Logging In

When the program starts, you can:

Register a new account

Log in with an existing account

Passwords are securely stored using BCrypt hashing, which means your password is never shown or saved in plain text.

2.2 User Roles and What They Can Do

There are three types of users, each with their own menu: -- ============================================

Admin

Admins have full control over the system. They can:

View all users and their contact information

Delete users from the system

View all memberships

View the gym’s total membership revenue

Add new merchandise items

View all merchandise

View the total value of merchandise stock

Admins do not manage workout classes — that is reserved for Trainers.

-- ============================================

Trainer

Trainers can:

Create new workout classes

Update existing workout classes

Delete workout classes they created

View their own workout classes

Purchase a membership for themselves

View gym merchandise

Trainers cannot modify merchandise prices or delete users — that’s Admin-only.

-- ============================================

Member

Members can:

Browse available workout classes

View their total membership expenses

Purchase a new membership for themselves

View merchandise available at the gym

They do not manage classes or other users. -- ============================================

-- ============================================ 3. Explanation of All Classes and Their Interactions

Here is a simple explanation — no technical terminology required.

User (Main Person Class)

Every person in the system starts as a User. A User has:

A username

A password (securely stored)

An email, phone number, and address

A role: Admin, Trainer, or Member

Three special types of users inherit from User:

Admin

Trainer

Member

This means Admins, Trainers, and Members all share the same basic information, but each has access to different menu options.

-- ============================================

Membership

A Membership represents a gym membership purchased by a Member or Trainer. Each membership stores:

Type (e.g., Monthly, Annual)

Description

Cost

When it starts and ends

Who purchased it

Admins can view all memberships. Members and Trainers can purchase their own.

-- ============================================

WorkoutClass

A Workout Class is created and managed by a Trainer. It stores:

Type (Yoga, HIIT, Strength Training, etc.)

A description

Trainer who teaches the class

Schedule date and time

Max capacity

Members can browse classes, but only Trainers can create or modify them.

-- ============================================

GymMerch

Gym merchandise items available for purchase. Each item stores:

Name (e.g., Gym Shirt, Water Bottle)

Type (Gear, Drink, Food)

Price

Quantity in stock

Admins can add new merchandise and view stock value. Members and Trainers can view all merchandise.

-- ============================================

How These Classes Work Together

Users log into the system and interact with memberships, classes, and merchandise.

Memberships are linked to Members and Trainers.

Workout classes are linked to Trainers.

Merchandise exists separately but can be viewed or modified depending on the user’s role.

-- ============================================ 4. Class Diagram (Entity Relationships)

Below is a simple visual description.

         +----------------+
         |     User       |
         |----------------|
         | userId         |
         | username       |
         | passwordHash   |
         | email          |
         | phoneNumber    |
         | address        |
         | role           |
         +----------------+
         /       |        \
        /        |         \

 +-----------+ | Admin | | Trainer | | Member | +-----------+

             Trainer 1 ----- * WorkoutClass
             
             Member 1  ----- * Membership
             
             (Admin manages Users & Merch)

GymMerch is standalone but accessed by all roles.

I can generate a PNG UML diagram for you if you'd like.

-- ============================================ 5. How to Start and Use the System 5.1 Starting the Program

Open IntelliJ IDEA.

Open the project folder.

In the Project panel, navigate to:

src/main/java/com/gymmanagement/app/GymManagementApp.java

Right-click → Run 'GymManagementApp.main()'.

The application will start and show the menu.

-- ============================================ 5.2 Registering a New User

Choose 1. Register from the main menu.

Enter your username, password, and contact information.

Choose your role (Admin, Trainer, Member).

You will receive your User ID once successful.

-- ============================================ 5.3 Logging In

Choose 2. Login.

Enter your username and password.

You will be taken to your role’s menu.

-- ============================================ 5.4 Using the Menus

Each menu shows numbered actions. Just type the number and press Enter.

Examples:

Admins can type 1 to view all users.

Trainers can type 1 to create a new workout class.

Members can type 3 to purchase a membership.

The program will clearly explain what to enter next.

-- ============================================ 5.5 Quitting the System

At any time, choose 0 to go back or log out. From the main menu, choose 0. Exit to close the program. -- ============================================
