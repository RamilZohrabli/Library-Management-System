import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginAndRegistrationPage extends JFrame {

    private Map<String, String> userDatabase; // Simulated user database
    private Map<String, String> adminCredentials; // Admin credentials

    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String USER_FILE = "users.txt";

    public LoginAndRegistrationPage() {
        super("Login & Registration");

        // Initialize user database
        userDatabase = new HashMap<>();
        // Load existing users from file
        loadUsersFromFile();

        // Initialize admin credentials
        adminCredentials = new HashMap<>();
        adminCredentials.put("admin", "admin");

        // Components
        JLabel titleLabel = new JLabel("Login or Register");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton viewUsersButton = new JButton("View Users");

        // Layout
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel);
        panel.add(new JLabel());
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(viewUsersButton);

        // Actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Admin login successful!");
                    // Provide admin functionality
                } else if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "User login successful!");
                    // Provide regular user functionality
                } else {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Invalid username or password.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Username and password cannot be empty.");
                } else if (userDatabase.containsKey(username)) {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Username already exists.");
                } else {
                    userDatabase.put(username, password);
                    saveUsersToFile(); // Save new user to file
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Registration successful!");
                    // Automatically log in the newly registered user
                    // Open the main application window for the user
                }
            }
        });

        viewUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdminLoggedIn()) {
                    displayUserList();
                } else {
                    JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "You need admin privileges to view users.");
                }
            }
        });

        // Frame setup
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginAndRegistrationPage();
            }
        });
    }

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

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            // Handle file write error
            e.printStackTrace();
        }
    }

    private void displayUserList() {
        StringBuilder userList = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
            userList.append(entry.getKey()).append("\n");
        }
        JTextArea userListArea = new JTextArea(userList.toString());
        JScrollPane scrollPane = new JScrollPane(userListArea);
        JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, scrollPane, "Registered Users", JOptionPane.PLAIN_MESSAGE);
    }

    private boolean isAdminLoggedIn() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        return adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password);
    }
}
