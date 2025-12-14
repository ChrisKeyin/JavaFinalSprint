package com.gymmanagement.app;

import com.gymmanagement.model.User;
import com.gymmanagement.model.UserRole;
import com.gymmanagement.service.UserService;
import com.gymmanagement.util.LoggerUtil;

import java.io.Console;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class GymManagementApp {

    private static final Logger LOGGER = LoggerUtil.getLogger();

    private final UserService userService;
    private final Scanner scanner;

    public GymManagementApp() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        GymManagementApp app = new GymManagementApp();
        app.run();
    }

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

    private void adminMenu(User admin) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View all users");
            System.out.println("2. Delete a user");
            System.out.println("3. View all memberships & total revenue (TODO)");
            System.out.println("4. Manage merch (TODO)");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    List<User> users = userService.getAllUsers();
                    System.out.println("\n--- All Users ---");
                    for (User u : users) {
                        System.out.println(u);
                    }
                    break;
                case "2":
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
                    break;
                case "3":
                    System.out.println("TODO: Show all memberships & total revenue.");
                    break;
                case "4":
                    System.out.println("TODO: Admin merch management.");
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

    private void trainerMenu(User trainer) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Trainer Menu ---");
            System.out.println("1. Manage workout classes (TODO)");
            System.out.println("2. View my classes (TODO)");
            System.out.println("3. Purchase membership (TODO)");
            System.out.println("4. View merch (TODO)");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                case "2":
                case "3":
                case "4":
                    System.out.println("Feature not implemented yet.");
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

    private void memberMenu(User member) {
        boolean stay = true;
        while (stay) {
            System.out.println("\n--- Member Menu ---");
            System.out.println("1. Browse workout classes (TODO)");
            System.out.println("2. View my membership expenses (TODO)");
            System.out.println("3. Purchase membership (TODO)");
            System.out.println("4. View merch (TODO)");
            System.out.println("0. Logout");
            System.out.print("Enter option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                case "2":
                case "3":
                case "4":
                    System.out.println("Feature not implemented yet.");
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
}
