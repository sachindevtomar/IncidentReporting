package com.fgiet.incidentreporting.Employees;

/**
 * Created by 1401480 on 7/13/2017.
 */

public class ModelClass {
    private String name="";
    private String userid="";
    private String date;
    private String desc;
    private String image_0;
    private String status;
    private int serialno;
    private String requestno;
    private String image_1;
    private String phone;
    private String emailid="";
    private double longitude,latitude;
    private String locality,sublocality,thoroughfare;
    private int imagecount;

    public ModelClass() {
    }

    public ModelClass(String name, String userid, String date, String desc, double longitude, double latitude, String image_0, String phone, String locality, String sublocality, String thoroughfare, int imagecount, String emailid ,String image_1,String requestno,String status,int serialno) {
        this.requestno=requestno;
        this.serialno=serialno;
        this.name = name;
        this.userid = userid;
        this.date = date;
        this.desc = desc;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image_0 = image_0;
        this.phone = phone;
        this.image_1=image_1;
        this.locality = locality;
        this.sublocality = sublocality;
        this.thoroughfare = thoroughfare;
        this.imagecount = imagecount;
        this.emailid=emailid;
        this.status=status;
    }
    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImage_0() {
        return image_0;
    }

    public void setImage_0(String image_0) {
        this.image_0 = image_0;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSublocality() {
        return sublocality;
    }

    public void setSublocality(String sublocality) {
        this.sublocality = sublocality;
    }

    public String getThoroughfare() {
        return thoroughfare;
    }

    public void setThoroughfare(String thoroughfare) {
        this.thoroughfare = thoroughfare;
    }

    public int getImagecount() {
        return imagecount;
    }

    public void setImagecount(int imagecount) {
        this.imagecount = imagecount;
    }
    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
