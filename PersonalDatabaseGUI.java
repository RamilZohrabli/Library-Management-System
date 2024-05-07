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
    public PersonalDatabaseGUI(PersonalDatabase database, GeneralDatabase generalDatabase) {
        this.personalDatabase = database;
        this.generalDatabase = generalDatabase;
        setTitle("Personal Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        personalTableModel = new DefaultTableModel(new Object[]{
            "Title", "Author", "Status", "Time Spent", "User Rating", "User Review"}, 0) {
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rateBookButton);

        add(new JScrollPane(personalTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populatePersonalTable(List<PersonalBook> personalBooks) {
        personalTableModel.setRowCount(0); // Clear existing rows
        for (PersonalBook book : personalBooks) {
            double userRating = book.getUserRatings().isEmpty() ? -1 : book.getUserRatings().get(0);
            String userReview = book.getUserReviews().isEmpty() ? "No review" : book.getUserReviews().get(0);

            personalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getStatus(),
                book.getTimeSpent(),
                userRating == -1 ? "No rating" : String.format("%.2f", userRating),
                userReview
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

            book.addUserRating(rating); // Add user rating to personal book

            // Update the general database
            generalDatabase.updateBookRating(book.getTitle(), rating);

            personalDatabase.saveToFile(); // Save the personal database
            populatePersonalTable(personalDatabase.getPersonalBooks()); // Refresh the table
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid rating. Please enter a number between 1 and 5.");
        }
    }
}
