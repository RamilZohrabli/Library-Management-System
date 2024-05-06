import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private JButton addToPersonalLibraryButton; // Button to add a book to the personal library
    public GeneralDatabaseGUI(GeneralDatabase generalDatabase, PersonalDatabase personalDatabase, boolean isRegularUser) {
        setTitle("General Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable(new DefaultTableModel(new Object[]{"Title", "Author", "Rating", "Reviews"}, 0));
        populateTable(generalDatabase.getBooks());

        JPanel bottomPanel = new JPanel();

        // Only show the "Add to Personal Library" button if the user is not an admin
        if (isRegularUser) {
            addToPersonalLibraryButton = new JButton("Add to Personal Library");
            addToPersonalLibraryButton.addActionListener(e -> addBookToPersonalLibrary(personalDatabase));
            bottomPanel.add(addToPersonalLibraryButton);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populateTable(List<GeneralBook> books) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows

        for (GeneralBook book : books) {
            double rating = book.getAverageRating();
            String ratingStr = (rating == -1) ? "No rating" : String.format("%.2f (%d)", rating, book.getRatingCount());

            String reviews = book.getReviews().isEmpty() ? "No reviews" : String.join(", ", book.getReviews());

            model.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                ratingStr,
                reviews
            });
        }
    }

    private void addBookToPersonalLibrary(PersonalDatabase personalDatabase) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to add to your personal library.");
            return;
        }

        String title = (String) table.getValueAt(selectedRow, 0);
        String author = (String) table.getValueAt(selectedRow, 1);

        PersonalBook personalBook = new PersonalBook(title, author);
        personalDatabase.addPersonalBook(personalBook);

        JOptionPane.showMessageDialog(this, "Book added to your personal library.");
    }
}
