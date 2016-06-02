package com.folives.item;

public class MerchantFoodItemListItem {

	String strid, strImg, strName, strMealType, strPrice, strStatus, strDesc, strCusine, strQty;

	public MerchantFoodItemListItem() {

	}

	public MerchantFoodItemListItem(String strid, String strImg, String strName, String strMealType, String strPrice,
			String strStatus, String strDesc, String strCusine, String strQty) {

		this.strid = strid;
		this.strImg = strImg;
		this.strName = strName;
		this.strMealType = strMealType;
		this.strPrice = strPrice;
		this.strStatus = strStatus;
		this.strDesc = strDesc;
		this.strCusine = strCusine;
		this.strQty = strQty;
	}

	public String getStrDesc() {
		return strDesc;
	}

	public void setStrDesc(String strDesc) {
		this.strDesc = strDesc;
	}

	public String getStrCusine() {
		return strCusine;
	}

	public void setStrCusine(String strCusine) {
		this.strCusine = strCusine;
	}

	public String getStrQty() {
		return strQty;
	}

	public void setStrQty(String strQty) {
		this.strQty = strQty;
	}

	public String getStrid() {
		return strid;
	}

	public void setStrid(String strid) {
		this.strid = strid;
	}

	public String getStrImg() {
		return strImg;
	}

	public void setStrImg(String strImg) {
		this.strImg = strImg;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrMealType() {
		return strMealType;
	}

	public void setStrMealType(String strMealType) {
		this.strMealType = strMealType;
	}

	public String getStrPrice() {
		return strPrice;
	}

	public void setStrPrice(String strPrice) {
		this.strPrice = strPrice;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

}
