package com.folives.item;


public class CustomerOrderSummeryItem {

    String menu_id, id, name, price, img, cnt, qtyAvl;

    public CustomerOrderSummeryItem() {
    }

    public CustomerOrderSummeryItem(String menu_id, String id, String name, String price, String img, String cnt, String qtyAvl) {

        this.menu_id = menu_id;
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
        this.cnt = cnt;
        this.qtyAvl = qtyAvl;

    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getQtyAvl() {
        return qtyAvl;
    }

    public void setQtyAvl(String qtyAvl) {
        this.qtyAvl = qtyAvl;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {

        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
