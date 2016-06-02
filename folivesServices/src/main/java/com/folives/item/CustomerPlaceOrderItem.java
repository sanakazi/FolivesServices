package com.folives.item;

/**
 * Created by ravikant on 17-02-2016.
 */
public class CustomerPlaceOrderItem {

    String item_id, cart_qty, slot_id, delivery_time, address_id;

    public CustomerPlaceOrderItem() {

    }

    public CustomerPlaceOrderItem(String item_id, String cart_qty, String slot_id, String delivery_time, String address_id) {
        this.item_id = item_id;
        this.cart_qty = cart_qty;
        this.slot_id = slot_id;
        this.delivery_time = delivery_time;
        this.address_id = address_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCart_qty() {
        return cart_qty;
    }

    public void setCart_qty(String cart_qty) {
        this.cart_qty = cart_qty;
    }

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
