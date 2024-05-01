import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonalDatabaseGUI extends JFrame {
    private JTable personalTable;
    private DefaultTableModel personalTableModel;

    public PersonalDatabaseGUI(PersonalDatabase database) {
        setTitle("Personal Database");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        personalTableModel = new DefaultTableModel();
        personalTableModel.addColumn("Title");
        personalTableModel.addColumn("Author");
        personalTableModel.addColumn("Status");
        personalTableModel.addColumn("Time Spent");
        personalTableModel.addColumn("Start Date");
        personalTableModel.addColumn("End Date");
        personalTableModel.addColumn("User Rating");
        personalTableModel.addColumn("User Review");

        personalTable = new JTable(personalTableModel);
        populatePersonalTable(database.getPersonalBooks()); // Fetching the personal books

        add(new JScrollPane(personalTable), BorderLayout.CENTER);
        setVisible(true);
    }

    private void populatePersonalTable(List<PersonalBook> personalBooks) {
        for (PersonalBook book : personalBooks) {
            String userRating = book.getUserRatings().isEmpty() ? "No rating" : String.format("%.2f", book.getUserRatings().get(0));
            String userReview = book.getUserReviews().isEmpty() ? "No review" : book.getUserReviews().get(0);

            personalTableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthor(),
                book.getStatus(),
                book.getTimeSpent(),
                book.getStartDate(),
                book.getEndDate(),
                userRating,
                userReview
            });
        }
    }
}
