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
