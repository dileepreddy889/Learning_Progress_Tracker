package tracker;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    public static void showStatistics(List<Student> students) {
        System.out.println("Type the name of a course to see details or 'back' to quit:");

        Map<String, List<Integer>> courseData = initializeCourseData(students);

        System.out.println("Most popular: " + getCoursesWithMaxEnrollments(courseData));
        System.out.println("Least popular: " + getCoursesWithMinEnrollments(courseData));
        System.out.println("Highest activity: " + getCoursesWithMaxActivity(courseData));
        System.out.println("Lowest activity: " + getCoursesWithMinActivity(courseData));
        System.out.println("Easiest course: " + getCoursesWithHighestAverage(courseData));
        System.out.println("Hardest course: " + getCoursesWithLowestAverage(courseData));
    }


    private static Map<String, List<Integer>> initializeCourseData(List<Student> students) {
        Map<String, List<Integer>> courseData = new HashMap<>();
        courseData.put("Java", new ArrayList<>());
        courseData.put("DSA", new ArrayList<>());
        courseData.put("Databases", new ArrayList<>());
        courseData.put("Spring", new ArrayList<>());

        for (Student student : students) {
            courseData.get("Java").add(student.points.getJava());
            courseData.get("DSA").add(student.points.getDsa());
            courseData.get("Databases").add(student.points.getDatabase());
            courseData.get("Spring").add(student.points.getSpring());
        }
        return courseData;
    }

    private static String getCoursesWithMaxEnrollments(Map<String, List<Integer>> courseData) {
        int maxEnrollments = courseData.values().stream()
                .mapToInt(list -> (int) list.stream().filter(points -> points > 0).count())
                .max().orElse(0);

        if (maxEnrollments == 0) return "n/a";  // No enrollments at all

        return courseData.entrySet().stream()
                .filter(entry -> entry.getValue().stream().filter(points -> points > 0).count() == maxEnrollments)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private static String getCoursesWithMinEnrollments(Map<String, List<Integer>> courseData) {
        int minEnrollments = courseData.values().stream()
                .mapToInt(list -> (int) list.stream().filter(points -> points > 0).count())
                .min().orElse(0);

        if (minEnrollments == 0 || allCoursesHaveSameEnrollment(courseData, minEnrollments)) {
            return "n/a";
        }

        return courseData.entrySet().stream()
                .filter(entry -> entry.getValue().stream().filter(points -> points > 0).count() == minEnrollments)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private static boolean allCoursesHaveSameEnrollment(Map<String, List<Integer>> courseData, int enrollmentCount) {
        return courseData.values().stream()
                .allMatch(list -> list.stream().filter(points -> points > 0).count() == enrollmentCount);
    }

    private static String getCoursesWithMaxActivity(Map<String, List<Integer>> courseData) {
        // Calculate activity: count how many times points were awarded in each course
        Map<String, Long> activityMap = courseData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().filter(points -> points > 0).count()
                ));

        // Find the maximum activity value
        long maxActivity = activityMap.values().stream().max(Long::compare).orElse(0L);

        // If the maximum activity is zero, return "n/a"
        if (maxActivity == 0) return "n/a";

        // Collect all courses with the maximum activity
        return activityMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxActivity)
                .map(Map.Entry::getKey)
                .sorted() // Alphabetical order
                .collect(Collectors.joining(", "));
    }

    private static String getCoursesWithMinActivity(Map<String, List<Integer>> courseData) {
        // Calculate activity: how many times points were awarded in each course
        Map<String, Long> activityMap = courseData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().filter(points -> points > 0).count()
                ));

        // Find the minimum activity value
        long minActivity = activityMap.values().stream().min(Long::compare).orElse(0L);

        // Check if all courses have the same activity or if the minimum activity is zero
        if (minActivity == 0 || allCoursesHaveSameActivity(activityMap, minActivity)) {
            return "n/a";
        }

        // Collect all courses with the minimum activity
        return activityMap.entrySet().stream()
                .filter(entry -> entry.getValue() == minActivity)
                .map(Map.Entry::getKey)
                .sorted() // Optional: for consistent alphabetical output
                .collect(Collectors.joining(", "));
    }

    private static boolean allCoursesHaveSameActivity(Map<String, Long> activityMap, long activityCount) {
        // Check if all courses have the same activity count
        return activityMap.values().stream().allMatch(activity -> activity == activityCount);
    }


    private static String getCoursesWithHighestAverage(Map<String, List<Integer>> courseData) {
        double highestAverage = courseData.values().stream()
                .mapToDouble(list -> list.stream().mapToInt(Integer::intValue).average().orElse(0))
                .max().orElse(0);

        if (highestAverage == 0) return "n/a";  // No averages available

        return courseData.entrySet().stream()
                .filter(entry -> entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0) == highestAverage)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private static String getCoursesWithLowestAverage(Map<String, List<Integer>> courseData) {
        double lowestAverage = courseData.values().stream()
                .mapToDouble(list -> list.stream().mapToInt(Integer::intValue).average().orElse(0))
                .min().orElse(0);

        if (lowestAverage == 0 || allCoursesHaveSameAverage(courseData, lowestAverage)) {
            return "n/a";
        }

        return courseData.entrySet().stream()
                .filter(entry -> entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0) == lowestAverage)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private static boolean allCoursesHaveSameAverage(Map<String, List<Integer>> courseData, double average) {
        return courseData.values().stream()
                .allMatch(list -> list.stream().mapToInt(Integer::intValue).average().orElse(0) == average);
    }
}
