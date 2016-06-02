package com.folives.item;

/**
 * Created by ravikant on 12-02-2016.
 */
public class CustomerItemDetailedChefReview {

    String item_id, rating, review, profile_pic;

    public CustomerItemDetailedChefReview() {

    }

    public CustomerItemDetailedChefReview(String item_id, String rating, String review, String profile_pic) {

        this.item_id = item_id;
        this.rating = rating;
        this.review = review;
        this.profile_pic = profile_pic;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
