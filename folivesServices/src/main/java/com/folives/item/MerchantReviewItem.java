package com.folives.item;

public class MerchantReviewItem {

	String strImg, strName, strDate, strRating, strDesc;

	public MerchantReviewItem() {
	}

	public MerchantReviewItem(String strImg, String strName, String strDate, String strRating, String strDesc) {
		this.strImg = strImg;
		this.strName = strName;
		this.strDate = strDate;
		this.strRating = strRating;
		this.strDesc = strDesc;
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

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getStrRating() {
		return strRating;
	}

	public void setStrRating(String strRating) {
		this.strRating = strRating;
	}

	public String getStrDesc() {
		return strDesc;
	}

	public void setStrDesc(String strDesc) {
		this.strDesc = strDesc;
	}

}
