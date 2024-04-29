import java.util.*;
public class GeneralBook{
    private String title;
    private String author;
    private double averageRating;
    private int ratingCount;
    private List<String> reviews;
    public GeneralBook(String title, String author){
        if(title != null){
            this.title = title;
        }
        else{
            this.title = "Unknown";
        }
        if(author != null){
            this.author = author;
        }
        else{
            this.author = "Unknown";
        }
        this.averageRating = 0;
        this.ratingCount = 0;
        this.reviews = new ArrayList<>();
    }
    

}