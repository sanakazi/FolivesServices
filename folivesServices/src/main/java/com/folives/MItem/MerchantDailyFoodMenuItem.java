package com.folives.MItem;

public class MerchantDailyFoodMenuItem {

    String menu_id;
    String id;
    String chef_id;
    String time_slot_id;
    String FoodImg;
    String FoodName;
    String ChefImg;
    String ChefName;
    String Quantity;
    String QtyPerOrder;
    String VegNonveg;
    String Rating;
    String Price;
    String Fav;
    String category;
    String FoodDesc;
    String Cuisine;
    String FoodType;
    String Date;

    public String getDate_ddMMyy() {
        return Date_ddMMyy;
    }

    public void setDate_ddMMyy(String date_ddMMyy) {
        Date_ddMMyy = date_ddMMyy;
    }

    String Date_ddMMyy;

    public MerchantDailyFoodMenuItem() {

    }

    public MerchantDailyFoodMenuItem(String menu_id, String id, String chef_id, String time_slot_id, String FoodImg, String FoodName, String ChefImg,
                                     String ChefName, String Quantity, String QtyPerOrder, String VegNonveg, String Rating, String Price, String Fav, String category, String FoodDesc, String Cuisine, String FoodType) {

        this.menu_id = menu_id;
        this.id = id;
        this.chef_id = chef_id;
        this.time_slot_id = time_slot_id;
        this.FoodImg = FoodImg;
        this.FoodName = FoodName;
        this.ChefImg = ChefImg;
        this.ChefName = ChefName;
        this.Quantity = Quantity;
        this.QtyPerOrder = QtyPerOrder;
        this.VegNonveg = VegNonveg;
        this.Rating = Rating;
        this.Price = Price;
        this.Fav = Fav;
        this.category = category;
        this.FoodDesc = FoodDesc;
        this.Cuisine = Cuisine;
        this.FoodType = FoodType;
    }


    public String getQtyPerOrder() {
        return QtyPerOrder;
    }

    public void setQtyPerOrder(String qtyPerOrder) {
        QtyPerOrder = qtyPerOrder;
    }

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public void setTime_slot_id(String time_slot_id) {
        this.time_slot_id = time_slot_id;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}


