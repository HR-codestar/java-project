import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class LibraryManagementSystemGUI {
    private LibraryManagementSystem library;
    private JFrame frame;

    private JTextField bookTitleField, bookAuthorField, categoryField, subcategoryField, memberNameField, bookIdField, memberIdField, returnDateField, categoryFilterField, subcategoryFilterField;
    private JTextArea recordsArea;

    public LibraryManagementSystemGUI() {
        library = new LibraryManagementSystem();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 900, 500);


        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Add Books", createBookPanel());
        tabbedPane.addTab("Add Members", createMemberPanel());
        tabbedPane.addTab("Issue/Return Books", createLoanPanel());
        tabbedPane.addTab("View Records", createRecordsPanel());
        tabbedPane.addTab("Delete Records", createDeletePanel());
        tabbedPane.addTab("Books by Category", createBooksByCategoryPanel());
        tabbedPane.addTab("Books by Subcategory", createBooksBySubcategoryPanel());
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Desktop\\Library.jpg"); // Replace with your image path
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel titleLabel = new JLabel("Book Title: ");
        titleLabel.setForeground(new Color(234, 229, 229, 250));
        //titleLabel.setBackground(new Color(234, 229, 229, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        bookTitleField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(bookTitleField, gbc);


        JLabel authorLabel = new JLabel("Book Author: ");
        authorLabel.setForeground(new Color(234, 229, 229, 250));
        //titleLabel.setBackground(new Color(234, 229, 229, 255));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(authorLabel, gbc);

        bookAuthorField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(bookAuthorField, gbc);

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setForeground(new Color(238, 231, 231));
       // titleLabel.setBackground(new Color(234, 229, 229, 255));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(categoryLabel, gbc);

        categoryField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);


        JLabel subcategoryLabel = new JLabel("Subcategory: ");
        subcategoryLabel.setForeground(new Color(234, 229, 229, 250));
       // titleLabel.setBackground(new Color(234, 229, 229, 255));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(subcategoryLabel, gbc);

        subcategoryField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(subcategoryField, gbc);


        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBackground(new Color(238, 12, 12)); // Tomato Red
        addBookButton.setForeground(Color.WHITE);
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = bookTitleField.getText().trim();
                String author = bookAuthorField.getText().trim();
                String category = categoryField.getText().trim();
                String subcategory = subcategoryField.getText().trim();


                if (title.isEmpty() || author.isEmpty() || category.isEmpty() || subcategory.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Error: All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                library.addBook(title, author, category, subcategory);

                JOptionPane.showMessageDialog(frame, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                bookTitleField.setText("");
                bookAuthorField.setText("");
                categoryField.setText("");
                subcategoryField.setText("");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(addBookButton, gbc);

        return panel;
    }


    private JPanel createMemberPanel() {

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Desktop\\member.jpg"); // Replace with your image path
                g.drawImage(backgroundImage.getImage(), 450, 5,30, 30, this);
            }
        };
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(255, 255, 255, 255));

        JLabel nameLabel = new JLabel("Member Name: ");
        panel.add(nameLabel);
        memberNameField = new JTextField(20);
        panel.add(memberNameField);

        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.setBackground(new Color(34, 139, 34));
        addMemberButton.setForeground(Color.WHITE);
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = memberNameField.getText().trim();
                library.addMember(name);

                if (name.isEmpty() ) {
                    JOptionPane.showMessageDialog(frame, "Error: Field is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                JOptionPane.showMessageDialog(frame, "Member added successfully!","Success", JOptionPane.INFORMATION_MESSAGE);

                memberNameField.setText("");
            }
        });
        panel.add(addMemberButton);
        return panel;
    }
    private JPanel createLoanPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(173, 216, 230));

        JLabel bookIdLabel = new JLabel("Book ID: ");
        panel.add(bookIdLabel);
        bookIdField = new JTextField(5);
        panel.add(bookIdField);

        JLabel memberIdLabel = new JLabel("Member ID: ");
        panel.add(memberIdLabel);
        memberIdField = new JTextField(5);
        panel.add(memberIdField);

        JButton issueBookButton = new JButton("Issue Book");
        issueBookButton.setBackground(new Color(255, 165, 0));
        issueBookButton.setForeground(Color.WHITE);

        issueBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String bookIdText = bookIdField.getText().trim();
                    String memberIdText = memberIdField.getText().trim();


                    if (bookIdText.isEmpty() || memberIdText.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "Error: Both fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);

                    }


                    int bookId = Integer.parseInt(bookIdText);
                    int memberId = Integer.parseInt(memberIdText);


                    library.issueBook(bookId, memberId);


                    JOptionPane.showMessageDialog(frame, "Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);


                    bookIdField.setText("");
                    memberIdField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter both fields", "Input Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        panel.add(issueBookButton);


        JLabel returnDateLabel = new JLabel("Return Date (YYYY-MM-DD): ");
        panel.add(returnDateLabel);
        returnDateField = new JTextField(10);
        panel.add(returnDateField);

        JButton returnBookButton = new JButton("Return Book:");
        returnBookButton.setBackground(new Color(34, 139, 34));
        returnBookButton.setForeground(Color.WHITE);
        returnBookButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdField.getText());
                    int memberId = Integer.parseInt(memberIdField.getText());
                    LocalDate returnDate = LocalDate.parse(returnDateField.getText());
                    library.returnBook(bookId, memberId, returnDate);


                    bookIdField.setText("");
                    memberIdField.setText("");
                    returnDateField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid numbers or a valid date format (YYYY-MM-DD).","Input Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(returnBookButton);

        return panel;
    }


    private JPanel createRecordsPanel() {
        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon backgroundImage = new ImageIcon("C:\\Users\\ASUS\\OneDrive\\Desktop\\Library.jpg"); // Replace with your image path
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 228, 181));

        JButton viewRecordsButton = new JButton("View All Records");
        viewRecordsButton.setBackground(new Color(255, 69, 0));
        viewRecordsButton.setForeground(Color.WHITE);
        viewRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String records = library.viewRecords();
                    recordsArea.setText(records);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error viewing records.");
                }
            }
        });
        panel.add(viewRecordsButton, BorderLayout.NORTH);

        recordsArea = new JTextArea();
        recordsArea.setEditable(false);
        recordsArea.setBackground(Color.LIGHT_GRAY);
        panel.add(new JScrollPane(recordsArea), BorderLayout.CENTER);

        return panel;
    }


    private JPanel createDeletePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(255, 239, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel deleteBookLabel = new JLabel("Book ID to Delete: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(deleteBookLabel, gbc);

        JTextField deleteBookIdField = new JTextField(5);
        gbc.gridx = 1;
        panel.add(deleteBookIdField, gbc);

        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.setBackground(Color.RED);
        deleteBookButton.setForeground(Color.WHITE);
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(deleteBookIdField.getText());
                    library.deleteBook(bookId);

                    deleteBookIdField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Book ID.");
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(deleteBookButton, gbc);

        return panel;
    }

    private JPanel createBooksByCategoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(255, 239, 200));

        JLabel categoryLabel = new JLabel("Category: ");
        panel.add(categoryLabel);

        categoryFilterField = new JTextField(15);
        panel.add(categoryFilterField);

        JButton filterButton = new JButton("Filter Books");
        filterButton.setBackground(new Color(60, 179, 113));
        filterButton.setForeground(Color.WHITE);
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = categoryFilterField.getText().trim();
                String result = library.getBooksByCategory(category);

                if(category.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,"Field Required","Input Error",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(frame, result);
                }
            }
        });
        panel.add(filterButton);

        return panel;
    }


    private JPanel createBooksBySubcategoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(255, 239, 200)); // Light Yellow

        JLabel subcategoryLabel = new JLabel("Subcategory:" );
        panel.add(subcategoryLabel);

        subcategoryFilterField = new JTextField(15);
        panel.add(subcategoryFilterField);

        JButton filterButton = new JButton("Filter Books");
        filterButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        filterButton.setForeground(Color.WHITE);
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subcategory = subcategoryFilterField.getText().trim();
                String result = library.getBooksBySubcategory(subcategory); // Call the method

                if(subcategory.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,"Field Required","Input Error",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(frame, result);
                }
            }
        });
        panel.add(filterButton);

        return panel;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryManagementSystemGUI();
            }
        });
    }
}
