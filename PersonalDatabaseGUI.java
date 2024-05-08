import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PersonalDatabaseGUI extends JFrame {
    private JTable personalTable;
    private DefaultTableModel personalTableModel;
    private PersonalDatabase personalDatabase;
    private GeneralDatabase generalDatabase;
    private String currentUser;

    public PersonalDatabaseGUI(PersonalDatabase personalDatabase, GeneralDatabase generalDatabase) {
        this.personalDatabase = personalDatabase;
        this.generalDatabase = generalDatabase;
        this.currentUser = personalDatabase.getCurrentUser(); // Retrieve the current user's name
        setTitle("Personal Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        personalTableModel = new DefaultTableModel(new Object[]{
            "Title", "Author", "Status", "Time Spent", "Start Date", "End Date", "User Rating", "User Review"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        personalTable = new JTable(personalTableModel);
        personalTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume(); 
                }
            }
        });

        populatePersonalTable(personalDatabase.getPersonalBooks());

        JButton rateBookButton = new JButton("Rate Book");
        rateBookButton.addActionListener(e -> rateBook());

        JButton writeReviewButton = new JButton("Write Review");
        writeReviewButton.addActionListener(e -> writeReview());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rateBookButton);
        buttonPanel.add(writeReviewButton);

        add(new JScrollPane(personalTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        personalTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click to view full review
                    int row = personalTable.getSelectedRow();
                    String review = (String) personalTableModel.getValueAt(row, 7); // 7th column for review
                    if (review.contains("click to read more")) {
                        viewFullReview(review); // Open the full review
                    }
                }
            }
        });

        setVisible(true);
    }

    private void populatePersonalTable(List<PersonalBook> personalBooks) {
        personalTableModel.setRowCount(0); // Clear existing rows
        for (PersonalBook book : personalBooks) {
            double userRating = book.getUserRatings().isEmpty() ? -1 : book.getUserRatings().get(0);
            String userReview = book.getUserReviews().isEmpty() 
                ? "No review" 
                : shortenReview(book.getUserReviews().get(0)); // Display shortened review with "click to read more"

            personalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getStatus(),
                book.getTimeSpent(),
                book.getStartDate(),
                book.getEndDate(),
                userRating == -1 ? "No rating" : String.format("%.2f", userRating),
                userReview
            });
        }
    }

    private String shortenReview(String review) {
        if (review.length() > 20) { // Example: If review is more than 20 characters
            return review.substring(0, 20) + "... (click to read more)";
        } else {
            return review;
        }
    }

    private void rateBook() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to rate.");
            return;
        }

        String title = (String) personalTableModel.getValueAt(selectedRow, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        String ratingStr = JOptionPane.showInputDialog(this, "Enter your rating (1-5):");

        try {
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                JOptionPane.showMessageDialog(this, "Please enter a valid rating between 1 and 5.");
                return;
            }

            book.addUserRating(rating);

            generalDatabase.updateBookRating(book.getTitle(), rating); // Update general database
            personalDatabase.saveToFile(); // Save personal data
            populatePersonalTable(personalDatabase.getPersonalBooks()); // Refresh the table
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number between 1 and 5.");
        }
    }

    private void writeReview() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to write a review.");
            return;
        }

        String title = (String) personalTableModel.getValueAt(selectedRow, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        String review = JOptionPane.showInputDialog(this, "Write your review:");

        if (review != null && !review.trim().isEmpty()) {
            book.addUserReview(review + " - " + currentUser); // Include the username with the review

            personalDatabase.saveToFile(); // Save the personal database
            populatePersonalTable(personalDatabase.getPersonalBooks()); // Refresh the table
        }
    }

    private void viewFullReview(String review) {
        String fullReview = review.split("\\(click to read more\\)")[0]; // Extract the full review
        JOptionPane.showMessageDialog(this, fullReview, "Full Review", JOptionPane.INFORMATION_MESSAGE);
    }
}

