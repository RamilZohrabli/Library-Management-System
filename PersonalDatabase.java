import java.io.*;
import java.util.*;

public class PersonalDatabase {
    private List<PersonalBook> personalBooks;

    public PersonalDatabase() {
        personalBooks = new ArrayList<>();
    }

    public List<PersonalBook> getPersonalBooks() {
        return new ArrayList<>(personalBooks);
    }

    public void addPersonalBook(PersonalBook book) {
        personalBooks.add(book);
    }

    public void removePersonalBook(String title) {
        personalBooks.removeIf(book -> book.getTitle().equals(title));
    }

    public PersonalBook getPersonalBook(String title) {
        return personalBooks.stream()
            .filter(book -> book.getTitle().equals(title))
            .findFirst()
            .orElse(null);
    }

    public void clear() {
        personalBooks.clear();
    }

    public void saveToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PersonalBook book : personalBooks) {
                // Save key data for each personal book
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.getStatus() + "," + book.getTimeSpent() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String filePath) {
        personalBooks.clear(); // Clear existing books before loading
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
                    personalBooks.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}