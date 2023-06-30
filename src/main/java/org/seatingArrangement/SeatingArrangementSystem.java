package org.seatingArrangement;

import java.util.*;
import java.io.*;

import org.seatingArrangement.exceptions.StudentAlreadyExistsException;
import org.seatingArrangement.exceptions.StudentNotFoundException;
import org.seatingArrangement.interfaces.SeatingManagement;
import org.seatingArrangement.models.Student;

public class SeatingArrangementSystem implements SeatingManagement {

    private final List<Student> students;
    private static final int MAX_SEATS_PER_ROOM = 20;
    private final String databaseFile;

    public SeatingArrangementSystem(String databaseFile) {
        this.databaseFile = databaseFile;
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) throws StudentAlreadyExistsException {
        String rollNumber = student.getRollNumber();

        for (Student existingStudent : students) {
            if (existingStudent.getRollNumber().equals(rollNumber)) {
                throw new StudentAlreadyExistsException("A student with the same roll number already exists.");
            }
        }

        students.add(student);
    }


    public void displayAllStudents() {
        // Sorting students
        students.sort(Comparator.comparing(Student::getRollNumber));

        if (students.isEmpty()) {
            System.out.println("No Students found.");
            return;
        }

        System.out.println("All Students:");
        for (Student student : students) {
            String seatInfo = student.getRoomNumber() != 0 ? "Floor: " + student.getFloorNumber() + ", Room: " + student.getRoomNumber() + ", Seat: " + student.getSeatNumber() : "Seat not assigned";
            System.out.println("Roll Number: " + student.getRollNumber());
            System.out.println("Name: " + student.getName());
            System.out.println("Batch: " + student.getBatch());
            System.out.println("Course: " + student.getCourse());
            System.out.println("Semester: " + student.getSemester());
            System.out.println("Subject: " + student.getSubject());
            System.out.println(seatInfo);
            System.out.println("-------------------------");
        }
    }


    public void assignSeat(String rollNumber, int floorNumber, int roomNumber, int seatNumber) {
        if (!isFloorValid(floorNumber)) {
            System.out.println("Invalid floor number.");
            return;
        }

        if (!isRoomValid(roomNumber)) {
            System.out.println("Invalid room number.");
            return;
        }

        if (seatNumber <= 0 || seatNumber > MAX_SEATS_PER_ROOM) {
            System.out.println("Invalid seat number. Seat number should be between 1 and " + MAX_SEATS_PER_ROOM + ".");
            return;
        }

        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                if (isSeatAvailable(floorNumber, roomNumber, seatNumber)) {
                    // check seat availability
                    for (Student existingStudent : students) {
                        if (existingStudent != student && existingStudent.getFloorNumber() == floorNumber &&
                                existingStudent.getRoomNumber() == roomNumber && existingStudent.getSeatNumber() == seatNumber) {
                            System.out.println("Seat is already assigned to another student.");
                            return;
                        }
                    }

                    student.assignSeat(floorNumber, roomNumber, seatNumber);
                    System.out.println("Seat assigned successfully.");
                } else {
                    System.out.println("Seat is already occupied or not available in the specified room.");
                }
                return;
            }
        }
        System.out.println("Student not found.");
    }


    private boolean isRoomValid(int roomNumber) {
        return roomNumber >= 1 && roomNumber <= 10;
    }

    private boolean isFloorValid(int floorNumber) {
        return floorNumber >= 1 && floorNumber <= 5;
    }

    private boolean isSeatAvailable(int floorNumber, int roomNumber, int seatNumber) {
        for (Student student : students) {
            if (student.getFloorNumber() == floorNumber && student.getRoomNumber() == roomNumber && student.getSeatNumber() == seatNumber) {
                return false; // seat occupied
            }
        }
        return true; // seat available
    }

    public void removeStudent(String rollNumber) throws StudentNotFoundException {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                students.remove(student);
                System.out.println("Student removed successfully.");
                return;
            }
        }
        throw new StudentNotFoundException("Student not found.");
    }

    public void displayStudentDetails(String rollNumber) throws StudentNotFoundException {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                String seatInfo = student.getRoomNumber() != 0 ? "Floor: " + student.getFloorNumber() + ", Room: " + student.getRoomNumber() + ", Seat: " + student.getSeatNumber() : "Seat not assigned";
                System.out.println("Student Details:");
                System.out.println("Roll Number: " + student.getRollNumber());
                System.out.println("Name: " + student.getName());
                System.out.println("Batch: " + student.getBatch());
                System.out.println("Course: " + student.getCourse());
                System.out.println("Semester: " + student.getSemester());
                System.out.println("Subject: " + student.getSubject());
                System.out.println("Seat Information: " + seatInfo);
                return;
            }
        }
        throw new StudentNotFoundException("Student not found.");
    }

    public void saveStudents() {
        try (FileWriter writer = new FileWriter(databaseFile)) {
            for (Student student : students) {
                writer.write(student.getRollNumber() + "," + student.getName() +
                        "," + student.getBatch() + "," + student.getCourse() + "," + student.getSemester() + "," +
                        student.getSubject() + "," + student.getFloorNumber() + "," + student.getRoomNumber() + "," +
                        student.getSeatNumber() + "\n");
            }
            System.out.println("Saving Student data...");
        } catch (IOException e) {
            System.out.println("Error occurred while saving student data.");
        }
    }


    public void loadStudents() {
        File file = new File(databaseFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error occurred while creating the database file.");
                return;
            }
        } else {
            try (Scanner scanner = new Scanner(file)) {
                boolean hasStudents = false; // check if student present in DB
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    if (parts.length >= 9) {
                        String rollNumber = parts[0].trim();
                        String name = parts[1].trim();
                        String batch = parts[2].trim();
                        String course = parts[3].trim();
                        int semester = Integer.parseInt(parts[4].trim());
                        String subject = parts[5].trim();
                        int floorNumber = Integer.parseInt(parts[6].trim());
                        int roomNumber = Integer.parseInt(parts[7].trim());
                        int seatNumber = Integer.parseInt(parts[8].trim());

                        Student student = new Student(rollNumber, name);
                        student.setBatch(batch);
                        student.setCourse(course);
                        student.setSemester(semester);
                        student.setSubject(subject);
                        student.assignSeat(floorNumber, roomNumber, seatNumber);
                        students.add(student);

                        hasStudents = true; // set true if any student in DB
                    }
                }

                if (!hasStudents) {
                    System.out.println("No student data found in the file.");
                } else {
                    System.out.println("Student data loaded successfully.");
                }
            } catch (IOException e) {
                System.out.println("Error occurred while loading student data.");
            }
        }
    }
}
