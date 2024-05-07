import java.io.*;
import java.util.*;

public class GeneralDatabase {
    private List<GeneralBook> books;
    private static final String GENERAL_CSV = "general.csv";

    public GeneralDatabase() {
        books = new ArrayList<>();
        loadFromCSV(); // Load books from the provided general.csv file
    }

    public void loadFromCSV() {
        books.clear(); // Clear any existing data
        try (BufferedReader br = new BufferedReader(new FileReader(GENERAL_CSV))) {
            String line;
            boolean isHeader = true; // Assuming the first line is a header
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header
                    continue;
                }
                String[] values = line.split(","); // CSV delimited by commas
                String title = values[0];
                String author = values.length > 1 ? values[1] : "Unknown";
                double averageRating = values.length > 2 ? parseDoubleOrDefault(values[2], -1) : -1;
                String review = values.length > 3 ? values[3] : "No Review";

                GeneralBook book = new GeneralBook(title, author);
                book.addReview(review);
                if (averageRating != -1) {
                    book.addRating(averageRating);
                }

                books.add(book); // Add the book to the list
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public List<GeneralBook> getBooks() {
        return new ArrayList<>(books); // Return a copy of the list for encapsulation
    }

    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GENERAL_CSV))) {
            for (GeneralBook book : books) {
                writer.write(
                    book.getTitle() + "," +
                    book.getAuthor() + "," +
                    (book.getAverageRating() == -1 ? "No Rating" : String.format("%.2f", book.getAverageRating())) + "," +
                    (book.getReviews().isEmpty() ? "No Review" : book.getReviews().get(0))
                ); 
                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
}
