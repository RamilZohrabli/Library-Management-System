import javax.swing.*;

public class MainApp {
    private GeneralDatabase generalDatabase;
    private PersonalDatabase personalDatabase;

    public MainApp() {
        generalDatabase = new GeneralDatabase();
        generalDatabase.loadFromCSV();

        personalDatabase = new PersonalDatabase();

        initializeLoginPage(); // Start with the login/registration
    }

    private void initializeLoginPage() {
        LoginAndRegistrationPage loginPage = new LoginAndRegistrationPage();
    
        loginPage.setLoginListener((isAdmin, username) -> {
            if (isAdmin) {
                openMainInterface(true); // Admin functionality
            } else {
                personalDatabase.setUser(username);
                personalDatabase.loadFromFile(); // Load personal data
                openMainInterface(false); // Open the main interface for regular users
            }
        });
        loginPage.setVisible(true);
    }
    
    private void openMainInterface(boolean isAdmin) {
        MainInterface mainInterface = new MainInterface(isAdmin);

        // Update to pass the isAdmin flag to GeneralDatabaseGUI
        mainInterface.setGeneralDatabaseListener(() -> new GeneralDatabaseGUI(generalDatabase, personalDatabase, !isAdmin));
        
        if (isAdmin) {
            mainInterface.setAdminInterfaceListener(() -> new AdminInterface(generalDatabase));
        } else {
            mainInterface.setPersonalDatabaseListener(() -> new PersonalDatabaseGUI(personalDatabase));
        }
        
        mainInterface.setLogoutListener(() -> {
            if (!isAdmin) {
                personalDatabase.saveToFile(); // Save personal books on logout
            }
            mainInterface.dispose(); // Close the main interface
            initializeLoginPage(); // Return to login/registration
        });

        mainInterface.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new); // Start the application
    }
}
