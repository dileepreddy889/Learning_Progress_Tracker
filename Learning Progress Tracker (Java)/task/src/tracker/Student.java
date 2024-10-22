package tracker;

import java.util.HashSet;
import java.util.Set;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    points points;

    private Set<String> notifiedCourses = new HashSet<>();

    public Student(int id) {
        this.id = id;
        this.points = new points();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getCompletedCourses() {
        Set<String> completedCourses = new HashSet<>();
        if (points.getJava() >= 600) completedCourses.add("Java");
        if (points.getDsa() >= 400) completedCourses.add("DSA");
        if (points.getDatabase() >= 480) completedCourses.add("Databases");
        if (points.getSpring() >= 550) completedCourses.add("Spring");
        return completedCourses;
    }

    public boolean hasBeenNotified(String course) {
        return notifiedCourses.contains(course);
    }

    public void markAsNotified(String course) {
        notifiedCourses.add(course);
    }
}
