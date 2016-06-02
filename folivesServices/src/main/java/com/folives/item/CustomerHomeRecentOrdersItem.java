package com.folives.item;

public class CustomerHomeRecentOrdersItem {

	String order_id, order_amt, order_date, item_name;

	public CustomerHomeRecentOrdersItem() {

	}

	public CustomerHomeRecentOrdersItem(String order_id, String order_amt, String order_date, String item_name) {
		this.order_id = order_id;
		this.order_amt = order_amt;
		this.order_date = order_date;
		this.item_name = item_name;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getOrder_amt() {
		return order_amt;
	}

	public void setOrder_amt(String order_amt) {
		this.order_amt = order_amt;
	}

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

}
