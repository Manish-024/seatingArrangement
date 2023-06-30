package org.seatingArrangement;

import java.util.*;

import org.seatingArrangement.exceptions.StudentNotFoundException;
import org.seatingArrangement.exceptions.StudentAlreadyExistsException;
import org.seatingArrangement.models.Student;

public class Main {
    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        Thread mainThread = new Thread(new MainRunnable());
        mainThread.start(); // Using runnable interface to create a thread
    }

    private static class MainRunnable implements Runnable {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            String databaseFile = "src/main/resources/students.csv";
            SeatingArrangementSystem seatingSystem = new SeatingArrangementSystem(databaseFile);

            while (true) {
                System.out.println(ANSI_CYAN + "Select User Type:" + ANSI_RESET);
                System.out.println("1. " + ANSI_GREEN + "Admin" + ANSI_RESET);
                System.out.println("2. " + ANSI_GREEN + "Student" + ANSI_RESET);
                System.out.println("3. " + ANSI_RED + "Exit" + ANSI_RESET);
                System.out.print("Enter your choice: ");
                int userTypeChoice = scanner.nextInt();
                scanner.nextLine();

                switch (userTypeChoice) {
                    case 1:
                        System.out.print("Enter Admin Password: ");
                        String adminPassword = scanner.nextLine();

                        // password authentication for Admin
                        if (authenticateAdmin(adminPassword)) {
                            adminMenu(scanner, seatingSystem);
                        } else {
                            System.out.println(ANSI_RED + "Incorrect password. Access denied." + ANSI_RESET);
                        }
                        break;
                    case 2:
                        // student block
                        System.out.print("Enter Roll Number: ");
                        String rollNumber = scanner.nextLine();
                        try {
                            seatingSystem.displayStudentDetails(rollNumber);
                        } catch (StudentNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        seatingSystem.saveStudents();
                        System.out.println(ANSI_RED + "Exiting..." + ANSI_RESET);
                        System.exit(0);
                        break;
                    default:
                        System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                        break;
                }
            }
        }

        private boolean authenticateAdmin(String password) {
            // Admin authentication logic
            String adminPassword = "admin@123";
            return password.equals(adminPassword);
        }

        private void adminMenu(Scanner scanner, SeatingArrangementSystem seatingSystem) {
            while (true) {
                System.out.println("\n" + ANSI_CYAN + "Admin Menu:" + ANSI_RESET);
                System.out.println("1. " + ANSI_GREEN + "Add Student" + ANSI_RESET);
                System.out.println("2. " + ANSI_GREEN + "Assign Seat" + ANSI_RESET);
                System.out.println("3. " + ANSI_GREEN + "Remove Student" + ANSI_RESET);
                System.out.println("4. " + ANSI_GREEN + "Display Student Details" + ANSI_RESET);
                System.out.println("5. " + ANSI_GREEN + "Display All Students" + ANSI_RESET);
                System.out.println("6. " + ANSI_GREEN + "Save Student Data" + ANSI_RESET);
                System.out.println("7. " + ANSI_RED + "Logout" + ANSI_RESET);
                System.out.print("Enter your choice: ");
                int adminChoice = scanner.nextInt();
                scanner.nextLine();

                switch (adminChoice) {
                    case 1:
                        System.out.print("Enter Roll Number: ");
                        String rollNumber = scanner.nextLine();
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Batch: ");
                        String batch = scanner.nextLine();
                        System.out.print("Enter Course: ");
                        String course = scanner.nextLine();
                        System.out.print("Enter Semester: ");
                        int semester = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Subject: ");
                        String subject = scanner.nextLine();

                        Student student = new Student(rollNumber, name);
                        student.setBatch(batch);
                        student.setCourse(course);
                        student.setSemester(semester);
                        student.setSubject(subject);
                        try {
                            seatingSystem.addStudent(student);
                        } catch (StudentAlreadyExistsException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        System.out.println(ANSI_GREEN + "Student added successfully." + ANSI_RESET);
                        break;
                    case 2:
                        System.out.print("Enter Roll Number: ");
                        rollNumber = scanner.nextLine();
                        System.out.print("Enter Floor Number (1 to 5): ");
                        int floorNumber = scanner.nextInt();
                        System.out.print("Enter Room Number (1 to 10): ");
                        int roomNumber = scanner.nextInt();
                        System.out.print("Enter Seat Number  (1 to 20): ");
                        int seatNumber = scanner.nextInt();
                        seatingSystem.assignSeat(rollNumber, floorNumber, roomNumber, seatNumber);
                        break;
                    case 3:
                        System.out.print("Enter Roll Number: ");
                        rollNumber = scanner.nextLine();
                        try {
                            seatingSystem.removeStudent(rollNumber);
                        } catch (StudentNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Enter Roll Number: ");
                        rollNumber = scanner.nextLine();
                        try {
                            seatingSystem.displayStudentDetails(rollNumber);
                        } catch (StudentNotFoundException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 5:
                        seatingSystem.displayAllStudents();
                        break;
                    case 6:
                        seatingSystem.saveStudents();
                        System.out.println(ANSI_GREEN + "Student data saved successfully." + ANSI_RESET);
                        break;
                    case 7:
                        seatingSystem.saveStudents();
                        System.out.println(ANSI_GREEN + "Student data saved successfully." + ANSI_RESET);
                        System.out.println(ANSI_RED + "Logged out from Admin account." + ANSI_RESET);
                        return;
                    default:
                        System.out.println(ANSI_RED + "Invalid choice. Please try again." + ANSI_RESET);
                        break;
                }
            }
        }
    }
}