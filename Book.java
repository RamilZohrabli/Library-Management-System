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
    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public List<String> getUserReviews() {
        return userReviews;
    }
}
