package tracker;

public class points {

    private int java;
    private int dsa;
    private int database;
    private int spring;

    public points() {
    }

    public points(int java, int dsa, int database, int spring) {
        this.java = java;
        this.dsa = dsa;
        this.database = database;
        this.spring = spring;
    }

    public int getPointsByCourse(String course) {
        return switch (course) {
            case "Java" -> java;
            case "DSA" -> dsa;
            case "Databases" -> database;
            case "Spring" -> spring;
            default -> 0;
        };
    }


    public int getSpring() {
        return spring;
    }

    public void setSpring(int spring) {
        this.spring = spring;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getDsa() {
        return dsa;
    }

    public void setDsa(int dsa) {
        this.dsa = dsa;
    }

    public int getJava() {
        return java;
    }

    public void setJava(int java) {
        this.java = java;
    }
}
