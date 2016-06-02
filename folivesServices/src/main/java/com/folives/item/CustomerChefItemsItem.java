package com.folives.item;

public class CustomerChefItemsItem {

    String Id, Image, Name, Fav, VegNonVeg, Rating, Price, Desc, Cusine, Type, ReviewFlag;

    public CustomerChefItemsItem() {

    }

    public CustomerChefItemsItem(String Id, String Image, String Name, String Fav, String VegNonVeg, String Rating,
                                 String Price, String Desc, String Cusine, String Type, String ReviewFlag) {

        this.Id = Id;
        this.Image = Image;
        this.Name = Name;
        this.Fav = Fav;
        this.VegNonVeg = VegNonVeg;
        this.Rating = Rating;
        this.Price = Price;
        this.Desc = Desc;
        this.Cusine = Cusine;
        this.Type = Type;
        this.ReviewFlag = ReviewFlag;
    }

    public String getReviewFlag() {
        return ReviewFlag;
    }

    public void setReviewFlag(String reviewFlag) {
        ReviewFlag = reviewFlag;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCusine() {
        return Cusine;
    }

    public void setCusine(String cusine) {
        Cusine = cusine;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
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

    public String getFav() {
        return Fav;
    }

    public void setFav(String fav) {
        Fav = fav;
    }

    public String getVegNonVeg() {
        return VegNonVeg;
    }

    public void setVegNonVeg(String vegNonVeg) {
        VegNonVeg = vegNonVeg;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

}
