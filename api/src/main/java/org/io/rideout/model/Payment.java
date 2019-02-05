package org.io.rideout.model;

import java.util.Date;

public class Payment {

    private String id;
    private Date date;
    private String rideOutId;
    private Float amount;

    public Payment(String id, Date date, String rideOutId, Float amount) {
        this.id = id;
        this.date = date;
        this.rideOutId = rideOutId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRideOutId() {
        return rideOutId;
    }

    public void setRideOutId(String rideOutId) {
        this.rideOutId = rideOutId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
