import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalDatabaseGUI extends JFrame {
    private JTable personalTable;
    private DefaultTableModel personalTableModel;
    private PersonalDatabase personalDatabase;
    private GeneralDatabase generalDatabase;

    public PersonalDatabaseGUI(PersonalDatabase personalDatabase, GeneralDatabase generalDatabase) {
        this.personalDatabase = personalDatabase;
        this.generalDatabase = generalDatabase;

        setTitle("Personal Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Updated table model to ensure correct columns
        personalTableModel = new DefaultTableModel(new Object[]{
            "Title", "Author", "Rating", "Status", "Time Spent", "Start Date", "End Date", "User Rating", "User Review"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        personalTable = new JTable(personalTableModel);

        populatePersonalTable(); // Load the personal table with existing data

        // Buttons to interact with personal database
        JButton rateBookButton = new JButton("Rate Book");
        rateBookButton.addActionListener(e -> rateBook());

        JButton writeReviewButton = new JButton("Write Review");
        writeReviewButton.addActionListener(e -> writeReview());

        JButton changeStatusButton = new JButton("Change Status");
        changeStatusButton.addActionListener(e -> changeBookStatus());

        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rateBookButton);
        buttonPanel.add(writeReviewButton);
        buttonPanel.add(changeStatusButton);

        add(new JScrollPane(personalTable), BorderLayout.CENTER); // Scroll pane for the table
        add(buttonPanel, BorderLayout.SOUTH); // Button panel at the bottom

        setVisible(true);
    }

    private void populatePersonalTable() {
        personalTableModel.setRowCount(0); // Clear existing rows

        for (PersonalBook personalBook : personalDatabase.getPersonalBooks()) {
            // Fetch the matching general book to get average rating and rating count
            GeneralBook generalBook = generalDatabase.getBooks().stream()
                .filter(book -> book.getTitle().equals(personalBook.getTitle()))
                .findFirst()
                .orElse(null);

            String ratingDisplay = "No rating";
            if (generalBook != null && generalBook.getAverageRating() >= 0) {
                ratingDisplay = String.format("%.2f (%d)", generalBook.getAverageRating(), generalBook.getRatingCount());
            }

            String reviews = personalBook.getUserReviews().isEmpty()
                ? "No reviews"
                : String.join(", ", personalBook.getUserReviews());

            double userRating = personalBook.getUserRatings().isEmpty() ? -1 : personalBook.getUserRatings().get(0);

            personalTableModel.addRow(new Object[]{
                personalBook.getTitle(),
                personalBook.getAuthor(),
                ratingDisplay,
                personalBook.getStatus(),
                personalBook.getTimeSpent(),
                personalBook.getStartDate(),
                personalBook.getEndDate(),
                userRating == -1 ? "No rating" : String.format("%.2f", userRating),
                reviews
            });
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

            generalDatabase.updateBookRating(book.getTitle(), rating); // Update the general database
            personalDatabase.saveToFile(); // Save the personal data
            populatePersonalTable(); // Refresh the table
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
            book.addUserReview(review);

            personalDatabase.saveToFile(); // Save personal data
            populatePersonalTable(); // Refresh the table
        }
    }

    private void changeBookStatus() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to change its status.");
            return;
        }

        String title = (String) personalTableModel.getValueAt(selectedRow, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        Object[] options = {"Not Started", "Ongoing", "Completed"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select a new status:",
            "Change Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            book.getStatus() // Default to current status
        );

        if (newStatus != null) {
            book.setStatus(newStatus); // Update status

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); // Date format
            String currentDate = sdf.format(new Date());

            if (newStatus.equals("Ongoing") && book.getStartDate().equals("N/A")) {
                book.setStartDate(currentDate); // Set start date if it's ongoing
            } else if (newStatus.equals("Completed") && book.getEndDate().equals("N/A")) {
                book.setEndDate(currentDate); // Set end date if it's completed
            }

            personalDatabase.saveToFile(); // Save the updated personal data
            populatePersonalTable(); // Refresh the table with new data
        }
    }
}