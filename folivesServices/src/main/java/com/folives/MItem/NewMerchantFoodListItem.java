package com.folives.MItem;

/**
 * Created by ravikant on 05-04-2016.
 */
public class NewMerchantFoodListItem {

    String FoodId, FoodName, FoodDesc, IsActive, Category, Price, Photo, City, Cusine, ItemType, DietType,ItemTypeValue, DietTypeValue;

    public NewMerchantFoodListItem() {

    }

    public NewMerchantFoodListItem(String FoodId, String FoodName, String FoodDesc, String IsActive, String Category,
                                   String Price, String Photo, String City, String Cusine, String ItemType,
                                   String DietType) {

        this.FoodId = FoodId;
        this.FoodName = FoodName;
        this.FoodDesc = FoodDesc;
        this.IsActive = IsActive;
        this.Category = Category;
        this.Price = Price;
        this.Photo = Photo;
        this.City = City;
        this.Cusine = Cusine;
        this.ItemType = ItemType;
        this.DietType = DietType;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodDesc() {
        return FoodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        FoodDesc = foodDesc;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCusine() {
        return Cusine;
    }

    public String getItemTypeValue() {
        return ItemTypeValue;
    }

    public void setItemTypeValue(String itemTypeValue) {
        ItemTypeValue = itemTypeValue;
    }

    public String getDietTypeValue() {
        return DietTypeValue;
    }

    public void setDietTypeValue(String dietTypeValue) {
        DietTypeValue = dietTypeValue;
    }

    public void setCusine(String cusine) {
        Cusine = cusine;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getDietType() {
        return DietType;
    }

    public void setDietType(String dietType) {
        DietType = dietType;
    }
}
