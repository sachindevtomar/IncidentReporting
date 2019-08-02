package com.fgiet.incidentreporting.Users;

/**
 * Created by 1401480 on 7/18/2017.
 */

public class ModelClassPost {
    String desc,date,postno;
    int imagecount;

    public ModelClassPost() {
    }

    public ModelClassPost(String desc, String date, String postno, int imagecount,int likes) {
        this.desc = desc;
        this.date = date;
        this.postno = postno;
        this.imagecount = imagecount;

    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostno() {
        return postno;
    }

    public void setPostno(String postno) {
        this.postno = postno;
    }

    public int getImagecount() {
        return imagecount;
    }

    public void setImagecount(int imagecount) {
        this.imagecount = imagecount;
    }
}
