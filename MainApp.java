import javax.swing.*;
import java.awt.*;

public class MainApp {
    private GeneralDatabase generalDatabase;
    private PersonalDatabase personalDatabase;
    private static final String CURRENT_USER_FILE = "current_user.txt";

    public MainApp() {
        generalDatabase = new GeneralDatabase();
        generalDatabase.loadFromCSV();

        personalDatabase = new PersonalDatabase();

        initializeLoginPage(); // Start with the login/registration
    }

    private void initializeLoginPage() {
        LoginAndRegistrationPage loginPage = new LoginAndRegistrationPage();

        loginPage.setLoginListener(isAdmin -> {
            String username = loginPage.getUsername(); // Get the logged-in username
            personalDatabase.setUser(username); // Set the user in PersonalDatabase
            personalDatabase.loadFromFile(); // Load personal data

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
            personalDatabase.saveToFile(); // Save personal books on logout
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

        adminFrame.add(new JScrollPane(userTextArea));
        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new); // Start the application
    }
}
