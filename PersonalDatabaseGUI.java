import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

public class PersonalDatabaseGUI extends JFrame {
    private JTable personalTable;
    private DefaultTableModel personalTableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private PersonalDatabase personalDatabase;
    private GeneralDatabase generalDatabase;
    private Timer readingTimer;
    private String currentUsername; // To track the current user's name

    public PersonalDatabaseGUI(PersonalDatabase personalDatabase, GeneralDatabase generalDatabase, String currentUsername) {
        this.personalDatabase = personalDatabase;
        this.generalDatabase = generalDatabase;
        this.currentUsername = currentUsername; // Get the current user's name

        setTitle("Personal Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        personalTableModel = new DefaultTableModel(new Object[]{
            "Title", "Author", "Rating", "Status", "Time Spent (min)", "Start Date", "End Date", "User Rating", "User Review"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing directly in the table
            }
        };

        personalTable = new JTable(personalTableModel);
        sorter = new TableRowSorter<>(personalTableModel); // For sorting and filtering
        personalTable.setRowSorter(sorter);

        populatePersonalTable(); // Load personal data into the table

        // Search functionality
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().toLowerCase();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null); // Clear filter
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(text)); // Apply filter
                }
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        // Button panel for interactions
        JButton rateBookButton = new JButton("Rate Book");
        rateBookButton.addActionListener(e -> rateBook());

        JButton writeReviewButton = new JButton("Write Review");
        writeReviewButton.addActionListener(e -> writeReview());

        JButton changeStatusButton = new JButton("Change Status");
        changeStatusButton.addActionListener(e -> changeBookStatus());

        JButton deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(e -> deleteBook());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rateBookButton);
        buttonPanel.add(writeReviewButton);
        buttonPanel.add(changeStatusButton);
        buttonPanel.add(deleteBookButton);

        add(new JScrollPane(personalTable), BorderLayout.CENTER); // Table with scroll pane
        add(searchPanel, BorderLayout.NORTH); // Search bar at the top
        add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        setVisible(true); // Display the interface
    }

    // Method to populate the personal table with data
    private void populatePersonalTable() {
        personalTableModel.setRowCount(0); // Clear existing rows

        for (PersonalBook book : personalDatabase.getPersonalBooks()) {
            personalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getAverageRating() == -1 ? "No rating" : String.format("%.2f", book.getAverageRating()),
                book.getStatus(),
                book.getTimeSpent(),
                book.getStartDate(),
                book.getEndDate(),
                book.getUserRatings().isEmpty() ? "No rating" : String.format("%.2f", book.getUserRatings().get(0)),
                book.getUserReviews().isEmpty() ? "No reviews" : String.join(", ", book.getUserReviews())
            });
        }
    }

    // Method to start the reading timer
    private void startReadingTimer(PersonalBook book) {
        if (readingTimer != null) {
            readingTimer.cancel(); // Cancel any existing timer
        }

        readingTimer = new Timer();
        readingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                book.addTimeSpent(1); // Increment time spent by 1 minute
                personalDatabase.saveToFile(); // Save to the personal database
                populatePersonalTable(); // Refresh the table
            }
        }, 60000, 60000); // Schedule every minute
    }

    // Method to stop the reading timer
    private void stopReadingTimer() {
        if (readingTimer != null) {
            readingTimer.cancel(); // Cancel the timer
            readingTimer = null; // Clear the reference
        }
    }

    // Method to change the book status
    private void changeBookStatus() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to change its status.");
            return;
        }

        int modelRowIndex = personalTable.convertRowIndexToModel(selectedRow); // Adjust for sorting

        String title = (String) personalTableModel.getValueAt(modelRowIndex, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        Object[] statusOptions = {"Not Started", "Ongoing", "Completed"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select a new status:",
            "Change Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            book.getStatus() // Default to current status
        );

        if (newStatus != null) {
            book.setStatus(newStatus);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            String currentDate = sdf.format(new Date());

            if (newStatus.equals("Ongoing")) {
                if (book.getStartDate().equals("N/A")) {
                    book.setStartDate(currentDate); // Set the start date if not already set
                }

                startReadingTimer(book); // Start the reading timer
            } else {
                stopReadingTimer(); // Stop the reading timer
                if (newStatus.equals("Completed")) {
                    book.setEndDate(currentDate); // Set the end date
                }
            }

            personalDatabase.saveToFile(); // Save changes to the personal database
            populatePersonalTable(); // Refresh the table
        }
    }

    private void rateBook() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to rate.");
            return;
        }

        int modelRowIndex = personalTable.convertRowIndexToModel(selectedRow); // Adjust for sorting

        String title = (String) personalTableModel.getValueAt(modelRowIndex, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        String ratingStr = JOptionPane.showInputDialog(this, "Enter your rating (1-5):");

        try {
            double rating = Double.parseDouble(ratingStr);
            if (rating < 1 || rating > 5) {
                JOptionPane.showMessageDialog(this, "Please enter a valid rating between 1 and 5.");
                return;
            }

            book.addUserRating(rating); // Add user rating
            generalDatabase.updateBookRating(book.getTitle(), rating); // Update the general database
            personalDatabase.saveToFile(); // Save to personal database
            populatePersonalTable(); // Refresh the table
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a valid number.");
        }
    }

    private void writeReview() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to write a review.");
            return;
        }

        int modelRowIndex = personalTable.convertRowIndexToModel(selectedRow);

        String title = (String) personalTableModel.getValueAt(modelRowIndex, 0);
        PersonalBook book = personalDatabase.getPersonalBook(title);

        String reviewText = JOptionPane.showInputDialog(this, "Write your review:");

        if (reviewText != null && !reviewText.trim().isEmpty()) {
            // Prepend the current username to the review text
            String fullReview = currentUsername + ": " + reviewText;

            book.addUserReview(fullReview); // Add user review
            generalDatabase.addReviewToGeneralBook(title, fullReview); // Update the general database
            personalDatabase.saveToFile(); 
            populatePersonalTable(); // Refresh the table
        }
    }

    private void deleteBook() {
        int selectedRow = personalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int modelRowIndex = personalTable.convertRowIndexToModel(selectedRow);

        String title = (String) personalTableModel.getValueAt(modelRowIndex, 0);

        int confirmDelete = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete \"" + title + "\" from your personal database?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmDelete == JOptionPane.YES_OPTION) {
            personalDatabase.deletePersonalBook(title); // Delete from the personal database
            personalDatabase.saveToFile(); // Save changes
            populatePersonalTable(); // Refresh the table to reflect the deletion
        }
    }
}