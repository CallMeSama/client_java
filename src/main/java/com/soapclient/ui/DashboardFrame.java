package com.soapclient.ui;

import com.soapclient.services.UserServicePortType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {

    private UserServicePortType userServicePort;

    public DashboardFrame(UserServicePortType userServicePort) {
        this.userServicePort = userServicePort;

        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Création des composants de l'interface du tableau de bord
        JPanel panel = new JPanel(new BorderLayout());

        // Création du menu latéral (sidebar)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        JButton addUserButton = new JButton("Add User");
        addUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddUserDialog();
            }
        });

        sidebar.add(logoutButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(addUserButton);

        panel.add(sidebar, BorderLayout.WEST);

        // Liste des utilisateurs récupérés via le service SOAP
        JList<String> userList = new JList<>(fetchUserList());
        panel.add(new JScrollPane(userList), BorderLayout.CENTER);

        getContentPane().add(panel);
    }

    private String[] fetchUserList() {
        // Appel du service SOAP pour récupérer la liste des utilisateurs
        // Exemple hypothétique, ajustez selon votre implémentation réelle
        return new String[]{"User 1", "User 2", "User 3"};
    }

    private void logout() {
        // Retour à l'écran de connexion
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        dispose(); // Fermer la fenêtre du tableau de bord
    }

    private void openAddUserDialog() {
        // Dialogue pour ajouter un utilisateur
        // Vous pouvez implémenter cette fonctionnalité selon vos besoins
        JOptionPane.showMessageDialog(this, "Functionality to add user not implemented yet.",
                "Add User", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Exemple minimaliste pour montrer comment utiliser DashboardFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserServicePortType userServicePort = null; // Passer le port du service ici
                DashboardFrame dashboardFrame = new DashboardFrame(userServicePort);
                dashboardFrame.setVisible(true);
            }
        });
    }
}
