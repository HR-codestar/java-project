import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class LibraryManagementSystem {
    private List<Book> books;
    private List<Member> members;
    private List<Loan> loans;

    public LibraryManagementSystem() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.loans = new ArrayList<>();
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/library";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);

    }


    public void addBook(String title, String author, String category, String subcategory) {
        // Check if any input field is empty
        if (title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty() ||
                category == null || category.trim().isEmpty() ||
                subcategory == null || subcategory.trim().isEmpty()) {
            System.out.println("Error adding book: All fields are required!");
            return;
        }

        String sql = "INSERT INTO Books (title, author, category, subcategory) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, category);
            stmt.setString(4, subcategory);


            stmt.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void addMember(String name) {
        try (Connection conn = getConnection())
        {
            if(name == null||name.trim().isEmpty())
            {
                System.out.println("Enter name please.");
                return;
            }
            String sql = "INSERT INTO Members (name) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void issueBook(int bookId, int memberId) {
        try (Connection conn = getConnection()) {
            // Check if the book is already issued
            String checkSql = "SELECT * FROM Loans WHERE book_id = ? AND return_date IS NULL";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);

            }
            String sql = "INSERT INTO Loans (book_id, member_id, loan_date, return_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                LocalDate loanDate = LocalDate.now();
                LocalDate returnDate = loanDate.plusDays(14); // Calculate return date as 14 days from loan date

                stmt.setInt(1, bookId);
                stmt.setInt(2, memberId);
                stmt.setDate(3, Date.valueOf(loanDate));
                stmt.setDate(4, Date.valueOf(returnDate));

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Book issued successfully! Return Date: " + returnDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Book already issued or not available!");
        }
    }

    public void returnBook(int bookId, int memberId, LocalDate actualReturnDate) {
        try (Connection conn = getConnection()) {

            String fetchSql = "SELECT return_date FROM Loans WHERE book_id = ? AND member_id = ?";
            LocalDate expectedReturnDate = null;

            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setInt(1, bookId);
                fetchStmt.setInt(2, memberId);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        expectedReturnDate = rs.getDate("return_date").toLocalDate();
                    } else {
                        JOptionPane.showMessageDialog(null, "No active loan found for the provided book and member.");
                        return;
                    }
                }
            }


            long daysOverdue = actualReturnDate.isAfter(expectedReturnDate)
                    ? expectedReturnDate.until(actualReturnDate).getDays()
                    : 0;
            double fine = daysOverdue * 50; // Rs. 50 per day overdue


            String updateSql = "UPDATE Loans SET return_date = ?, overdue_fine = ? WHERE book_id = ? AND member_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setDate(1, Date.valueOf(actualReturnDate));
                stmt.setDouble(2, fine);
                stmt.setInt(3, bookId);
                stmt.setInt(4, memberId);
                stmt.executeUpdate();

                if (daysOverdue > 0) {
                    JOptionPane.showMessageDialog(null, "Book returned late. Fine: Rs. " + fine);
                } else {
                    JOptionPane.showMessageDialog(null, "Book returned on time. No fine.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error returning book.");
        }
    }




    public String viewRecords() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = getConnection()) {
            sb.append("Books:\n");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Books")) {
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id"))
                            .append(", Title: ").append(rs.getString("title"))
                            .append(", Author: ").append(rs.getString("author"))
                            .append("\n");
                }
            }
            sb.append("\nMembers:\n");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Members")) {
                while (rs.next()) {
                    sb.append("ID: ").append(rs.getInt("id"))
                            .append(", Name: ").append(rs.getString("name"))
                            .append("\n");
                }
            }
            sb.append("\nLoans:\n");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM Loans")) {
                while (rs.next()) {
                    sb.append("Book ID: ").append(rs.getInt("book_id"))
                            .append(", Member ID: ").append(rs.getInt("member_id"))
                            .append(", Loan Date: ").append(rs.getDate("loan_date"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void deleteBook(int bookId) {

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1, bookId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Book deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No book found with the provided ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting book from database.");
        }
    }


    public void deleteMember(int memberId) {
        Member member = findMemberById(memberId);
        if (member != null) {
            members.remove(member);
        }
    }

    public String generateReceipt(int bookId, int memberId) {
        Book book = findBookById(bookId);
        Member member = findMemberById(memberId);
        if (book != null && member != null) {
            return "Receipt: \nBook: " + book.getTitle() + "\nMember: " + member.getName();
        }
        return "Invalid Book or Member ID.";
    }


    public String getBooksByCategory(String category) {
        String query = "SELECT * FROM books WHERE category = ?";
        StringBuilder sb = new StringBuilder();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String categoryFromDb = resultSet.getString("category");
                    String subcategoryFromDb = resultSet.getString("subcategory");

                    sb.append("Title: ").append(title)
                            .append("\nAuthor: ").append(author)
                            .append("\nCategory: ").append(categoryFromDb)
                            .append("\nSubcategory: ").append(subcategoryFromDb)
                            .append("\n\n");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sb.append("Error retrieving books from database.");
        }

        return sb.length() == 0 ? "No books available in this category." : sb.toString();
    }


    public String getBooksBySubcategory(String subcategory) {
        String query = "SELECT * FROM books WHERE subcategory = ?";
        StringBuilder sb = new StringBuilder();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, subcategory);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String subcategoryFromDb = resultSet.getString("subcategory");
                    String categoryFromDb = resultSet.getString("category");

                    sb.append("Title: ").append(title)
                            .append("\nAuthor: ").append(author)
                            .append("\nCategory: ").append(categoryFromDb)
                            .append("\nSubcategory: ").append(subcategoryFromDb)
                            .append("\n\n");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sb.append("Error retrieving books from database.");
        }

        return sb.length() == 0 ? "No books available in this subcategory." : sb.toString();
    }




    private Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        return null;
    }

    private Member findMemberById(int memberId) {
        for (Member member : members) {
            if (member.getId() == memberId) {
                return member;
            }
        }
        return null;
    }

    private boolean isBookIssued(int bookId) {
        for (Loan loan : loans) {
            if (loan.getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }

    public static class Book {
        private static int idCounter = 1;
        private int id;
        private String title;
        private String author;
        private String category;
        private String subcategory;

        public Book(String title, String author, String category, String subcategory) {
            this.id = idCounter++;
            this.title = title;
            this.author = author;
            this.category = category;
            this.subcategory = subcategory;
        }

        public int getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Title: " + title + ", Author: " + author + ", Category: " + category + ", Subcategory: " + subcategory;
        }
    }

    public static class Member {
        private static int idCounter = 1;
        private int id;
        private String name;

        public Member(String name) {
            this.id = idCounter++;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name;
        }
    }


    public static class Loan {
        private int bookId;
        private int memberId;
        private LocalDate loanDate;
        private LocalDate returnDate;
        private double overdueFine;

        public Loan(int bookId, int memberId, LocalDate loanDate) {
            this.bookId = bookId;
            this.memberId = memberId;
            this.loanDate = loanDate;
        }

        public int getBookId() {
            return bookId;
        }

        public int getMemberId() {
            return memberId;
        }

        public LocalDate getLoanDate() {
            return loanDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setOverdueFine(double fine) {
            this.overdueFine = fine;
        }

        public double getOverdueFine() {
            return overdueFine;
        }

        public long calculateOverdueDays() {
            if (returnDate != null && returnDate.isAfter(loanDate.plusDays(14))) {
                return loanDate.plusDays(14).until(returnDate).getDays();
            }
            return 0;
        }

        @Override
        public String toString() {
            return "Book ID: " + bookId +
                    ", Member ID: " + memberId +
                    ", Loan Date: " + loanDate +
                    (returnDate != null ? ", Return Date: " + returnDate : ", Not Returned Yet") +
                    (overdueFine > 0 ? ", Fine: Rs. " + overdueFine : "");
        }
    }
}