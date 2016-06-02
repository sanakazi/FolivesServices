package com.folives.item;

public class CustomerChefReviewsItem {

	String Id, Image, Name, Review, Rating, Date;

	public CustomerChefReviewsItem() {

	}

	public CustomerChefReviewsItem(String Id, String Image, String Name, String Review, String Rating, String Date) {
		this.Id = Id;
		this.Image = Image;
		this.Name = Name;
		this.Review = Review;
		this.Rating = Rating;
		this.Date = Date;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
