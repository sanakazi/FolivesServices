package com.folives.item;

/**
 * Created by ravikant on 19-02-2016.
 */
public class CustomerOrderTrackingListItem {

    String order_id, status_id, created_at, status_name;

    public CustomerOrderTrackingListItem() {

    }

    public CustomerOrderTrackingListItem(String order_id, String status_id, String created_at, String status_name) {
        this.order_id = order_id;
        this.status_id = status_id;
        this.created_at = created_at;
        this.status_name = status_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String order_name) {
        this.status_name = order_name;
    }
}
