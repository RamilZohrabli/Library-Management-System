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
        books.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(GENERAL_CSV))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }

                String[] values = line.split(",");
                if (values.length >= 2) {
                    String title = values[0];
                    String author = values[1];

                    GeneralBook book = new GeneralBook(title, author);

                    // If a rating is stored, initialize it
                    if (values.length >= 3) {
                        try {
                            double rating = Double.parseDouble(values[2]);
                            if (rating >= 0) {
                                book.addRating(rating); // Initialize the rating
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid ratings
                        }
                    }

                    // If there's a stored rating count, update the book's rating count
                    if (values.length >= 4) {
                        try {
                            int ratingCount = Integer.parseInt(values[3]);
                            if (ratingCount >= 0) {
                                book.setRatingCount(ratingCount); // Initialize the rating count
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid rating counts
                        }
                    }

                    books.add(book); // Add to the list of general books
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<GeneralBook> getBooks() {
        return new ArrayList<>(books); // Return a copy of the list for encapsulation
    }

    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GENERAL_CSV))) {
            writer.write("Title,Author,Rating,Reviews"); // Header without Rating Count
            writer.newLine(); // Ensure a new line after the header

            for (GeneralBook book : books) {
                double averageRating = book.getAverageRating();
                int ratingCount = book.getRatingCount();

                String ratingDisplay = averageRating < 0 
                    ? "No rating" 
                    : String.format("%.2f (%d)", averageRating, ratingCount); // Show count in parentheses

                // Proper representation for reviews
                String reviewText = book.getReviews().isEmpty() 
                    ? "No Reviews" 
                    : String.join(", ", book.getReviews());

                // Write book details to CSV with proper formatting
                writer.write(String.format("%s,%s,%s,%s", 
                    book.getTitle(), 
                    book.getAuthor(), 
                    ratingDisplay, 
                    reviewText
                ));
                writer.newLine(); // New line after each book
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO exceptions
        }
    }

    
    public void updateBookRating(String title, double newRating) {
        GeneralBook book = books.stream()
            .filter(b -> b.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElseGet(() -> {
                GeneralBook newBook = new GeneralBook(title, "Unknown");
                books.add(newBook);
                return newBook;
            });

        book.addRating(newRating); // Update the book's rating
        saveToCSV(); // Save changes to the CSV file
    }
}
