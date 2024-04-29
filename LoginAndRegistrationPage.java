import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginAndRegistrationPage extends JFrame {

    private Map<String, String> userDatabase; // Simulated user database
    private Map<String, String> adminCredentials; // Admin credentials

    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String USER_FILE = "users.txt";
    private static final Color MORNING_COLOR = new Color(255, 230, 168); // Dawn color
    private static final Color EVENING_COLOR = new Color(91, 91, 91); // Dusk color

    public LoginAndRegistrationPage() {
        super("Book Library Login");

        // Initialize user database
        userDatabase = new HashMap<>();
        // Load existing users from file
        loadUsersFromFile();

        // Initialize admin credentials
        adminCredentials = new HashMap<>();
        adminCredentials.put("admin", "admin");

        // Components
        JLabel titleLabel = new JLabel("Book Library Login");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 36));
        titleLabel.setForeground(new Color(102, 0, 204)); // Purple color

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        usernameLabel.setForeground(new Color(102, 0, 204)); // Purple color

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Verdana", Font.PLAIN, 18));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(102, 0, 204)); // Purple color

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Verdana", Font.PLAIN, 18));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.BOLD, 20));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(new Color(102, 0, 204)); // Purple color

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Verdana", Font.BOLD, 20));
        registerButton.setForeground(Color.BLACK);
        registerButton.setBackground(new Color(153, 51, 255)); // Lighter purple color

        // Layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(MORNING_COLOR); // Initial color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        panel.add(registerButton, gbc);

        // Actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        // Dynamic Background Update
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBackground();
            }
        }, 0, 60000); // Update every minute

        // Frame setup
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            openAdminPage();
        } else if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "User login successful!");
            // Provide regular user functionality
        } else {
            JOptionPane.showMessageDialog(LoginAndRegistrationPage.this, "Invalid username or password.");
        }
    }

    private void register() {
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

    private void openAdminPage() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea userTextArea = new JTextArea();
        userTextArea.setEditable(false);
        userTextArea.setFont(new Font("Verdana", Font.PLAIN, 14));

        StringBuilder userList = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
            userList.append("Username: ").append(entry.getKey()).append(", Password: ").append(entry.getValue()).append("\n");
        }
        userTextArea.setText(userList.toString());

        JScrollPane scrollPane = new JScrollPane(userTextArea);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        adminFrame.add(adminPanel);
        adminFrame.setVisible(true);
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

    private void updateBackground() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();

        // Calculate gradient color
        int r = (int) (MORNING_COLOR.getRed() * (1 - (hour / 24.0)) + EVENING_COLOR.getRed() * (hour / 24.0));
        int g = (int) (MORNING_COLOR.getGreen() * (1 - (hour / 24.0)) + EVENING_COLOR.getGreen() * (hour / 24.0));
        int b = (int) (MORNING_COLOR.getBlue() * (1 - (hour / 24.0)) + EVENING_COLOR.getBlue() * (hour / 24.0));

        Color newColor = new Color(r, g, b);
        getContentPane().setBackground(newColor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginAndRegistrationPage();
            }
        });
    }
}
