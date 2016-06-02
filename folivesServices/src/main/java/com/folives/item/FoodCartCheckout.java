package com.folives.item;

public class FoodCartCheckout {

    String menu_id, id, name, price, count, t_price, img, qty;

    public FoodCartCheckout() {
    }

    public FoodCartCheckout(String menu_id, String id, String name, String price, String count, String t_price, String img,
                            String qty) {
        this.menu_id = menu_id;
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.t_price = t_price;
        this.img = img;
        this.qty = qty;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getT_price() {
        return t_price;
    }

    public void setT_price(String t_price) {
        this.t_price = t_price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
