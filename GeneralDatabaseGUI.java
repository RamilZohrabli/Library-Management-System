import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter; // Add import for MouseAdapter
import java.awt.event.MouseEvent; // Add import for MouseEvent
import java.util.List;
import java.util.ArrayList;
import java.util.Collections; // Add import for Collections
import java.util.Comparator;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

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
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        populateTable(generalDatabase.getBooks());

        JPanel bottomPanel = new JPanel();

        if (isRegularUser) {
            JButton addToPersonalLibraryButton = new JButton("Add to Personal Library");
            addToPersonalLibraryButton.addActionListener(e -> addBookToPersonalLibrary(personalDatabase));
            bottomPanel.add(addToPersonalLibraryButton);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setupTableSorting();
        setupSearchBar();

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

    private void setupTableSorting() {
        // Enable sorting by clicking on column headers
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // Initial sorting by Title
        sorter.setSortKeys(sortKeys);

        // Allow sorting by multiple columns
        sorter.setSortsOnUpdates(true);

        // Track sorting state for each column
        int[] sortingState = {0, 0, 0, 0}; // Initially all columns are in default order

        // Customize sorting behavior for certain columns (e.g., handle ratings properly)
        Comparator<Object> ratingComparator = (o1, o2) -> {
            if (o1 instanceof String && o2 instanceof String) {
                String s1 = (String) o1;
                String s2 = (String) o2;
                if (s1.equals("No rating") && s2.equals("No rating"))
                    return 0;
                else if (s1.equals("No rating"))
                    return -1;
                else if (s2.equals("No rating"))
                    return 1;
                else {
                    // Extract rating values
                    double rating1 = Double.parseDouble(s1.split("\\s+")[0]);
                    double rating2 = Double.parseDouble(s2.split("\\s+")[0]);
                    return Double.compare(rating1, rating2);
                }
            }
            return 0;
        };

        sorter.setComparator(2, ratingComparator); // Set custom comparator for the Rating column

        // Add sorting listener to each column header
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int columnIndex = table.columnAtPoint(e.getPoint());
                if (columnIndex != -1) {
                    // Toggle sorting state for the clicked column
                    sortingState[columnIndex]++;
                    if (sortingState[columnIndex] % 3 == 1) {
                        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
                    } else if (sortingState[columnIndex] % 3 == 2) {
                        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(columnIndex, SortOrder.DESCENDING)));
                    } else {
                        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.UNSORTED)));
                    }
                }
            }
        });
    }

    private void setupSearchBar() {
        // Add search functionality using a JTextField
        JTextField searchField = new JTextField();
        searchField.setToolTipText("Search");
        searchField.addActionListener(e -> {
            String text = searchField.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        add(searchField, BorderLayout.NORTH);
    }

}
