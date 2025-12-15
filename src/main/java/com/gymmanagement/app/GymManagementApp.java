package com.gymmanagement.app;

import com.gymmanagement.model.User;
import com.gymmanagement.model.UserRole;
import com.gymmanagement.model.WorkoutClass;
import com.gymmanagement.model.Membership;
import com.gymmanagement.model.GymMerch;
import com.gymmanagement.service.UserService;
import com.gymmanagement.service.MembershipService;
import com.gymmanagement.service.WorkoutClassService;
import com.gymmanagement.service.GymMerchService;
import com.gymmanagement.util.LoggerUtil;

import java.io.Console;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Main application class for the Gym Management System.
 * Handles user interaction through a menu-driven CLI interface.
 * Supports three user roles: Admin, Trainer, and Member, each with unique functionality.
 */
public class GymManagementApp {

    private static final Logger LOGGER = LoggerUtil.getLogger();
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Service instances for business logic
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService workoutClassService;
    private final GymMerchService gymMerchService;
    private final Scanner scanner;

    /**
     * Constructor that initializes all services and the scanner for user input.
     */
    public GymManagementApp() {
        this.userService = new UserService();
        this.membershipService = new MembershipService();
        this.workoutClassService = new WorkoutClassService();
        this.gymMerchService = new GymMerchService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Entry point for the application.
     * Creates an instance of GymManagementApp and starts the main loop.
     */
    public static void main(String[] args) {
        GymManagementApp app = new GymManagementApp();
        app.run();
    }

    /**
     * Main application loop that displays the initial menu and handles user choices.
     * Routes users to registration or login based on their selection.
     */
    public void run() {
        LOGGER.info("Gym Management Application started.");
        boolean running = true;

        while (running) {
            System.out.println("==================================");
            System.out.println("      GYM MANAGEMENT SYSTEM");
            System.out.println("==================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleRegistration();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "0":
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    LOGGER.info("Application exited by user.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Securely reads a password from the console.
     * Uses System.console() if available to hide input, falls back to standard input otherwise.
     */
    private String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            if (passwordChars == null) {
                return "";
            }
            return new String(passwordChars).trim();
        } else {
            System.out.print(prompt);
            return scanner.nextLine().trim();
        }
    }

    /**
     * Handles new user registration.
     * Collects user information (username, password, email, phone, address) and assigns a role.
     */
    private void handleRegistration() {
        System.out.println("\n--- User Registration ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        String password = readPassword("Enter password: ");

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter address: ");
        String address = scanner.nextLine().trim();

        System.out.println("Select role:");
        System.out.println("1. Admin");
        System.out.println("2. Trainer");
        System.out.println("3. Member");
        System.out.print("Enter option: ");
        String roleChoice = scanner.nextLine().trim();

        UserRole role;
        switch (roleChoice) {
            case "1":
                role = UserRole.ADMIN;
                break;
            case "2":
                role = UserRole.TRAINER;
                break;
            case "3":
            default:
                role = UserRole.MEMBER;
                break;
        }

        User newUser = userService.registerUser(username, password, email, phone, address, role);
        if (newUser != null) {
            System.out.println("Registration successful! Your user ID is: " + newUser.getUserId());
        } else {
            System.out.println("Registration failed. Username might already exist.");
        }
    }

    /**
     * Handles user login authentication.
     * Verifies credentials and routes the authenticated user to their respective menu based on role.
     */
    private void handleLogin() {
        System.out.println("\n--- Login ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        String password = readPassword("Enter password: ");

        User user = userService.login(username, password);
        if (user == null) {
            System.out.println("Login failed. Check your username or password.");
            return;
        }

        System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");

        switch (user.getRole()) {
            case ADMIN:
                adminMenu(user);
                break;
            case TRAINER:
                trainerMenu(user);
                break;
            case MEMBER:
                memberMenu(user);
                break;
            default:
                System.out.println("Unknown role. Logging out.");
        }
    }

    /**
     * Admin menu providing access to user management, membership tracking, and merchandise management.
     */
    private void adminMenu(User admin) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View all users");
            System.out.println("2. Delete a user");
            System.out.println("3. View all memberships & total revenue");
            System.out.println("4. Manage merch");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showAllUsers();
                    break;
                case "2":
                    deleteUserById();
                    break;
                case "3":
                    showAllMembershipsAndRevenue();
                    break;
                case "4":
                    adminMerchMenu();
                    break;
                case "0":
                    stay = false;
                    System.out.println("Logging out from Admin menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays all registered users in the system.
     */
    private void showAllUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println("\n--- All Users ---");
        for (User u : users) {
            System.out.println(u);
        }
    }

    /**
     * Deletes a user from the system by their ID.
     */
    private void deleteUserById() {
        System.out.print("Enter user ID to delete: ");
        String idStr = scanner.nextLine().trim();
        try {
            int id = Integer.parseInt(idStr);
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user. Check the ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    /**
     * Displays all memberships and calculates total revenue from all memberships.
     */
    private void showAllMembershipsAndRevenue() {
        List<Membership> memberships = membershipService.getAllMemberships();
        System.out.println("\n--- All Memberships ---");
        for (Membership m : memberships) {
            System.out.println(m);
        }
        BigDecimal totalRevenue = membershipService.getTotalRevenue();
        System.out.println("Total Membership Revenue: $" + totalRevenue);
    }

    /**
     * Admin submenu for managing gym merchandise (add, view, check stock value).
     */
    private void adminMerchMenu() {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Admin Merch Management ---");
            System.out.println("1. Add merch item");
            System.out.println("2. View all merch items");
            System.out.println("3. View total stock value");
            System.out.println("0. Back");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addMerchItem();
                    break;
                case "2":
                    listAllMerch();
                    break;
                case "3":
                    BigDecimal totalValue = gymMerchService.getTotalStockValue();
                    System.out.println("Total merch stock value: $" + totalValue);
                    break;
                case "0":
                    stay = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Adds a new merchandise item to the gym's inventory.
     */
    private void addMerchItem() {
        System.out.print("Enter merch name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter merch type (e.g. Gear, Drink, Food): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter merch price: ");
        String priceStr = scanner.nextLine().trim();

        System.out.print("Enter quantity in stock: ");
        String qtyStr = scanner.nextLine().trim();

        try {
            BigDecimal price = new BigDecimal(priceStr);
            int quantity = Integer.parseInt(qtyStr);

            GymMerch created = gymMerchService.addMerchItem(name, type, price, quantity);
            if (created != null) {
                System.out.println("Merch item added with ID: " + created.getMerchId());
            } else {
                System.out.println("Failed to add merch item.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price or quantity.");
        }
    }

    /**
     * Displays all merchandise items available in the gym.
     */
    private void listAllMerch() {
        List<GymMerch> merchList = gymMerchService.getAllMerch();
        System.out.println("\n--- Gym Merchandise ---");
        for (GymMerch m : merchList) {
            System.out.println(m);
        }
    }

    /**
     * Trainer menu providing access to work out class management and membership purchasing.
     */
    private void trainerMenu(User trainer) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Trainer Menu ---");
            System.out.println("1. Create workout class");
            System.out.println("2. Update workout class");
            System.out.println("3. Delete workout class");
            System.out.println("4. View my classes");
            System.out.println("5. Purchase membership for myself");
            System.out.println("6. View merch items");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createWorkoutClass(trainer);
                    break;
                case "2":
                    updateWorkoutClass(trainer);
                    break;
                case "3":
                    deleteWorkoutClass(trainer);
                    break;
                case "4":
                    viewTrainerClasses(trainer);
                    break;
                case "5":
                    purchaseMembershipForUser(trainer);
                    break;
                case "6":
                    listAllMerch();
                    break;
                case "0":
                    stay = false;
                    System.out.println("Logging out from Trainer menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Creates a new workout class for the trainer.
     * Collects class details and schedules the class.
     */
    private void createWorkoutClass(User trainer) {
        System.out.print("Enter workout class type (e.g. Yoga): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter class description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Enter schedule time (yyyy-MM-dd HH:mm): ");
        String timeStr = scanner.nextLine().trim();

        System.out.print("Enter capacity: ");
        String capStr = scanner.nextLine().trim();

        try {
            LocalDateTime scheduleTime = LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER);
            int capacity = Integer.parseInt(capStr);

            WorkoutClass created = workoutClassService.createClass(
                    trainer.getUserId(), type, description, scheduleTime, capacity
            );

            if (created != null) {
                System.out.println("Workout class created with ID: " + created.getWorkoutClassId());
            } else {
                System.out.println("Failed to create workout class.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/time format. Please use yyyy-MM-dd HH:mm");
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity value.");
        }
    }

    /**
     * Updates an existing workout class owned by the trainer.
     */
    private void updateWorkoutClass(User trainer) {
        System.out.print("Enter workout class ID to update: ");
        String idStr = scanner.nextLine().trim();

        try {
            int classId = Integer.parseInt(idStr);

            System.out.print("Enter new class type: ");
            String type = scanner.nextLine().trim();

            System.out.print("Enter new description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter new schedule time (yyyy-MM-dd HH:mm): ");
            String timeStr = scanner.nextLine().trim();

            System.out.print("Enter new capacity: ");
            String capStr = scanner.nextLine().trim();

            LocalDateTime scheduleTime = LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER);
            int capacity = Integer.parseInt(capStr);

            WorkoutClass updatedClass = new WorkoutClass(
                    classId, type, description, trainer.getUserId(), scheduleTime, capacity
            );

            boolean updated = workoutClassService.updateClass(updatedClass);
            if (updated) {
                System.out.println("Workout class updated successfully.");
            } else {
                System.out.println("Failed to update workout class. Check the ID and ownership.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID or capacity value.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/time format. Please use yyyy-MM-dd HH:mm");
        }
    }

    /**
     * Deletes a workout class owned by the trainer.
     */
    private void deleteWorkoutClass(User trainer) {
        System.out.print("Enter workout class ID to delete: ");
        String idStr = scanner.nextLine().trim();

        try {
            int classId = Integer.parseInt(idStr);
            boolean deleted = workoutClassService.deleteClass(classId, trainer.getUserId());
            if (deleted) {
                System.out.println("Workout class deleted successfully.");
            } else {
                System.out.println("Failed to delete workout class. Check the ID and ownership.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    /**
     * Displays all workout classes created by the trainer.
     */
    private void viewTrainerClasses(User trainer) {
        List<WorkoutClass> classes = workoutClassService.getClassesForTrainer(trainer.getUserId());
        System.out.println("\n--- My Workout Classes ---");
        for (WorkoutClass wc : classes) {
            System.out.println(wc);
        }
    }

    /**
     * Purchases a membership for a user (trainer or member).
     * Collects membership type, description, cost, and duration.
     */
    private void purchaseMembershipForUser(User user) {
        System.out.print("Enter membership type (e.g. Monthly, Annual): ");
        String type = scanner.nextLine().trim();

        System.out.print("Enter membership description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Enter membership cost: ");
        String costStr = scanner.nextLine().trim();

        System.out.print("Enter duration in months: ");
        String monthsStr = scanner.nextLine().trim();

        try {
            BigDecimal cost = new BigDecimal(costStr);
            int months = Integer.parseInt(monthsStr);

            Membership membership = membershipService.purchaseMembership(
                    user.getUserId(), type, description, cost, months
            );

            if (membership != null) {
                System.out.println("Membership purchased with ID: " + membership.getMembershipId());
            } else {
                System.out.println("Failed to purchase membership.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid cost or duration.");
        }
    }

    /**
     * Member menu providing access to browse classes, view expenses, purchase membership, and view merchandise.
     */
    private void memberMenu(User member) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Member Menu ---");
            System.out.println("1. Browse workout classes");
            System.out.println("2. View my membership expenses");
            System.out.println("3. Purchase membership");
            System.out.println("4. View merch items");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    browseWorkoutClasses();
                    break;
                case "2":
                    viewMemberExpenses(member);
                    break;
                case "3":
                    purchaseMembershipForUser(member);
                    break;
                case "4":
                    listAllMerch();
                    break;
                case "0":
                    stay = false;
                    System.out.println("Logging out from Member menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays all available workout classes to members.
     */
    private void browseWorkoutClasses() {
        List<WorkoutClass> classes = workoutClassService.getAllClasses();
        System.out.println("\n--- Available Workout Classes ---");
        for (WorkoutClass wc : classes) {
            System.out.println(wc);
        }
    }

    /**
     * Displays all memberships and total expenses for a specific member.
     */
    private void viewMemberExpenses(User member) {
        BigDecimal total = membershipService.getTotalExpensesForMember(member.getUserId());
        List<Membership> memberships = membershipService.getMembershipsForMember(member.getUserId());

        System.out.println("\n--- My Memberships ---");
        for (Membership m : memberships) {
            System.out.println(m);
        }
        System.out.println("Total spent on memberships: $" + total);
    }
}
