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

                GeneralBook book = new GeneralBook(title, author);

                if (values.length > 2) {
                    try {
                        double rating = Double.parseDouble(values[2]);
                        if (rating != -1) {
                            book.addRating(rating); // Set the average rating
                        }
                    } catch (NumberFormatException e) {
                        // If the rating cannot be parsed, it's left as "No rating"
                    }
                }

                if (values.length > 3) {
                    for (int i = 3; i < values.length; i++) {
                        book.addReview(values[i]); // Add reviews
                    }
                }

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
                writer.write(book.getTitle() + "," + book.getAuthor());

                double rating = book.getAverageRating();
                if (rating != -1) {
                    writer.write("," + String.format("%.2f", rating)); // Write the book's rating
                } else {
                    writer.write(",No rating");
                }

                List<String> reviews = book.getReviews();
                if (!reviews.isEmpty()) {
                    for (String review : reviews) {
                        writer.write("," + review); // Write the reviews
                    }
                } else {
                    writer.write(",No reviews");
                }

                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
}
