import javax.swing.*;

import java.awt.*;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.Timer;

import java.util.TimerTask;



public class LoginPage {

    private JFrame frame;

    private JLabel clockLabel;



    // Constructor

    public LoginPage() {

        initialize();

    }
    private void initialize() {

        frame = new JFrame("Library Management System - Login");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setBounds(100, 100, 700, 500);

        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        clockLabel = new JLabel();

        clockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        clockLabel.setFont(new Font("Roboto", Font.BOLD, 20));

        topPanel.add(clockLabel, BorderLayout.EAST);



        JLabel titleLabel = new JLabel("Library Management System");

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel.setFont(new Font("Roboto", Font.BOLD, 18));

        topPanel.add(titleLabel, BorderLayout.CENTER);



        frame.add(topPanel, BorderLayout.NORTH);



        // Start live time update

        startClock();



        // Show login panel

        frame.add(createLoginPanel(), BorderLayout.CENTER);



        frame.setVisible(true);

    }



    private JPanel createLoginPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBackground(new Color(135, 155, 235)); // Sky blue



        JPanel innerPanel = new JPanel(new GridBagLayout()) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Desktop\\forlogin.jpg"); // Replace with your image path
                g.drawImage(backgroundImage.getImage(), 0, 0, 900, 500, this);
            }
        };
        innerPanel.setBorder(BorderFactory.createTitledBorder("Login"));
        //innerPanel.setBackground(new Color(176, 209, 230));


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);



        // Username and Password Fields

        JLabel usernameLabel = new JLabel("USERNAME :");
        usernameLabel.setForeground(new Color(0x000000));
        usernameLabel.setBackground(new Color(0xF7F7F7));
        usernameLabel.setOpaque(true);


        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.anchor = GridBagConstraints.EAST;

        innerPanel.add(usernameLabel, gbc);



        JTextField usernameField = new JTextField(20);

        gbc.gridx = 1;

        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(usernameField, gbc);



        JLabel passwordLabel = new JLabel("PASSWORD :");
        passwordLabel.setForeground(new Color(0x000000));
        passwordLabel.setBackground(new Color(0xF7F7F7));
        passwordLabel.setOpaque(true);

        gbc.gridx = 0;

        gbc.gridy = 1;

        gbc.anchor = GridBagConstraints.EAST;

        innerPanel.add(passwordLabel, gbc);



        JPasswordField passwordField = new JPasswordField(20);

        passwordField.setEchoChar('*');

        gbc.gridx = 1;

        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(passwordField, gbc);
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");

        showPasswordCheckBox.setBackground(new Color(0xB9DB9C));

        showPasswordCheckBox.addActionListener(e -> {

            if (showPasswordCheckBox.isSelected()) {

                passwordField.setEchoChar((char) 0);

            } else {

                passwordField.setEchoChar('*');

            }

        });



        gbc.gridx = 1;

        gbc.gridy = 2;

        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(showPasswordCheckBox, gbc);



        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(new Color(0x000000));
        roleLabel.setBackground(new Color(0xF7F7F7));
        roleLabel.setOpaque(true);

        gbc.gridx = 0;

        gbc.gridy = 3;

        gbc.anchor = GridBagConstraints.EAST;

        innerPanel.add(roleLabel, gbc);



        String[] roles = {"Student", "Teacher"};

        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        gbc.gridx = 1;

        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(roleComboBox, gbc);



        // Captcha

        JLabel captchaLabel = new JLabel("Captcha:");
        captchaLabel.setForeground(new Color(0x000000));
        captchaLabel.setBackground(new Color(0xF7F7F7));
        captchaLabel.setOpaque(true);

        gbc.gridx = 0;

        gbc.gridy = 4;

        gbc.anchor = GridBagConstraints.EAST;

        innerPanel.add(captchaLabel, gbc);



        String captcha = generateCaptcha();

        JLabel captchaTextLabel = new JLabel(captcha);
        captchaTextLabel.setForeground(new Color(0x000000));
        captchaTextLabel.setBackground(new Color(0xF7F7F7));
        captchaTextLabel.setOpaque(true);

        gbc.gridx = 1;

        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(captchaTextLabel, gbc);



        JLabel captchaInputLabel = new JLabel("Enter Captcha:");
        captchaInputLabel.setForeground(new Color(0x000000));
        captchaInputLabel.setBackground(new Color(0xF7F7F7));
        captchaInputLabel.setOpaque(true);

        gbc.gridx = 0;

        gbc.gridy = 5;

        gbc.anchor = GridBagConstraints.EAST;

        innerPanel.add(captchaInputLabel, gbc);



        JTextField captchaInputField = new JTextField(15);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(captchaInputField, gbc);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 128, 255));

        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String selectedRole = (String) roleComboBox.getSelectedItem();
            String enteredCaptcha = captchaInputField.getText();


                if (validateCredentials(username, password, selectedRole)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    openLibrarySystem(selectedRole);
                }
                    else if (username.isEmpty()||password.isEmpty()||captcha.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,"Please fill the credentials!","Input Error",JOptionPane.ERROR_MESSAGE);
                }
                else if(password != "1234"){
                    JOptionPane.showMessageDialog(frame, "INCORRECT PASSWORD!");
                }
                else if (captcha.equals(enteredCaptcha)) {
            } else {
                JOptionPane.showMessageDialog(frame, "Captcha Incorrect!");
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;

        innerPanel.add(loginButton, gbc);
        JButton refreshButton = new JButton("Refresh");

        refreshButton.setBackground(new Color(255, 113, 113));

        refreshButton.setForeground(Color.WHITE);

        refreshButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            captchaInputField.setText("");
            showPasswordCheckBox.setSelected(false);
            passwordField.setEchoChar('\u2022'); // Reset password field to bullet
            String newCaptcha = generateCaptcha();
            captchaTextLabel.setText(newCaptcha); // Update captcha

        });
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        innerPanel.add(refreshButton, gbc);
        panel.add(innerPanel, BorderLayout.CENTER);

        return panel;

    }



    private boolean validateCredentials(String username, String password, String role) {

        if (role.equals("Student")) {

            return username.trim().equalsIgnoreCase("STUDENT") && password.equals("1234");

        } else if (role.equals("Teacher")) {

            return username.trim().equalsIgnoreCase("TEACHER") && password.equals("1234");

        }

        return false;

    }



    private String generateCaptcha() {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < 6; i++) {

            int index = (int) (Math.random() * chars.length());

            captcha.append(chars.charAt(index));

        }

        return captcha.toString();

    }



    private void startClock() {

        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override

            public void run() {

                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                clockLabel.setText(now.format(formatter));

            }

        }, 0, 1000);

    }
    private void openLibrarySystem(String role) {

        SwingUtilities.invokeLater(() -> {
            new LibraryManagementSystemGUI();
        });
        frame.dispose();
    }




    private void openTeacherPage() {

        JFrame teacherFrame = new JFrame("Teacher - Library Management");

        teacherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        teacherFrame.setSize(400, 300);



        JLabel label = new JLabel("Welcome Teacher!", SwingConstants.CENTER);

        teacherFrame.add(label, BorderLayout.CENTER);



        // Add teacher-specific features like adding/removing books, etc.



        teacherFrame.setVisible(true);

    }



    public static void main(String[] args) {

        SwingUtilities.invokeLater(LoginPage::new);


    }

}
