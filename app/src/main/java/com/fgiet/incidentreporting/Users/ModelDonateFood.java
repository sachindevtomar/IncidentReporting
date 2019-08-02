package com.fgiet.incidentreporting.Users;

/**
 * Created by Admin on 8/23/2017.
 */

public class ModelDonateFood {
    private String name,desc,type;
    private String price;


    private String imageurl;

    public ModelDonateFood(String name, String desc, String type, String price, String imageurl) {
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.price = price;
        this.imageurl = imageurl;
    }

    public ModelDonateFood() {
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
