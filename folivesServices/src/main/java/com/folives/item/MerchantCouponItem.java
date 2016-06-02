package com.folives.item;

public class MerchantCouponItem {

	String id, name, type, amount, expiration, active, used, created;

	public MerchantCouponItem() {

	}

	public MerchantCouponItem(String id, String name, String type, String amount, String expiration, String active,
			String used, String created) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.amount = amount;
		this.expiration = expiration;
		this.active = active;
		this.used = used;
		this.created = created;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
