package tracker;

import java.util.*;

public class Main {
    static List<Student> list = new ArrayList<>();
    static int id = 10000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Learning Progress Tracker");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty() || input.isBlank()){
                System.out.println("No input.");
                break;
            }

            switch (input) {
                case "add students":
                    addStudents(scanner);
                    break;
                case "list":
                    listStudents();
                    break;
                case "add points":
                    addPoints(scanner);
                    break;
                case "notify":
                    notifyStudents();
                    break;
                case "find":
                    findById(scanner);
                    break;
                case "statistics":
                    Statistics.showStatistics(list);
                    showCourseDetails(scanner);
                    break;
                case "exit":
                    System.out.println("Bye!");
                    return;
                case "back":
                    System.out.println("Enter 'exit' to exit the program.");
                    break;
                default:
                    System.out.println("Unknown command!");
            }
        }
    }
    private static void notifyStudents() {
        int notifiedCount = 0;
        Set<Student> notifiedStudents = new HashSet<>();

        for (Student student : list) {
            boolean studentNotified = false;

            for (String course : student.getCompletedCourses()) {
                if (!student.hasBeenNotified(course)) {
                    sendNotification(student, course);
                    student.markAsNotified(course);
                    studentNotified = true;  // Mark this student as notified for counting
                }
            }

            if (studentNotified) {
                notifiedStudents.add(student);
            }
        }

        // Count unique students who received at least one notification
        System.out.printf("Total %d student%s have been notified.%n",
                notifiedStudents.size(),
                notifiedStudents.size() > 1 ? "s" : "");
    }

    private static void sendNotification(Student student, String course) {
        System.out.printf("To: %s%n", student.getEmail());
        System.out.println("Re: Your Learning Progress");
        System.out.printf("Hello, %s %s! You have accomplished our %s course!%n",
                student.getFirstName(), student.getLastName(), course);
    }



    private static void addStudents(Scanner scanner) {
        System.out.println("Enter student credentials or 'back' to return:");
        while (scanner.hasNextLine()) {
            String firstInput = scanner.nextLine().trim();
            if (firstInput.equals("back")) {
                System.out.println("Total " + list.size() + " students have been added.");
                return;
            }

            String[] parts = firstInput.split(" ");
            if (parts.length < 3) {
                System.out.println("Incorrect credentials");
                continue;
            }

            String firstName = parts[0];
            String email = parts[parts.length - 1];
            StringBuilder lastNameBuilder = new StringBuilder();
            for (int i = 1; i < parts.length - 1; i++) {
                lastNameBuilder.append(parts[i]).append(" ");
            }
            String lastName = lastNameBuilder.toString().trim();

            if (!isValidName(firstName)) {
                System.out.println("Incorrect first name.");
                continue;
            }
            if (!isValidName(lastName)) {
                System.out.println("Incorrect last name.");
                continue;
            }
            if (!isValidEmail(email)) {
                System.out.println("Incorrect email.");
                continue;
            }

            if (list.stream().anyMatch(s -> s.getEmail().equals(email))) {
                System.out.println("This email is already taken.");
                continue;
            }

            Student student = new Student(id++);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            list.add(student);
            System.out.println("The student has been added.");
        }
    }

    private static void listStudents() {
        if (list.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            list.forEach(student -> System.out.println(student.getId()));
        }
    }

    private static void addPoints(Scanner scanner) {
        System.out.println("Enter an id and points or 'back' to return:");
        while (scanner.hasNextLine()) {
            String firstInput = scanner.nextLine().trim();
            if (firstInput.equals("back")) {
                return;
            }

            String[] input = firstInput.split(" ");
            if (input.length != 5) {
                System.out.println("Incorrect points format.");
                continue;
            }

            String studentId = input[0];
            int[] points = new int[4];
            boolean validPoints = true;

            for (int i = 1; i <= 4; i++) {
                try {
                    points[i - 1] = Integer.parseInt(input[i]);
                    if (points[i - 1] < 0) validPoints = false;
                } catch (NumberFormatException e) {
                    validPoints = false;
                    break;
                }
            }

            if (!validPoints) {
                System.out.println("Incorrect points format.");
                continue;
            }

            Optional<Student> studentOpt = list.stream()
                    .filter(s -> String.valueOf(s.getId()).equals(studentId))
                    .findFirst();

            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                student.points.setJava(student.points.getJava() + points[0]);
                student.points.setDsa(student.points.getDsa() + points[1]);
                student.points.setDatabase(student.points.getDatabase() + points[2]);
                student.points.setSpring(student.points.getSpring() + points[3]);
                System.out.println("Points updated.");
            } else {
                System.out.printf("No student is found for id=%s.%n", studentId);
            }
        }
    }

    private static void findById(Scanner scanner) {
        System.out.println("Enter an id or 'back' to return:");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.equals("back")) {
                return;
            }

            Optional<Student> studentOpt = list.stream()
                    .filter(s -> String.valueOf(s.getId()).equals(input))
                    .findFirst();

            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                System.out.printf("%d points: Java=%d; DSA=%d; Databases=%d; Spring=%d%n",
                        student.getId(),
                        student.points.getJava(),
                        student.points.getDsa(),
                        student.points.getDatabase(),
                        student.points.getSpring());
            } else {
                System.out.printf("No student is found for id=%s.%n", input);
            }
        }
    }

    private static void showCourseDetails(Scanner scanner) {
        String[] courseNames = {"Java", "DSA", "Databases", "Spring"};

        while (scanner.hasNextLine()) {
            String course = scanner.nextLine().trim();
            if (course.equals("back")) return;

            if (!Arrays.asList(courseNames).contains(course)) {
                System.out.println("Unknown course.");
                continue;
            }

            System.out.println(course);
            System.out.println("id\tpoints\tcompleted");
            list.stream()
                    .filter(s -> s.points.getPointsByCourse(course) > 0)
                    .sorted(Comparator.comparingInt((Student s) -> s.points.getPointsByCourse(course)).reversed()
                            .thenComparingInt(Student::getId))
                    .forEach(student -> {
                        int points = student.points.getPointsByCourse(course);
                        double completion = calculateCompletionPercentage(course, points);
                        System.out.printf("%d\t%d\t%.1f%%%n", student.getId(), points, completion);
                    });
        }
    }

    private static double calculateCompletionPercentage(String course, int points) {
        int totalPoints = switch (course) {
            case "Java" -> 600;
            case "DSA" -> 400;
            case "Databases" -> 480;
            case "Spring" -> 550;
            default -> 1;
        };
        return ((double) points / totalPoints) * 100;
    }

    private static boolean isValidName(String name) {
        return name.matches("^[A-Za-z]([A-Za-z]*[ '-]?[A-Za-z]){1,}$");
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z0-9]{1,}$");
    }
}
