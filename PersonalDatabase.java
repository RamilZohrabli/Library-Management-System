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
            .filter(book -> book.getTitle().equalsIgnoreCase(title)) // Case-insensitive title matching
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
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getStatus() + "," + book.getTimeSpent());
                for (double rating : book.getUserRatings()) {
                    writer.write("," + rating);
                }
                for (String review : book.getUserReviews()) {
                    writer.write("," + review);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    PersonalBook book = new PersonalBook(parts[0], parts[1]);
                    book.setStatus(parts[2]);
                    if (parts.length > 3) {
                        book.addTimeSpent(Integer.parseInt(parts[3]));
                    }
                    for (int i = 4; i < parts.length; i++) {
                        try {
                            double rating = Double.parseDouble(parts[i]);
                            book.addUserRating(rating);
                        } catch (NumberFormatException ex) {
                            book.addUserReview(parts[i]);
                        }
                    }
                    personalBooks.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
