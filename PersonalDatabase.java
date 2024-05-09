import java.io.*;
import java.util.*;

public class PersonalDatabase {
    private List<PersonalBook> personalBooks;
    private String currentUser;

    public PersonalDatabase() {
        personalBooks = new ArrayList<>();
        currentUser = "";
    }

    public List<PersonalBook> getPersonalBooks() {
        return new ArrayList<>(personalBooks);
    }

    public void setUser(String username) {
        this.currentUser = username;
    }

    public void addPersonalBook(PersonalBook book) {
        personalBooks.add(book);
    }

    public PersonalBook getPersonalBook(String title) {
        return personalBooks.stream()
            .filter(book -> book.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
    }

    public void saveToFile() {
        if (currentUser.isEmpty()) {
            return;
        }

        String filePath = currentUser + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PersonalBook book : personalBooks) {
                StringBuilder sb = new StringBuilder();

                sb.append(book.getTitle()).append(",")
                  .append(book.getAuthor()).append(",")
                  .append(book.getStatus()).append(",")
                  .append(book.getTimeSpent()).append(",")
                  .append(book.getStartDate()).append(",") // Include start date
                  .append(book.getEndDate()); // Include end date

                // Append user ratings
                for (double rating : book.getUserRatings()) {
                    sb.append(",").append(rating);
                }

                // Append user reviews
                for (String review : book.getUserReviews()) {
                    sb.append(",").append(review);
                }

                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write error
        }
    }

    public void loadFromFile() {
        if (currentUser.isEmpty()) {
            return;
        }

        String filePath = currentUser + ".csv";
        personalBooks.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split at commas to separate data
                if (parts.length >= 6) { // Ensure sufficient parts for the expected data
                    PersonalBook book = new PersonalBook(parts[0], parts[1]);
                    book.setStatus(parts[2]);
                    book.addTimeSpent(Integer.parseInt(parts[3]));
                    book.setStartDate(parts[4]);
                    book.setEndDate(parts[5]);

                    // Process user ratings and reviews
                    for (int i = 6; i < parts.length; i++) {
                        try {
                            double rating = Double.parseDouble(parts[i]); // Attempt to parse as rating
                            book.addUserRating(rating);
                        } catch (NumberFormatException ex) {
                            book.addUserReview(parts[i]); // Otherwise, treat as review
                        }
                    }

                    personalBooks.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential IO errors
        }
    }
}