package com.folives.item;

public class CustomerFoodMenuItem {

    String menu_id, id, chef_id, FoodImg, FoodName, ChefImg, ChefName, Quantity, VegNonveg, Rating, Price, Fav, category, FoodDesc, Cuisine, FoodType;

    public CustomerFoodMenuItem() {

    }

    public CustomerFoodMenuItem(String menu_id, String id, String chef_id, String FoodImg, String FoodName, String ChefImg,
                                String ChefName, String Quantity, String VegNonveg, String Rating, String Price, String Fav, String category, String FoodDesc, String Cuisine, String FoodType) {

        this.menu_id = menu_id;
        this.id = id;
        this.chef_id = chef_id;
        this.FoodImg = FoodImg;
        this.FoodName = FoodName;
        this.ChefImg = ChefImg;
        this.ChefName = ChefName;
        this.Quantity = Quantity;
        this.VegNonveg = VegNonveg;
        this.Rating = Rating;
        this.Price = Price;
        this.Fav = Fav;
        this.category = category;
        this.FoodDesc = FoodDesc;
        this.Cuisine = Cuisine;
        this.FoodType = FoodType;
    }


    public String getFoodDesc() {
        return FoodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        FoodDesc = foodDesc;
    }

    public String getCuisine() {
        return Cuisine;
    }

    public void setCuisine(String cuisine) {
        Cuisine = cuisine;
    }

    public String getFoodType() {
        return FoodType;
    }

    public void setFoodType(String foodType) {
        FoodType = foodType;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChef_id() {
        return chef_id;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodImg() {
        return FoodImg;
    }

    public void setFoodImg(String foodImg) {
        FoodImg = foodImg;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getChefImg() {
        return ChefImg;
    }

    public void setChefImg(String chefImg) {
        ChefImg = chefImg;
    }

    public String getChefName() {
        return ChefName;
    }

    public void setChefName(String chefName) {
        ChefName = chefName;
    }

    public String getVegNonveg() {
        return VegNonveg;
    }

    public void setVegNonveg(String vegNonveg) {
        VegNonveg = vegNonveg;
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

    public String getFav() {
        return Fav;
    }

    public void setFav(String fav) {
        Fav = fav;
    }

}
