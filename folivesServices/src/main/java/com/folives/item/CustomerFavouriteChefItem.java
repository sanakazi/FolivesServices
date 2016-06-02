package com.folives.item;

public class CustomerFavouriteChefItem {

    String FavId, ChefId, ChefPic, ChefName, ChefRating;

    public CustomerFavouriteChefItem() {

    }

    public CustomerFavouriteChefItem(String FavId, String ChefId, String ChefPic, String ChefName, String ChefRating) {
        this.FavId = FavId;
        this.ChefId = ChefId;
        this.ChefPic = ChefPic;
        this.ChefName = ChefName;
        this.ChefRating = ChefRating;
    }

    public String getFavId() {
        return FavId;
    }

    public void setFavId(String favId) {
        FavId = favId;
    }

    public String getChefId() {
        return ChefId;
    }

    public void setChefId(String chefId) {
        ChefId = chefId;
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

    public String getChefRating() {
        return ChefRating;
    }

    public void setChefRating(String chefRating) {
        ChefRating = chefRating;
    }

}
