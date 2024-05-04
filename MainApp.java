import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainApp {
    private GeneralDatabase generalDatabase;
    private PersonalDatabase personalDatabase;
    private static final String PERSONAL_BOOKS_FILE = "personal_books.txt";

    public MainApp() {
        generalDatabase = new GeneralDatabase();
        generalDatabase.loadFromCSV("brodsky.csv"); // Load general database from CSV

        personalDatabase = new PersonalDatabase(); // User's personal library
        personalDatabase.loadFromFile(PERSONAL_BOOKS_FILE); // Load personal database from file

        initializeLoginPage(); // Start with login/registration

        // Save personal database on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> personalDatabase.saveToFile(PERSONAL_BOOKS_FILE)));
    }

    private void initializeLoginPage() {
        LoginAndRegistrationPage loginPage = new LoginAndRegistrationPage();

        loginPage.setLoginListener(isAdmin -> {
            if (isAdmin) {
                openAdminPage(); // Admin functionality
            } else {
                openMainInterface(); // Open the main interface for regular users
            }
        });

        loginPage.setVisible(true);
    }

    private void openMainInterface() {
        MainInterface mainInterface = new MainInterface();

        mainInterface.setGeneralDatabaseListener(() -> new GeneralDatabaseGUI(generalDatabase, personalDatabase));
        mainInterface.setPersonalDatabaseListener(() -> new PersonalDatabaseGUI(personalDatabase));
        mainInterface.setLogoutListener(() -> {
            mainInterface.dispose(); // Close the main interface
            initializeLoginPage(); // Return to login/registration
        });

        mainInterface.setVisible(true);
    }

    private void openAdminPage() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JTextArea userTextArea = new JTextArea();
        userTextArea.setEditable(false);
        userTextArea.setFont(new Font("Verdana", Font.PLAIN, 14));

        StringBuilder userList = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : new HashMap<String, String>().entrySet()) {
            userList.append("Username: ").append(entry.getKey())
                    .append(", Password: ").append(entry.getValue())
                    .append("\n");
        }

        JScrollPane scrollPane = new JScrollPane(userTextArea); 
        adminFrame.add(scrollPane);

        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
