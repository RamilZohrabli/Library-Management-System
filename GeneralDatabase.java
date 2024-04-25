import java.util.*;
public class GeneralDatabase{
    private String author;
    private String title;
    private double averageRating;
    private int countRating;
    private List<String> reviews;
    //Constructor
    public GeneralDatabase(String title, String author){
        this.title = title;
        this.averageRating = 0.0;
        this.countRating = 0;
        if(author == null){
            this.author = "Unknown";
        }
        else{
            this.author = author;
        }
        this.reviews = new ArrayList<>();
    }
    //Getter methods
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public double getAverageRating(){
        return averageRating;
    }
    public int getcountRating(){
        return countRating;
    }
    

}