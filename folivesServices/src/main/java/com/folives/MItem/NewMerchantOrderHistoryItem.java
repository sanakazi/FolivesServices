package com.folives.MItem;

/**
 * Created by ravikant on 16-03-2016.
 */
public class NewMerchantOrderHistoryItem {

    String OrderId, PaymentType, TotalAmt, DeliveryStatus, DeliveryStatusId, DeliveryTime, TimeSlot, DateTime, Name, Email, Mobile,
            Pic, TimeSlotValue, TimeSlotName;

    public NewMerchantOrderHistoryItem() {

    }

    public NewMerchantOrderHistoryItem(String OrderId, String PaymentType, String TotalAmt, String DeliveryStatus,
                                       String DeliveryStatusId, String DeliveryTime, String TimeSlot, String DateTime,
                                       String Name, String Email, String Mobile, String Pic, String TimeSlotValue, String TimeSlotName) {

        this.OrderId = OrderId;
        this.PaymentType = PaymentType;
        this.TotalAmt = TotalAmt;
        this.DeliveryStatus = DeliveryStatus;
        this.DeliveryStatusId = DeliveryStatusId;
        this.DeliveryTime = DeliveryTime;
        this.TimeSlot = TimeSlot;
        this.DateTime = DateTime;
        this.Name = Name;
        this.Email = Email;
        this.Mobile = Mobile;
        this.Pic = Pic;
        this.TimeSlotValue = TimeSlotValue;
        this.TimeSlotName = TimeSlotName;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public String getDeliveryStatusId() {
        return DeliveryStatusId;
    }

    public void setDeliveryStatusId(String deliveryStatusId) {
        DeliveryStatusId = deliveryStatusId;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getTimeSlotValue() {
        return TimeSlotValue;
    }

    public void setTimeSlotValue(String timeSlotValue) {
        TimeSlotValue = timeSlotValue;
    }

    public String getTimeSlotName() {
        return TimeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        TimeSlotName = timeSlotName;
    }
}