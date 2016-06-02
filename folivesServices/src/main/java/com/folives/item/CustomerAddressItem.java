package com.folives.item;

/**
 * Created by ravikant on 03-02-2016.
 */
public class CustomerAddressItem {

    String id, userid, address, title, city, state, country, zipcode, landmark;

    public CustomerAddressItem() {

    }

    public CustomerAddressItem(String id, String userid, String address, String title, String city, String state, String country, String zipcode, String landmark) {
        this.id = id;
        this.userid = userid;
        this.address = address;
        this.title = title;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.landmark = landmark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
