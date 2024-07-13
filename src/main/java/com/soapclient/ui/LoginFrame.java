package com.soapclient.ui;

import com.soapclient.services.AuthenticateUserRequest;
import com.soapclient.services.AuthenticateUserResponse;
import com.soapclient.services.UserService;
import com.soapclient.services.UserServicePortType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private UserServicePortType userServicePort;

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Authenticate user using SOAP service
                boolean authenticated = authenticateUser(username, password);

                if (authenticated) {
                    // Open DashboardFrame if authenticated
                    openDashboardFrame();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Authentication failed. Please check your username and password.",
                            "Authentication Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(loginButton);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private boolean authenticateUser(String username, String password) {
        try {
            UserService userService = new UserService();
            userServicePort = userService.getUserServicePort();

            AuthenticateUserRequest request = new AuthenticateUserRequest();
            request.setUsername(username);
            request.setPassword(password);

            // Call SOAP service to authenticate user
            AuthenticateUserResponse response = userServicePort.authenticateUser(request);

            // Check if authentication was successful based on the response status
            return response.isStatus();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openDashboardFrame() {
        DashboardFrame dashboardFrame = new DashboardFrame(userServicePort);
        dashboardFrame.setVisible(true);
        setVisible(false); // Hide the login frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
