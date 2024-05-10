import java.io.*;
import java.util.*;

public class GeneralDatabase {
    private List<GeneralBook> books;
    private static final String GENERAL_CSV = "general.csv";

    public GeneralDatabase() {
        books = new ArrayList<>();
        loadFromCSV(); // Load initial books from CSV
    }

    public List<GeneralBook> getBooks() {
        return new ArrayList<>(books); // Return a copy for encapsulation
    }

    public void addBook(GeneralBook book) {
        books.add(book); // Add the book to the list
        saveToCSV(); // Save the updated list to the CSV file
    }

    public boolean removeBookByTitle(String title) {
        boolean removed = books.removeIf(book -> book.getTitle().equalsIgnoreCase(title)); // Case-insensitive removal
        if (removed) {
            saveToCSV(); // Save changes if removal is successful
        }
        return removed; // Return whether removal was successful
    }

    public void updateBookRating(String title, double rating) {
        for (GeneralBook book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) { // Find the book by title
                book.addRating(rating); // Update the rating
                saveToCSV(); // Save the updated list to the CSV file
                break; // Once found, we can exit the loop
            }
        }
    }

    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GENERAL_CSV))) {
            writer.write("Title,Author,Average Rating,Rating Count,Reviews"); // CSV header
            writer.newLine(); // New line after the header

            for (GeneralBook book : books) {
                double averageRating = book.getAverageRating();
                int ratingCount = book.getRatingCount();

                String ratingDisplay = averageRating < 0 
                    ? "No rating" 
                    : String.format("%.2f", averageRating); // Format average rating

                String reviewsDisplay = book.getReviews().isEmpty() 
                    ? "No reviews" 
                    : String.join(", ", book.getReviews()); // Format reviews

                writer.write(String.format("%s,%s,%s,%d,%s", 
                    book.getTitle(), 
                    book.getAuthor(), 
                    ratingDisplay, 
                    ratingCount, 
                    reviewsDisplay // Added rating count and formatted reviews
                ));

                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write error
        }
    }

    public void loadFromCSV() {
        books.clear(); // Clear existing books
        try (BufferedReader br = new BufferedReader(new FileReader(GENERAL_CSV))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header
                    continue;
                }

                String[] values = line.split(","); // Split CSV row into values
                if (values.length >= 2) {
                    String title = values[0];
                    String author = values[1];

                    GeneralBook book = new GeneralBook(title, author); // Create new book

                    // Handle potential additional fields (like rating and reviews)
                    if (values.length >= 3) {
                        try {
                            double averageRating = Double.parseDouble(values[2]); // Parse average rating
                            int ratingCount = (values.length >= 4) 
                                ? Integer.parseInt(values[3]) // Get rating count if available
                                : 0;
                            book.setRatingCount(ratingCount);
                            if (averageRating >= 0) {
                                book.addRating(averageRating);
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid values for rating
                        }
                    }

                    if (values.length >= 5) { // Parse reviews if available
                        String[] reviews = values[4].split(", ");
                        for (String review : reviews) {
                            book.addReview(review);
                        }
                    }

                    books.add(book); // Add the book to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read error
        }
    }
}
