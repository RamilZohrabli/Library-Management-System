import javax.swing.*;
import java.io.*;

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
        // Check if there's a currently logged-in user
        String loggedInUser = getCurrentLoggedInUser();
        if (loggedInUser != null) {
            // Auto-login based on the stored username
            if (loggedInUser.equals("admin")) {
                openMainInterface(true); // Admin functionality
            } else {
                personalDatabase.setUser(loggedInUser);
                personalDatabase.loadFromFile(); // Load personal data
                openMainInterface(false); // Open the main interface for regular users
            }
        } else {
            // Show login/registration page
            LoginAndRegistrationPage loginPage = new LoginAndRegistrationPage();

            loginPage.setLoginListener((isAdmin, username) -> {
                saveCurrentUser(username); // Save the current logged-in user

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
    }

    private void openMainInterface(boolean isAdmin) {
        MainInterface mainInterface = new MainInterface(isAdmin);

        mainInterface.setGeneralDatabaseListener(() -> new GeneralDatabaseGUI(generalDatabase, personalDatabase, !isAdmin));
        
        if (isAdmin) {
            mainInterface.setAdminInterfaceListener(() -> new AdminInterface(generalDatabase));
        } else {
            mainInterface.setPersonalDatabaseListener(() -> new PersonalDatabaseGUI(personalDatabase));
        }
        
        mainInterface.setLogoutListener(() -> {
            logout(); // Handle logout
            initializeLoginPage(); // Return to login/registration
        });

        mainInterface.setVisible(true);
    }

    private void saveCurrentUser(String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CURRENT_USER_FILE))) {
            writer.write(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentLoggedInUser() {
        File file = new File(CURRENT_USER_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(CURRENT_USER_FILE))) {
                return reader.readLine().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void logout() {
        File file = new File(CURRENT_USER_FILE);
        if (file.exists()) {
            file.delete(); // Delete the file to log out
        }

        personalDatabase.saveToFile(); // Save personal books on logout
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new); // Start the application
    }
}