import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GeneralDatabaseGUI extends JFrame {
    private JTable table;
    private GeneralDatabase database;

    public GeneralDatabaseGUI(GeneralDatabase database) {
        this.database = database;

        setTitle("General Database");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change from EXIT_ON_CLOSE to avoid closing the main app
        setLayout(new BorderLayout());

        // Create a DefaultTableModel that is read-only
        table = new JTable(new DefaultTableModel(new Object[]{"Title", "Author", "Rating", "Reviews"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        });

        populateTable(database.getBooks());

        // Adding a mouse listener for interactions with the table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (col == 3) { // Reviews column
                    String reviews = (String) table.getValueAt(row, col);
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

        setVisible(true);
    }

    private void populateTable(List<GeneralBook> books) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows
        for (GeneralBook book : books) {
            String reviews = book.getReviews().isEmpty() ? "No reviews" : String.join(", ", book.getReviews());
            double rating = book.getAverageRating();
            String ratingStr = (rating == -1) ? "No rating" : String.format("%.2f (%d)", rating, book.getRatingCount());

            model.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                ratingStr,
                reviews
            });
        }
    }
}
