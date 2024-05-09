import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter; // Sorter for sorting and filtering
    private JTextField searchField;

    public GeneralDatabaseGUI(GeneralDatabase generalDatabase, PersonalDatabase personalDatabase, boolean isRegularUser) {
        setTitle("General Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Rating", "Reviews"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Cells are not editable
            }
        };

        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter); // Set the sorter for the table
        
        // Populate the table initially
        populateTable(generalDatabase.getBooks());
        
        // Search bar implementation
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = searchField.getText().toLowerCase();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null); // No filter
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(text)); // Filter by text
                }
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel bottomPanel = new JPanel();

        if (isRegularUser) {
            JButton addToPersonalLibraryButton = new JButton("Add to Personal Library");
            addToPersonalLibraryButton.addActionListener(e -> addBookToPersonalLibrary(personalDatabase));
            bottomPanel.add(addToPersonalLibraryButton);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH); // Add the search bar at the top
        add(bottomPanel, BorderLayout.SOUTH); // Add buttons at the bottom

        setVisible(true);
    }

    private void populateTable(List<GeneralBook> books) {
        tableModel.setRowCount(0); // Clear existing rows

        for (GeneralBook book : books) {
            double rating = book.getAverageRating();
            int ratingCount = book.getRatingCount();

            String ratingDisplay = rating == -1
                ? "No rating"
                : String.format("%.2f (%d)", rating, ratingCount);

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

        int modelRowIndex = table.convertRowIndexToModel(selectedRow); // Correct index after sorting/filtering

        String title = (String) tableModel.getValueAt(modelRowIndex, 0);
        String author = (String) tableModel.getValueAt(modelRowIndex, 1);

        PersonalBook personalBook = new PersonalBook(title, author);
        personalDatabase.addPersonalBook(personalBook);

        JOptionPane.showMessageDialog(this, "Book added to your personal library.");
    }
}
