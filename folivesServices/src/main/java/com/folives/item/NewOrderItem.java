package com.folives.item;

public class NewOrderItem {

	String orderNumber, timeStamp, Amount, status, stats_id, payment_type, name;

	public NewOrderItem() {
	}

	public NewOrderItem(String orderNumber, String timeStamp, String Amount, String status, String stats_id,
			String payment_type, String name) {
		this.orderNumber = orderNumber;
		this.timeStamp = timeStamp;
		this.Amount = Amount;
		this.status = status;
		this.stats_id = stats_id;
		this.payment_type = payment_type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getStats_id() {
		return stats_id;
	}

	public void setStats_id(String stats_id) {
		this.stats_id = stats_id;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
