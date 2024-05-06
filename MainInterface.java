import javax.swing.*;
import java.awt.*;

public class MainInterface extends JFrame {
    private JButton generalDatabaseButton;
    private JButton personalDatabaseButton; // Only for users
    private JButton adminInterfaceButton; // Only for admins
    private JButton logoutButton;

    public MainInterface(boolean isAdmin) {
        setTitle("Main Interface");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(new Color(255, 230, 168));

        // General Database Button
        generalDatabaseButton = new JButton("General Database");
        generalDatabaseButton.setFont(new Font("Verdana", Font.BOLD, 18));
        generalDatabaseButton.setForeground(Color.BLACK);
        generalDatabaseButton.setBackground(new Color(102, 0, 204));

        gbc.gridx = 0; // Column position
        gbc.gridy = 0; // Row position
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(generalDatabaseButton, gbc);

        if (isAdmin) {
            // Admin Interface Button
            adminInterfaceButton = new JButton("Admin Interface");
            adminInterfaceButton.setFont(new Font("Verdana", Font.BOLD, 18));
            adminInterfaceButton.setForeground(Color.BLACK);
            adminInterfaceButton.setBackground(new Color(255, 140, 0));

            gbc.gridy = 1;
            panel.add(adminInterfaceButton, gbc);
        } else {
            // Personal Database Button
            personalDatabaseButton = new JButton("Personal Database");
            personalDatabaseButton.setFont(new Font("Verdana", Font.BOLD, 18));
            personalDatabaseButton.setForeground(Color.BLACK);
            personalDatabaseButton.setBackground(new Color(153, 51, 255));

            gbc.gridy = 1;
            panel.add(personalDatabaseButton, gbc);
        }

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Verdana", Font.BOLD, 18));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setBackground(new Color(255, 0, 0)); // Red color for logout

        gbc.gridy = 2; // Position in the next row
        panel.add(logoutButton, gbc);

        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Listener setters for the buttons
    public void setGeneralDatabaseListener(Runnable listener) {
        generalDatabaseButton.addActionListener(e -> listener.run());
    }

    public void setPersonalDatabaseListener(Runnable listener) {
        if (personalDatabaseButton != null) {
            personalDatabaseButton.addActionListener(e -> listener.run());
        }
    }

    public void setAdminInterfaceListener(Runnable listener) {
        if (adminInterfaceButton != null) {
            adminInterfaceButton.addActionListener(e -> listener.run());
        }
    }

    public void setLogoutListener(Runnable listener) {
        logoutButton.addActionListener(e -> listener.run());
    }
}
