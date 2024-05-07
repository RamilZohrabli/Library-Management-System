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
                String title = values[0];
                String author = values.length > 1 ? values[1] : "Unknown";

                GeneralBook book = new GeneralBook(title, author);

                // If there's a stored rating, add it to the book
                if (values.length > 2) {
                    try {
                        double rating = Double.parseDouble(values[2]);
                        if (rating != -1) {
                            book.addRating(rating);
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid rating values
                    }
                }

                // If there's a stored rating count, update the book's rating count
                if (values.length > 3) {
                    try {
                        int ratingCount = Integer.parseInt(values[3]);
                        if (ratingCount > 0) {
                            book.setRatingCount(ratingCount);
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid rating count
                    }
                }

                books.add(book); // Add to the general list
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
            writer.write("Title,Author,Rating,Rating Count"); // Header
            writer.newLine();

            for (GeneralBook book : books) {
                double rating = book.getAverageRating();
                int ratingCount = book.getRatingCount();

                writer.write(book.getTitle() + "," + book.getAuthor() + "," + (rating == -1 ? "No rating" : String.format("%.2f", rating)) + "," + (ratingCount > 0 ? ratingCount : 0));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        book.addRating(newRating);
        saveToCSV();
    }
}
