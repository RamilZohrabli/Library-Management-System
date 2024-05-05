import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
public class GeneralDatabase {
    private List<GeneralBook> books;
    private static final String GENERAL_CSV = "general.csv";

    public GeneralDatabase() {
        books = new ArrayList<>();
        ensureGeneralCSVExists();
    }

    private void ensureGeneralCSVExists() {
        File generalFile = new File(GENERAL_CSV);
        if (!generalFile.exists()) {
            try {
                Files.copy(Paths.get("brodsky.csv"), Paths.get(GENERAL_CSV));
            } catch (IOException e) {
                e.printStackTrace(); // Handle file copy error
            }
        }
    }

    public void loadFromCSV() {
        books.clear(); // Clear any existing data
        try (BufferedReader br = new BufferedReader(new FileReader(GENERAL_CSV))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] values = line.split(",");
                String title = values[0];
                String author = values.length > 1 ? values[1] : "Unknown";
                GeneralBook book = new GeneralBook(title, author);
                books.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file read error
        }
    }

    public List<GeneralBook> getBooks() {
        return new ArrayList<>(books); // Return a new list to ensure encapsulation
    }

    public void saveToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GENERAL_CSV))) {
            for (GeneralBook book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor());
                writer.newLine(); // Move to the next line
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle file write error
        }
    }
}

