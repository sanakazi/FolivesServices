package com.folives.MItem;

/**
 * Created by ravikant on 14-03-2016.
 */
public class NewMerchantFollowersItem {
    String fb_id, name, pic;

    public NewMerchantFollowersItem() {

    }

    public NewMerchantFollowersItem(String fb_id, String name, String pic) {
        this.fb_id = fb_id;
        this.name = name;
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }
}
