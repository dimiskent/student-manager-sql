import java.sql.*;
import java.util.Scanner;

public class StudentManager {
    private final Connection connection;
    private final Scanner scanner;
    public final boolean isReady;
    //  TODO:
    //    List all students
    //    Find student by ID
    //    Update student email
    //    Delete student
    public StudentManager(String url, String user, String password) {
        Connection testConnection;
        try {
            testConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            testConnection = null;
        }
        connection = testConnection;
        isReady = connection != null;
        scanner = new Scanner(System.in);
    }
    public void close() {
        scanner.close();
        try {
            if(connection != null && connection.isClosed()) connection.close();
        } catch (SQLException e) {
            System.out.println("Cannot close connection? " + e.getMessage());
        }
    }
    public String getUserInput(String msg) {
        String s;
        while (true) {
            System.out.print(msg + ": ");
            s = scanner.nextLine().trim();
            if(s.isEmpty()) System.out.println("Please enter a valid prompt!");
            else break;
        }
        return s;
    }

    public int getUserIntInput(String msg) {
        int i;
        while (true) {
            try {
                i = Integer.parseInt(getUserInput(msg));
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again!");
            }
        }
        return i;
    }

    public void addStudent() {
        String name = getUserInput("Student Name");
        int age = getUserIntInput("Age");
        String email = getUserInput("E-Mail");

        try {
            String query = "INSERT INTO students(username, age, email) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, email);

            int res = statement.executeUpdate();

            if(res > 0) System.out.println("Added successfully!");
            else System.out.println("Student wasn't added...");
        } catch (SQLException e) {
            System.out.println("Couldn't add student: " + e.getMessage());
        }
    }

    public void deleteStudent() {
        int age = getUserIntInput("Student ID");
        try {
            String query = "DELETE FROM students WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, age);

            int res = statement.executeUpdate();

            if(res > 0) System.out.println("Deleted successfully!");
            else System.out.println("Student not found!");
        } catch (SQLException e) {
            System.out.println("Couldn't delete student: " + e.getMessage());
        }
    }

    public void updateStudent() {
        int id = getUserIntInput("Student ID");
        String email = getUserInput("E-Mail");

        try {
            String query = "UPDATE students SET email = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, email);
            statement.setInt(2, id);

            int res = statement.executeUpdate();

            if(res > 0) System.out.println("Updated email successfully!");
            else System.out.println("Student not found!");
        } catch (SQLException e) {
            System.out.println("Couldn't update student: " + e.getMessage());
        }
    }

    public void getStudent() {
        int id = getUserIntInput("Student ID");
        try {
            String query = "SELECT * FROM students WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                System.out.println("Name: " + res.getString("username"));
                System.out.println("Age: " + res.getInt("age"));
                System.out.println("E-Mail: " + res.getString("email"));

            } else System.out.println("No student found.");
        } catch (SQLException e) {
            System.out.println("Couldn't get student: " + e.getMessage());
        }
    }

    public void listStudents() {
        try {
            String query = "SELECT * FROM students";
            ResultSet res = connection.createStatement().executeQuery(query);
            boolean empty = true;
            while (res.next()) {
                empty = false;
                System.out.println("--- Student ---");
                System.out.println("ID: " + res.getInt("id"));
                System.out.println("Name: " + res.getString("username"));
                System.out.println("Age: " + res.getInt("age"));
                System.out.println("E-Mail: " + res.getString("email"));

            }
            if(empty) System.out.println("No results.");
        } catch (SQLException e) {
            System.out.println("Couldn't get students: " + e.getMessage());
        }
        System.out.println("------------------------");
    }
}
