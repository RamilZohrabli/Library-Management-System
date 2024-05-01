import java.util.ArrayList;
import java.util.List;

public class PersonalDatabase {
    private List<PersonalBook> personalBooks;

    public PersonalDatabase() {
        personalBooks = new ArrayList<>();
    }

    // Method to return the list of personal books
    public List<PersonalBook> getPersonalBooks() {
        return new ArrayList<>(personalBooks); // Returning a copy of the list to avoid external modification
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
}
