package com.folives.item;

import java.io.Serializable;

/**
 * Created by ravikant on 18-02-2016.
 */


public class OrderTrackingFoodItem implements Serializable {

    String order_id, item_id, item_name, price, qty, pic;

    public OrderTrackingFoodItem() {

    }

    OrderTrackingFoodItem(String order_id, String item_id, String item_name, String price, String qty, String pic) {
        this.order_id = order_id;
        this.item_id = item_id;
        this.item_name = item_name;
        this.price = price;
        this.qty = qty;
        this.pic = pic;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
