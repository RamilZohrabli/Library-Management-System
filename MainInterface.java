import javax.swing.*;
import java.awt.*;

public class MainInterface extends JFrame {
    private JButton generalDatabaseButton;
    private JButton personalDatabaseButton;
    private JButton logoutButton;

    public MainInterface() {
        setTitle("Main Interface");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout configuration
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Consistent padding
        panel.setBackground(new Color(255, 230, 168)); // Background color

        // General Database Button
        generalDatabaseButton = new JButton("General Database");
        generalDatabaseButton.setFont(new Font("Verdana", Font.BOLD, 18));
        generalDatabaseButton.setForeground(Color.BLACK);
        generalDatabaseButton.setBackground(new Color(102, 0, 204)); // Purple color

        gbc.gridx = 0; // Column position
        gbc.gridy = 0; // Row position
        gbc.fill = GridBagConstraints.BOTH; // Fill the cell
        panel.add(generalDatabaseButton, gbc);

        // Personal Database Button
        personalDatabaseButton = new JButton("Personal Database");
        personalDatabaseButton.setFont(new Font("Verdana", Font.BOLD, 18));
        personalDatabaseButton.setForeground(Color.BLACK);
        personalDatabaseButton.setBackground(new Color(153, 51, 255)); // Lighter purple color

        gbc.gridy = 1; // Move to the next row
        panel.add(personalDatabaseButton, gbc);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Verdana", Font.BOLD, 18));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBackground(new Color(255, 0, 0)); // Red color for logout

        gbc.gridy = 2; // Move to the next row
        panel.add(logoutButton, gbc);

        // Add panel to the frame
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true); // Show the frame
    }

    // Listener setters for the buttons
    public void setGeneralDatabaseListener(Runnable listener) {
        generalDatabaseButton.addActionListener(e -> listener.run());
    }

    public void setPersonalDatabaseListener(Runnable listener) {
        personalDatabaseButton.addActionListener(e -> listener.run());
    }

    public void setLogoutListener(Runnable listener) {
        logoutButton.addActionListener(e -> listener.run());
    }
}
