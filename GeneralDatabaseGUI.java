import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private List<GeneralBook> originalBooks;
    private Map<Integer, Integer> sortClickCount; // Track the click count for each column

    public GeneralDatabaseGUI(GeneralDatabase generalDatabase, PersonalDatabase personalDatabase, boolean isRegularUser) {
        this.originalBooks = new ArrayList<>(generalDatabase.getBooks()); // Store the original list of books
        this.sortClickCount = new HashMap<>(); // Initialize the sort click count map

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

        table.setRowSorter(sorter);

        // Custom header listener to handle cycling through sorting states
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = table.columnAtPoint(e.getPoint());
                int clickCount = sortClickCount.getOrDefault(columnIndex, 0) + 1; // Increment the click count

                if (clickCount == 1) {
                    // First click - ascending order
                    sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
                } else if (clickCount == 2) {
                    // Second click - descending order
                    sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING)));
                } else {
                    // Third click - reset to default (original) order
                    sorter.setSortKeys(null); // Reset to the original order
                    populateTable(originalBooks); // Re-populate the table with the original data
                    clickCount = 0; // Reset the click count for this column
                }

                sortClickCount.put(columnIndex, clickCount); // Update the click count map
            }
        });

        // Populate the table initially
        populateTable(originalBooks);

        // Search functionality
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

        // Check if the book already exists in the personal database
        if (personalDatabase.getPersonalBook(title) != null) {
            JOptionPane.showMessageDialog(this, "This book is already in your personal library.");
            return; // If it exists, prevent adding it again
        }

        PersonalBook personalBook = new PersonalBook(title, author);
        personalDatabase.addPersonalBook(personalBook);

        JOptionPane.showMessageDialog(this, "Book added to your personal library.");
    }
}
