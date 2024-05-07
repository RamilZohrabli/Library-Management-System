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

    public interface LoginListener {
        // Should accept two parameters: isAdmin and username
        void onLoginSuccess(boolean isAdmin, String username);
    }

    private LoginListener loginListener;

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }


    private Map<String, String> userDatabase; // Simulated user database
    private Map<String, String> adminCredentials; // Admin credentials
    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String USER_FILE = "users.txt";
    private static final Color MORNING_COLOR = new Color(255, 230, 168);
    private static final Color EVENING_COLOR = new Color(91, 91, 91);

    public LoginAndRegistrationPage() {
        super("Book Library Login");

        // Initialize user database
        userDatabase = new HashMap<>();
        loadUsersFromFile();

        // Initialize admin credentials
        adminCredentials = new HashMap<>();
        adminCredentials.put("admin", "admin");

        initUI(); // Initialize the GUI components

        // Dynamic Background Update
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBackground(); // Change background color based on time of day
            }
        }, 0, 60000); // Update every minute

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true); // Show the frame
    }
    public String getUsername() {
        return usernameField.getText(); 
    }
    public void openAdminPage() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder userInfo = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
            userInfo.append("Username: ").append(entry.getKey()).append(", Password: ").append(entry.getValue()).append("\n");
        }

        textArea.setText(userInfo.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        adminFrame.add(scrollPane);
        adminFrame.setVisible(true);
    }
    private void initUI() {
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
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(); // Handle login button click
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Verdana", Font.BOLD, 20));
        registerButton.setForeground(Color.BLACK);
        registerButton.setBackground(new Color(153, 51, 255)); // Lighter purple color
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register(); // Handle registration button click
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(MORNING_COLOR); // Initial background color

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
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(registerButton, gbc);

        add(panel); // Add the panel to the frame
    }
    
    private void login() {
        String username = usernameField.getText(); // Capture the username
        String password = String.valueOf(passwordField.getPassword());
    
        boolean isAdmin = false;
    
        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            isAdmin = true; // Admin login
        } else if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            isAdmin = false; // Regular user login
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
            return; // Exit if login fails
        }
    
        // Notify the listener upon successful login
        if (loginListener != null) {
            if (isAdmin) {
                loginListener.onLoginSuccess(true, "admin"); // Admin login, pass "admin"
            } else {
                loginListener.onLoginSuccess(false, username); // Corrected: Pass username
            }
        }
    }
    


    private void register() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
            return;
        }

        if (userDatabase.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        } else {
            userDatabase.put(username, password);
            saveUsersToFile(); // Save new user to file
            JOptionPane.showMessageDialog(this, "Registration successful!");
            // Automatically log in after registration
            login(); // Perform login after successful registration
        }
    }

    private void updateBackground() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();

        int r = (int) (MORNING_COLOR.getRed() * (1 - (hour / 24.0)) + EVENING_COLOR.getRed() * (hour / 24.0));
        int g = (int) (MORNING_COLOR.getGreen() * (1 - (hour / 24.0)) + EVENING_COLOR.getGreen() * (hour / 24.0));
        int b = (int) (MORNING_COLOR.getBlue() * (1 - (hour / 24.0)) + EVENING_COLOR.getBlue() * (hour / 24.0));

        Color newColor = new Color(r, g, b); // Gradient effect
        getContentPane().setBackground(newColor);
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine(); // Save each user
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write error
        }
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]); // Load users into the database
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read error
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginAndRegistrationPage());
    }
}