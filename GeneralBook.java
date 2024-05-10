import java.util.*;

public class GeneralBook {
    private String title;
    private String author;
    private double averageRating;
    private int ratingCount;
    private List<String> reviews;

    public GeneralBook(String title, String author) {
        if (title != null) {
            this.title = title;
        } else {
            this.title = "Unknown";
        }
        if (author != null) {
            this.author = author;
        } else {
            this.author = "Unknown";
        }
        this.averageRating = 0;
        this.ratingCount = 0;
        this.reviews = new ArrayList<>();
    }

    // Getters 
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getAverageRating() {
        if (ratingCount > 0) {
            return averageRating;
        } else {
            return -1; // -1 for "No rating"
        }
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public List<String> getReviews() {
        return new ArrayList<>(reviews); 
    }

    //Review adding
    public void addReview(String review) {
        if (review != null && !review.isEmpty()) {
            reviews.add(review);
        }
    }

    //Rating adding
    public void addRating(double rating) {
        averageRating = (averageRating * ratingCount + rating) / (++ratingCount);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRatingCount(int count) {
        if (count >= 0) {
            this.ratingCount = count;
        }
    }
}
