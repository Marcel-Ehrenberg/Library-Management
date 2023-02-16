package de.lavie.homeoffice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        Login login = new Login();
        Scanner scanner = new Scanner(System.in);
        String action = "";
        String userName;
        String passWord;
        boolean loginCorrect = false;

        do {
            switch (action) {
                case "login":
                    System.out.println("Enter login credentials:");
                    System.out.print("Username: ");
                    userName = scanner.nextLine();
                    System.out.print("Password: ");
                    passWord = scanner.nextLine();

                    loginCorrect = login.login(userName,passWord);
                    action="";
                    break;
                case "register":
                    System.out.println("Register new username and password:");
                    System.out.print("New username: ");
                    userName = scanner.nextLine();
                    System.out.print("New password: ");
                    passWord = scanner.nextLine();

                    login.register(userName, passWord);
                    action="";
                    break;
                default:
                    System.out.println("Please login using an existing user or register a new one. login; register;");
                    action = scanner.nextLine();
                    System.out.println();
                    break;
            }
        } while(!loginCorrect);

            LibraryService libraryService = new LibraryService();
            int index;

            System.out.println("\nLogin complete. Welcome to the library management program!");

            do {
                switch (action) {
                    case "add":
                        System.out.print("Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Author: ");
                        String author = scanner.nextLine();
                        System.out.print("Publisher: ");
                        String publisher = scanner.nextLine();
                        System.out.print("ISBN: ");
                        String ISBN = scanner.nextLine();

                        libraryService.addBook(title, author, publisher, ISBN);
                        System.out.println("\nBook successfully added to the library");

                        action = "";
                        break;
                    case "delete":
                        System.out.print("\nEnter the number of the book to delete: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine());
                            libraryService.deleteBook(index);
                        }
                        catch (NumberFormatException e) {
                            System.out.println("\nGiven Index is invalid.");
                        }
                        action = "";
                        break;
                    case "show":
                        System.out.println("Showing library contents:\n");
                        libraryService.showLibrary();
                        action = "";
                        break;
                    case "borrow":
                        libraryService.showLibrary();
                        System.out.print("\nEnter the number of the book to borrow: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine());
                                LocalDateTime startDateTime;
                                LocalDateTime endDateTime;

                                System.out.println("Start date(yyyy-MM-dd): ");
                                String startDate = scanner.nextLine();
                                System.out.println("Start time(HH:mm): ");
                                String startTime = scanner.nextLine();

                                System.out.println("End date(yyyy-MM-dd): ");
                                String endDate = scanner.nextLine();
                                System.out.println("End time(HH:mm): ");
                                String endTime = scanner.nextLine();
                                try {
                                    startDateTime = LocalDateTime.parse(startDate + "T" + startTime + ":00.000");
                                    endDateTime = LocalDateTime.parse(endDate + "T" + endTime + ":00.000");
                                    libraryService.borrowBook(index, startDateTime, endDateTime);
                                }
                                catch (DateTimeParseException e) {
                                    System.out.println("\nGiven Date and or time is invalid. Please try Again.");
                                }
                        }
                        catch (NumberFormatException e) {
                            System.out.println("\nGiven Index is invalid.");
                        }
                        action = "";
                        break;
                    default:
                        System.out.println("\nWhat action would you like to perform? add; delete; borrow; show; exit;");
                        action = scanner.nextLine();
                        System.out.println();
                        break;
                }
            } while (!action.equals("exit"));
        }
    }
