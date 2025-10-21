import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        String file = "resources" + File.separator + "config.properties";
        String url, user, pass;
        try(FileInputStream stream = new FileInputStream(file)) {
            properties.load(stream);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            pass = properties.getProperty("db.password");
        } catch (IOException e) {
            System.out.println("Cannot open configuration: " + e.getMessage());
            System.out.println("Cannot continue, ensure you have a config.properties file\ninside a \"resources\" folder with values: ");
            System.out.println("* db.url");
            System.out.println("* db.user");
            System.out.println("* db.password");
            return;
        }

        StudentManager manager = new StudentManager(url, user, pass);
        if(!manager.isReady) return;

        boolean run = true;
        do {
            System.out.println("0) Exit");
            System.out.println("1) Add Student");
            System.out.println("2) List Students");
            System.out.println("3) Find Student by ID");
            System.out.println("4) Update Student's E-Mail");
            System.out.println("5) Delete Student");

            switch (manager.getUserInput("Option").charAt(0)) {
                case '0' -> run = false;
                case '1' -> manager.addStudent();
                case '2' -> manager.listStudents();
                case '3' -> manager.getStudent();
                case '4' -> manager.updateStudent();
                case '5' -> manager.deleteStudent();
                default -> System.out.println("Invalid command");
            }
        } while (run);
        System.out.println("Goodbye!");
        manager.close();
    }
}
