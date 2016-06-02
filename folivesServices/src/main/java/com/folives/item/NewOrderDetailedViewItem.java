package com.folives.item;

public class NewOrderDetailedViewItem {

	String id, order_id, client_id, item_id, item_name, item_price, discounted_price, qty;

	public NewOrderDetailedViewItem() {

	}

	public NewOrderDetailedViewItem(String id, String order_id, String client_id, String item_id, String item_name,
			String item_price, String discounted_price, String qty) {
		this.id = id;
		this.order_id = order_id;
		this.client_id = client_id;
		this.item_id = item_id;
		this.item_name = item_name;
		this.item_price = item_price;
		this.discounted_price = discounted_price;
		this.qty = qty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
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

	public String getItem_price() {
		return item_price;
	}

	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}

	public String getDiscounted_price() {
		return discounted_price;
	}

	public void setDiscounted_price(String discounted_price) {
		this.discounted_price = discounted_price;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

}
