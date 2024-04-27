import java.util.*;

public class Book {
    //Attributes
    private String title;
    private String author;
    private Float averageRating;
    private List<String> userReviews;
    //Constructor
    public Book(String title, String author){
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
        this.averageRating = null;
        this.userReviews = new ArrayList<>();
    }
}
