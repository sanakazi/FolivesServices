package com.folives.item;

public class CustomerPinCodeItem {

	String Id, AreaName, PostCode;

	public CustomerPinCodeItem() {

	}

	public CustomerPinCodeItem(String Id, String AreaName, String PostCode) {
		this.Id = Id;
		this.AreaName = AreaName;
		this.PostCode = PostCode;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getAreaName() {
		return AreaName;
	}

	public void setAreaName(String areaName) {
		AreaName = areaName;
	}

	public String getPostCode() {
		return PostCode;
	}

	public void setPostCode(String postCode) {
		PostCode = postCode;
	}

}
