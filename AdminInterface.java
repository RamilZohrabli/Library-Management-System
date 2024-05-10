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

        // Table model initialization with uneditable cells
        tableModel = new DefaultTableModel(new Object[]{"Title", "Author"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        generalTable = new JTable(tableModel);
        generalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        generalTable.setRowSelectionAllowed(true);

        // Preventing key-based deletion or editing
        generalTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume(); // Prevent deletion using keyboard
                }
            }
        });

        populateTable(); // Populate the table with initial data

        // Buttons initialization and listeners
        addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook()); // Set listener

        deleteBookButton = new JButton("Delete Book");
        deleteBookButton.addActionListener(e -> deleteBook()); // Set listener

        editBookButton = new JButton("Edit Book");
        editBookButton.addActionListener(e -> editBook()); // Set listener

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(editBookButton);

        add(new JScrollPane(generalTable), BorderLayout.CENTER); // Table with scroll pane
        add(buttonPanel, BorderLayout.SOUTH); // Buttons panel at the bottom

        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Display the interface
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

            if (title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both title and author fields must be filled.");
                return; // Do not add empty fields
            }

            GeneralBook newBook = new GeneralBook(title, author);
            generalDatabase.addBook(newBook); // Use a new method to add and save
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

        int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete \"" + title + "\"?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);

        if (confirmDelete == JOptionPane.YES_OPTION) {
            boolean isDeleted = generalDatabase.removeBookByTitle(title); // Use a new method to remove and save
            if (isDeleted) {
                populateTable(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the book."); // Handle failure
            }
        }
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
            return; // If the book is not found, exit
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

            generalDatabase.saveToCSV(); // Save changes
            populateTable(); // Refresh the table
        }
    }
}
