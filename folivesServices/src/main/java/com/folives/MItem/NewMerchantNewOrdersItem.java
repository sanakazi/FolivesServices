package com.folives.MItem;

public class NewMerchantNewOrdersItem {

    String order_id, time_slot_id, payment_type, amount, delivery_status, delivery_status_id, delivery_time, user_name, profile_pic,next_order_status_id, next_order_status_name,
            next_order_status_value, time_slot_name, time_slot_value;

    public NewMerchantNewOrdersItem() {

    }

    public NewMerchantNewOrdersItem(String order_id, String time_slot_id, String payment_type, String amount, String delivery_status, String delivery_status_id, String delivery_time, String user_name, String profile_pic,String next_order_status_id, String next_order_status_name,
                                    String next_order_status_value, String time_slot_name, String time_slot_value) {

        this.order_id = order_id;
        this.time_slot_id = time_slot_id;
        this.payment_type = payment_type;
        this.amount = amount;
        this.delivery_status = delivery_status;
        this.delivery_status_id = delivery_status_id;
        this.delivery_time = delivery_time;
        this.user_name = user_name;
        this.profile_pic = profile_pic;
        this.next_order_status_id = next_order_status_id;
        this.next_order_status_name = next_order_status_name;
        this.next_order_status_value = next_order_status_value;
        this.time_slot_name = time_slot_name;
        this.time_slot_value = time_slot_value;

    }

    public String getNext_order_status_id() {
        return next_order_status_id;
    }

    public void setNext_order_status_id(String next_order_status_id) {
        this.next_order_status_id = next_order_status_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTime_slot_id() {
        return time_slot_id;
    }

    public void setTime_slot_id(String time_slot_id) {
        this.time_slot_id = time_slot_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getDelivery_status_id() {
        return delivery_status_id;
    }

    public void setDelivery_status_id(String delivery_status_id) {
        this.delivery_status_id = delivery_status_id;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getNext_order_status_name() {
        return next_order_status_name;
    }

    public void setNext_order_status_name(String next_order_status_name) {
        this.next_order_status_name = next_order_status_name;
    }

    public String getNext_order_status_value() {
        return next_order_status_value;
    }

    public void setNext_order_status_value(String next_order_status_value) {
        this.next_order_status_value = next_order_status_value;
    }

    public String getTime_slot_name() {
        return time_slot_name;
    }

    public void setTime_slot_name(String time_slot_name) {
        this.time_slot_name = time_slot_name;
    }

    public String getTime_slot_value() {
        return time_slot_value;
    }

    public void setTime_slot_value(String time_slot_value) {
        this.time_slot_value = time_slot_value;
    }
}
