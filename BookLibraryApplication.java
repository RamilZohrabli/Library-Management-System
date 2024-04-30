import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.Timer;

public class BookLibraryApplication extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Map<String, String> userDatabase;
    private Map<String, String> adminCredentials;
    private GeneralDatabase generalDatabase;

    private static final String USER_FILE = "users.txt";
    private static final Color MORNING_COLOR = new Color(255, 230, 168);
    private static final Color EVENING_COLOR = new Color(91, 91, 91);

    public BookLibraryApplication() {
        userDatabase = new HashMap<>();
        loadUsersFromFile();
        adminCredentials = new HashMap<>();
        adminCredentials.put("admin", "admin");

        generalDatabase = new GeneralDatabase();
        generalDatabase.loadFromCSV("brodsky.csv");

        initLoginPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBackground();
            }
        }, 0, 60000);
    }

    private void initLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Book Library Login");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameField = new JTextField(15);
        JLabel usernameLabel = new JLabel("Username:");

        passwordField = new JPasswordField(15);
        JLabel passwordLabel = new JLabel("Password:");

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());

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
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        panel.add(registerButton, gbc);

        panel.setBackground(MORNING_COLOR);
        getContentPane().add(panel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password)) {
            openAdminPanel();
        } else if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            openGeneralDatabasePage();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    private void register() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
        } else if (userDatabase.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists.");
        } else {
            userDatabase.put(username, password);
            saveUsersToFile();
            JOptionPane.showMessageDialog(this, "Registration successful!");
        }
    }

    private void openAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLocationRelativeTo(null);

        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea userTextArea = new JTextArea();
        userTextArea.setFont(new Font("Verdana", Font.PLAIN, 14));

        StringBuilder userList = new StringBuilder("Registered Users:\n");
        for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
            userList.append("Username: ").append(entry.getKey()).append("\n");
        }

        userTextArea.setText(userList.toString());
        JScrollPane scrollPane = new JScrollPane(userTextArea);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        adminFrame.add(adminPanel);
        adminFrame.setVisible(true);
    }

    private void openGeneralDatabasePage() {
        JFrame generalDatabaseFrame = new JFrame("General Book Database");
        generalDatabaseFrame.setSize(800, 600);
        generalDatabaseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        generalDatabaseFrame.setLocationRelativeTo(null);

        GeneralDatabaseGUI databaseGUI = new GeneralDatabaseGUI(generalDatabase);
        generalDatabaseFrame.add(databaseGUI, BorderLayout.CENTER);

        generalDatabaseFrame.setVisible(true);
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
            e.printStackTrace();
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            userDatabase.forEach((key, value) -> {
                try {
                    writer.write(key + "," + value);
                    writer.newLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void updateBackground() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
    
        double ratio = hour / 24.0;
        int r = (int) (MORNING_COLOR.getRed() * (1 - ratio) + EVENING_COLOR.getRed() * ratio);
        int g = (int) (MORNING_COLOR.getGreen() * (1 - ratio) + EVENING_COLOR.getGreen() * ratio);
        int b = (int) (MORNING_COLOR.getBlue() * (1 - ratio) + EVENING_COLOR.getBlue() * ratio);
    
        Color newColor = new Color(r, g, b);
        getContentPane().setBackground(newColor);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookLibraryApplication());
    }
}
