import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public GeneralDatabaseGUI(GeneralDatabase generalDatabase, PersonalDatabase personalDatabase, boolean isRegularUser) {
        setTitle("General Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Rating", "Reviews"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        populateTable(generalDatabase.getBooks());

        JPanel bottomPanel = new JPanel();

        if (isRegularUser) {
            JButton addToPersonalLibraryButton = new JButton("Add to Personal Library");
            addToPersonalLibraryButton.addActionListener(e -> addBookToPersonalLibrary(personalDatabase));
            bottomPanel.add(addToPersonalLibraryButton);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void populateTable(List<GeneralBook> books) {
        tableModel.setRowCount(0);

        for (GeneralBook book : books) {
            double rating = book.getAverageRating();
            int ratingCount = book.getRatingCount();

            String ratingDisplay = rating == -1
                ? "No rating"
                : String.format("%.2f (%d)", rating, ratingCount); // Format with rating count

            String reviews = book.getReviews().isEmpty()
                ? "No reviews"
                : String.join(", ", book.getReviews());

            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                ratingDisplay,
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

        String title = (String) tableModel.getValueAt(selectedRow, 0);
        String author = (String) tableModel.getValueAt(selectedRow, 1);

        PersonalBook personalBook = new PersonalBook(title, author);
        personalDatabase.addPersonalBook(personalBook);

        JOptionPane.showMessageDialog(this, "Book added to your personal library.");
    }
}
