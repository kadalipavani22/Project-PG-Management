import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import javax.swing.*;

public class LoginRegisterSystem extends JFrame {
    private JTextField regNameField, regEmailField, loginEmailField;
    private JPasswordField regPasswordField, regConfirmPasswordField, loginPasswordField;
    private JButton registerButton, loginButton;
    private JTabbedPane tabbedPane;

    public LoginRegisterSystem() {
        setTitle("PG Management - Login & Register");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
 
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Login", createLoginPanel());
        tabbedPane.addTab("Register", createRegisterPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 20);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(labelFont);
        regNameField = new JTextField(20);
        regNameField.setFont(fieldFont);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        regEmailField = new JTextField(20);
        regEmailField.setFont(fieldFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        regPasswordField = new JPasswordField(20);
        regPasswordField.setFont(fieldFont);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        regConfirmPasswordField = new JPasswordField(20);
        regConfirmPasswordField.setFont(fieldFont);

        registerButton = new JButton("Register");
        registerButton.setFont(labelFont);
        registerButton.addActionListener(e -> registerUser());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; panel.add(regNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(emailLabel, gbc);
        gbc.gridx = 1; panel.add(regEmailField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(regPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; panel.add(regConfirmPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 20);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        loginEmailField = new JTextField(20);
        loginEmailField.setFont(fieldFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        loginPasswordField = new JPasswordField(20);
        loginPasswordField.setFont(fieldFont);

        loginButton = new JButton("Login");
        loginButton.setFont(labelFont);
        loginButton.addActionListener(e -> loginUser());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(emailLabel, gbc);
        gbc.gridx = 1; panel.add(loginEmailField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passwordLabel, gbc);
        gbc.gridx = 1; panel.add(loginPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(loginButton, gbc);

        return panel;
    }

    private void registerUser() {
        String name = regNameField.getText();
        String email = regEmailField.getText();
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(email + "," + password + "," + name);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            tabbedPane.setSelectedIndex(0);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving user data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginUser() {
        String email = loginEmailField.getText();
        String password = new String(loginPasswordField.getPassword());

        try {
            List<String> lines = Files.readAllLines(Paths.get("users.txt"));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(email) && parts[1].equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();
                    
                    // **Pass the email and password correctly**
                    new PGManagementHome(email, password);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginRegisterSystem::new);
    }
}
