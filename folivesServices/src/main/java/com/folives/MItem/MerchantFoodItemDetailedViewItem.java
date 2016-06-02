package com.folives.MItem;

/**
 * Created by ravikant on 15-03-2016.
 */
public class MerchantFoodItemDetailedViewItem {

    String FoodPic, FoodName, FoodQuantity, FoodPrice;

    public MerchantFoodItemDetailedViewItem() {

    }

    public MerchantFoodItemDetailedViewItem(String FoodPic, String FoodName, String FoodQuantity, String FoodPrice) {
        this.FoodPic = FoodPic;
        this.FoodName = FoodName;
        this.FoodQuantity = FoodQuantity;
        this.FoodPrice = FoodPrice;
    }

    public String getFoodPic() {
        return FoodPic;
    }

    public void setFoodPic(String foodPic) {
        FoodPic = foodPic;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodQuantity() {
        return FoodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        FoodQuantity = foodQuantity;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }
}
