import java.util.*;

public class PersonalBook extends GeneralBook {
    private String status;
    private int timeSpent;
    private List<Double> userRatings;
    private List<String> userReviews;

    public PersonalBook(String title, String author) {
        super(title, author);
        this.status = "Not Started";
        this.timeSpent = 0;
        this.userRatings = new ArrayList<>();
        this.userReviews = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void addTimeSpent(int time) {
        if (time > 0) {
            this.timeSpent += time;
        }
    }

    public List<Double> getUserRatings() {
        return userRatings;
    }

    public List<String> getUserReviews() {
        return userReviews;
    }

    public void addUserRating(double rating) {
        if (rating >= 1 && rating <= 5) {
            userRatings.add(rating);
            addRating(rating); // Update the overall rating in the general database
        }
    }

    public void addUserReview(String review) {
        if (review != null && !review.isEmpty()) {
            userReviews.add(review);
        }
    }
}
