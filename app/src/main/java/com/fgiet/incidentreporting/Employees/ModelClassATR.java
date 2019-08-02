package com.fgiet.incidentreporting.Employees;

/**
 * Created by This Pc on 3/30/2018.
 */

public class ModelClassATR {

    String date;
    String desc;
    String workerid;
    String workername;
    int imagecount;
    String atrno;



    String requestid;

    public  ModelClassATR(){

    }

    public ModelClassATR(String date, String desc, String workerid, String workername, int imagecount,String atrno,String requestid) {
        this.date = date;
        this.desc = desc;
        this.workerid = workerid;
        this.workername = workername;
        this.imagecount = imagecount;
        this.atrno=atrno;
        this.requestid=requestid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
    public String getAtrno() {
        return atrno;
    }

    public void setAtrno(String atrno) {
        this.atrno = atrno;
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

    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    public String getWorkername() {
        return workername;
    }

    public void setWorkername(String workername) {
        this.workername = workername;
    }

    public int getImagecount() {
        return imagecount;
    }

    public void setImagecount(int imagecount) {
        this.imagecount = imagecount;
    }
}
