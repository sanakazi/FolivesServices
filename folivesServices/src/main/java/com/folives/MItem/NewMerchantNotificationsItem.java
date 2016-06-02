package com.folives.MItem;

/**
 * Created by ravikant on 15-03-2016.
 */
public class NewMerchantNotificationsItem {

    String notificationId, message, name, pic, datetime;

    public NewMerchantNotificationsItem(){};

    public NewMerchantNotificationsItem(String notificationId, String message, String name, String pic, String datetime) {
        this.notificationId = notificationId;
        this.message = message;
        this.name = name;
        this.pic = pic;
        this.datetime = datetime;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
