import javax.swing.*;

public class MainApp {
    private GeneralDatabase generalDatabase;
    private PersonalDatabase personalDatabase;

    public MainApp() {
        generalDatabase = new GeneralDatabase(); // No argument required
        generalDatabase.loadFromCSV(); // Load from "general.csv"

        personalDatabase = new PersonalDatabase();

        initializeLoginPage(); // Start with the login/registration
    }

    private void initializeLoginPage() {
        LoginAndRegistrationPage loginPage = new LoginAndRegistrationPage();

        loginPage.setLoginListener(isAdmin -> {
            if (isAdmin) {
                openAdminPage(); // Admin functionality
            } else {
                String username = loginPage.getUsername(); // Get the logged-in username
                personalDatabase.setUser(username); // Set the user in PersonalDatabase
                personalDatabase.loadFromFile(); // Load personal data
                openMainInterface(); // Open the main interface for regular users
            }
        });

        loginPage.setVisible(true); // Show the login page
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
        // When admin logs in, open the admin interface
        GeneralDatabase generalDatabase = new GeneralDatabase(); // Use general.csv
        generalDatabase.loadFromCSV(); // Load from the default CSV
        new AdminInterface(generalDatabase); // Open the admin interface
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new); // Start the application
    }
}
