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
                String title = values[0]; // Assume the title is the first value
                String author = values.length > 1 ? values[1] : "Unknown"; // Default to "Unknown" if there's no author
                GeneralBook book = new GeneralBook(title, author);
                books.add(book); // Add the book to the list
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file reading errors
        }
    }

    public List<GeneralBook> getBooks() {
        return new ArrayList<>(books); // Return a copy of the list for encapsulation
    }

    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GENERAL_CSV))) {
            for (GeneralBook book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor()); // Write the book's title and author
                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
}