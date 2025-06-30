import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(800, 550); // Larger window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 230, 140)); // Khaki

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 25, 25, 25);

        // Title
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Username Label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(usernameField, gbc);
        gbc.gridwidth = 1;

        // Password Label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Show Password Toggle (No Text)
        showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBackground(new Color(240, 230, 140));
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(showPasswordCheckBox, gbc);

        // Toggle Logic
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 22));
        loginButton.setBackground(new Color(0, 153, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loginButton, gbc);

        add(panel);

        // Login Logic
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("admin2025")) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
                dispose();
                new BookstoreHomePage();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openHomePage() {
        JFrame homeFrame = new JFrame("Home Page");
        homeFrame.setSize(800, 550);
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setLocationRelativeTo(null);

        JPanel homePanel = new JPanel();
        homePanel.setBackground(new Color(240, 230, 140));
        homePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Admin Home Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(0, 102, 153));
        homePanel.add(welcomeLabel, BorderLayout.CENTER);

        homeFrame.add(homePanel);
        homeFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminLoginPage().setVisible(true);
        });
    }
}
