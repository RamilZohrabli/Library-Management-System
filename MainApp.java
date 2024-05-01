import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainApp {
    private GeneralDatabase generalDatabase; // General book database
    private PersonalDatabase personalDatabase; // Personal book database
    @SuppressWarnings("unused")
    private static final String USER_FILE = "users.txt"; // Path to the user data file

    public MainApp() {
        // Initialize databases
        generalDatabase = new GeneralDatabase();
        generalDatabase.loadFromCSV("brodsky.csv"); // Load books from CSV file

        personalDatabase = new PersonalDatabase();

        // Start with the login/registration page
        initializeLoginPage();
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

        // Ensure `MainInterface` has these methods defined
        mainInterface.setGeneralDatabaseListener(() -> new GeneralDatabaseGUI(generalDatabase));
        mainInterface.setPersonalDatabaseListener(() -> new PersonalDatabaseGUI(personalDatabase));
        mainInterface.setLogoutListener(() -> {
            mainInterface.dispose(); // Close the main interface
            initializeLoginPage(); // Return to login/registration
        });

        mainInterface.setVisible(true);
    }

    private void openAdminPage() {
        // This method requires a reference to the `userDatabase`
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JTextArea userTextArea = new JTextArea();
        userTextArea.setEditable(false);
        userTextArea.setFont(new Font("Verdana", Font.PLAIN, 14));

        // Ensure `userDatabase` is defined in the context
        StringBuilder userList = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : new HashMap<String, String>().entrySet()) {
            userList.append("Username: ").append(entry.getKey())
                    .append(", Password: ").append(entry.getValue())
                    .append("\n");
        }
        
        userTextArea.setText(userList.toString());

        JScrollPane scrollPane = new JScrollPane(userTextArea);
        adminFrame.add(scrollPane);
        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
