package com.example.soapclient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementApp {
    private JFrame frame;
    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextArea resultArea;
    private UserServicePortType port;
    private String token;

    public UserManagementApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("User Management");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setBounds(10, 10, 80, 25);
        frame.getContentPane().add(lblLogin);

        loginField = new JTextField();
        loginField.setBounds(100, 10, 160, 25);
        frame.getContentPane().add(loginField);
        loginField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(10, 45, 80, 25);
        frame.getContentPane().add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 45, 160, 25);
        frame.getContentPane().add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 250, 25);
        frame.getContentPane().add(loginButton);

        resultArea = new JTextArea();
        resultArea.setBounds(10, 115, 414, 135);
        frame.getContentPane().add(resultArea);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        frame.setVisible(true);

        UserService service = new UserService();
        port = service.getUserServicePort();
    }

    private void authenticateUser() {
        String login = loginField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String token = port.authenticateUser(login, password);
            if (token != null && !token.isEmpty()) {
                this.token = token;
                resultArea.setText("Authentication successful. Token: " + token);
                if (port.isAdmin(token)) {
                    resultArea.append("\nYou have admin rights.");
                    showAdminOptions();
                } else {
                    resultArea.append("\nYou do not have admin rights.");
                }
            } else {
                resultArea.setText("Authentication failed.");
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showAdminOptions() {
        // Add components for admin options (list, add, delete, update users)
        JButton listUsersButton = new JButton("List Users");
        listUsersButton.setBounds(270, 10, 150, 25);
        frame.getContentPane().add(listUsersButton);

        listUsersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listUsers();
            }
        });

        // Implement additional buttons and functionality for add, delete, and update users
    }

    private void listUsers() {
        try {
            List<User> users = port.listUsers(token);
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append(user).append("\n");
            }
            resultArea.setText(sb.toString());
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserManagementApp window = new UserManagementApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
