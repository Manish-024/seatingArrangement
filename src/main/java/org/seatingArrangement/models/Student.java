package org.seatingArrangement.models;

public class Student extends Person {
    private String rollNumber;
    private int floorNumber;
    private int roomNumber;
    private int seatNumber;
    private String batch;
    private String course;
    private int semester;
    private String subject;

    public Student(String rollNumber, String name) {
        super(name);
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void assignSeat(int floorNumber, int roomNumber, int seatNumber) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.seatNumber = seatNumber;
    }
}
