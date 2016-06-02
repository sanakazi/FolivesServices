package com.folives.MItem;

/**
 * Created by ravikant on 16-03-2016.
 */
public class NewMerchantReviewItem {

    String Id, Review, Rating, DateTime, Name, Pic;

    public NewMerchantReviewItem(){

    }

    public NewMerchantReviewItem(String Id, String Review, String Rating, String DateTime, String Name, String Pic) {
        this.Id = Id;
        this.Review = Review;
        this.Rating = Rating;
        this.DateTime = DateTime;
        this.Name = Name;
        this.Pic = Pic;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }
}
