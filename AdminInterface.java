import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class AdminInterface extends JFrame {
    private JButton addBookButton;
    private JButton deleteBookButton;
    private JButton editBookButton;
    private JTable generalTable;
    private DefaultTableModel tableModel;
    private GeneralDatabase generalDatabase;

    public AdminInterface(GeneralDatabase generalDatabase) {
        this.generalDatabase = generalDatabase;
        setTitle("Admin Interface");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Title", "Author"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable cell editing
            }
        };

        generalTable = new JTable(tableModel);
        generalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single row selection
        generalTable.setRowSelectionAllowed(true);

        // Prevent key-based deletion or editing
        generalTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume(); // Prevent Backspace and Delete keys from doing anything
                }
            }
        });

        populateTable(); // Populate the initial data

        addBookButton = new JButton("Add Book");
        deleteBookButton = new JButton("Delete Book");
        editBookButton = new JButton("Edit Book");

        addBookButton.addActionListener(e -> addBook());
        deleteBookButton.addActionListener(e -> deleteBook());
        editBookButton.addActionListener(e -> editBook());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(editBookButton);

        add(new JScrollPane(generalTable), BorderLayout.CENTER); // Add the table with scroll pane
        add(buttonPanel, BorderLayout.SOUTH); // Add the buttons to the bottom

        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (GeneralBook book : generalDatabase.getBooks()) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor()
            });
        }
    }

    private void addBook() {
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();

            GeneralBook newBook = new GeneralBook(title, author);
            generalDatabase.getBooks().add(newBook); // Add to the general database
            generalDatabase.saveToCSV(); // Save the new book to general.csv
            populateTable(); // Refresh the table
        }
    }

    private void deleteBook() {
        int selectedRow = generalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        String title = (String) tableModel.getValueAt(selectedRow, 0);
        generalDatabase.getBooks().removeIf(book -> book.getTitle().equals(title)); // Remove from the general database
        generalDatabase.saveToCSV(); // Save the change to general.csv
        populateTable(); // Refresh the table
    }

    private void editBook() {
        int selectedRow = generalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.");
            return;
        }

        String originalTitle = (String) generalTable.getValueAt(selectedRow, 0);
        GeneralBook bookToEdit = generalDatabase.getBooks()
            .stream()
            .filter(book -> book.getTitle().equals(originalTitle))
            .findFirst()
            .orElse(null);

        if (bookToEdit == null) {
            JOptionPane.showMessageDialog(this, "Book not found.");
            return;
        }

        JTextField titleField = new JTextField(bookToEdit.getTitle(), 15);
        JTextField authorField = new JTextField(bookToEdit.getAuthor(), 15);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            bookToEdit.setTitle(titleField.getText());
            bookToEdit.setAuthor(authorField.getText());

            generalDatabase.saveToCSV(); // Save changes to general.csv
            populateTable(); // Refresh the table
        }
    }
}