package com.folives.item;

public class MerchantCouponDetailsItem {

	String id, name, type, discount, expiration_date, used_count;

	public MerchantCouponDetailsItem() {

	}

	public MerchantCouponDetailsItem(String id, String name, String type, String discount, String expiration_date,
			String used_count) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.discount = discount;
		this.expiration_date = expiration_date;
		this.used_count = used_count;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	public String getUsed_count() {
		return used_count;
	}

	public void setUsed_count(String used_count) {
		this.used_count = used_count;
	}

}
