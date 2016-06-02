package com.folives.item;

public class CustomerFavouriteFoodItem {

    String FavId, FoodPic, Chef_id, ChefPic, ChefName, VegNonveg, FoodName, FoodRating, FoodPrice;

    public CustomerFavouriteFoodItem() {

    }

    public CustomerFavouriteFoodItem(String FavId, String FoodPic, String Chef_id, String ChefPic, String ChefName, String VegNonveg,
                                     String FoodName, String FoodRating, String FoodPrice) {

        this.FavId = FavId;
        this.FoodPic = FoodPic;
        this.Chef_id = Chef_id;
        this.ChefPic = ChefPic;
        this.ChefName = ChefName;
        this.VegNonveg = VegNonveg;
        this.FoodName = FoodName;
        this.FoodRating = FoodRating;
        this.FoodPrice = FoodPrice;
    }

    public String getFavId() {
        return FavId;
    }

    public void setFavId(String favId) {
        FavId = favId;
    }

    public String getChef_id() {
        return Chef_id;
    }

    public void setChef_id(String chef_id) {
        Chef_id = chef_id;
    }

    public String getFoodPic() {
        return FoodPic;
    }

    public void setFoodPic(String foodPic) {
        FoodPic = foodPic;
    }

    public String getChefPic() {
        return ChefPic;
    }

    public void setChefPic(String chefPic) {
        ChefPic = chefPic;
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

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodRating() {
        return FoodRating;
    }

    public void setFoodRating(String foodRating) {
        FoodRating = foodRating;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

}
