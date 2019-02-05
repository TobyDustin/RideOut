package org.io.rideout.model;

public class Vehicle {

    private String id;
    private String make;
    private String model;
    private Integer power;
    private String registration;
    private Boolean isChecked;

    public Vehicle(String id, String make, String model, Integer power, String registration) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.power = power;
        this.registration = registration;
        this.isChecked = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
