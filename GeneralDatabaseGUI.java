import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private GeneralDatabase database;

    public GeneralDatabaseGUI(GeneralDatabase database) {
        this.database = database;

        setTitle("General Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change from EXIT_ON_CLOSE to avoid closing the main app
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Rating", "Reviews"}, 0);

        table = new JTable(tableModel);
        populateTable(database.getBooks());

        // Adding a mouse listener for interactions with the table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 3) { // Reviews column
                    String reviews = (String) tableModel.getValueAt(row, col);
                    if (!reviews.equals("No reviews")) {
                        JOptionPane.showMessageDialog(
                            GeneralDatabaseGUI.this,
                            "Reviews: " + reviews,
                            "Review Details",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Adding a button panel to allow interaction with the general database
        JPanel buttonPanel = new JPanel();
        JButton addBookButton = new JButton("Add to Personal Library");

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookTitle = (String) tableModel.getValueAt(selectedRow, 0);
                    // Trigger an event to add the selected book to the personal library
                    JOptionPane.showMessageDialog(
                        GeneralDatabaseGUI.this,
                        "Add to Personal Library: " + bookTitle,
                        "Action",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        GeneralDatabaseGUI.this,
                        "No book selected",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        buttonPanel.add(addBookButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTable getTable() {
        return table;
    }

    public GeneralDatabase getGeneralDatabase() {
        return database;
    }

    private void populateTable(List<GeneralBook> books) {
        tableModel.setRowCount(0); // Clear existing rows
        for (GeneralBook book : books) {
            String reviews = book.getReviews().isEmpty() ? "No reviews" : String.join(", ", book.getReviews());
            double rating = book.getAverageRating();
            String ratingStr = (rating == -1) ? "No rating" : String.format("%.2f (%d)", rating, book.getRatingCount());

            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                ratingStr,
                reviews
            });
        }
    }
}
