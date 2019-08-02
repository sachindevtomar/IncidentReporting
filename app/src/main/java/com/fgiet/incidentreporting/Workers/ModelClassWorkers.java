package com.fgiet.incidentreporting.Workers;

/**
 * Created by This Pc on 7/16/2017.
 */

public class ModelClassWorkers {

    private String Engaged;
    private String Name;
    private String Phone;
    private String RequestId;
    private String UserId;
    private String EmailId;
    private String Employee;

    public ModelClassWorkers() {
    }

    public ModelClassWorkers(String engaged, String name, String phone, String requestId, String userId, String emailId, String employee) {
        Engaged = engaged;
        Name = name;
        Phone = phone;
        RequestId = requestId;
        UserId = userId;
        EmailId = emailId;
        Employee = employee;
    }


    public String getEngaged() {
        return Engaged;
    }

    public void setEngaged(String engaged) {
        Engaged = engaged;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String employee) {
        Employee = employee;
    }
}
