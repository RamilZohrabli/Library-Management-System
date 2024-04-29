import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class GeneralDatabase{
    private List<GeneralBook> books;

    public GeneralDatabase(){
        books = new ArrayList<>();
    }
    public void loadFromCSV(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String l;
            boolean isHeader = true;
            while((l = br.readLine())!=null){
                if(isHeader){
                    isHeader = false;
                    continue;
                }
                String[] values = l.split(",");
                String title = values[0];
                String author = values.length > 1 ? values[1] : "Unknown";
                GeneralBook book = new GeneralBook(title, author);
                books.add(book);
            }
        }catch (IOException e){
            e.printStackTrace(); // Exception handling
        }
    }
    public List<GeneralBook> getBooks() {
        return books;
    }
}