import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UserAdmin extends JFrame {

    private Map<String, String> userDatabase; 

    private JTextField usernameField;
    private JPasswordField passwordField;

    public UserAdmin() {
        super("Login & Registration");
      
        userDatabase = new HashMap<>();
        userDatabase.put("admin", "admin");
registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(UserAdmin.this, "Username and password cannot be empty.");
                } else if (userDatabase.containsKey(username)) {
                    JOptionPane.showMessageDialog(UserAdmin.this, "Username already exists.");
                } else {
                    userDatabase.put(username, password);
                    saveUsersToFile(); // Save new user to file
                    JOptionPane.showMessageDialog(UserAdmin.this, "Registration successful!");
                    // Automatically log in the newly registered user
                    // Open the main application window for the user
                }
            }
        });
    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // Handle file read error
            e.printStackTrace();
        }
    }
