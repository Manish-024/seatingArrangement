package org.seatingArrangement.interfaces;

import org.seatingArrangement.exceptions.StudentAlreadyExistsException;
import org.seatingArrangement.exceptions.StudentNotFoundException;
import org.seatingArrangement.models.Student;

public interface SeatingManagement {
    void addStudent(Student student) throws StudentAlreadyExistsException;
    void removeStudent(String rollNumber) throws StudentNotFoundException;
    void displayStudentDetails(String rollNumber) throws StudentNotFoundException;
    void displayAllStudents();
}
