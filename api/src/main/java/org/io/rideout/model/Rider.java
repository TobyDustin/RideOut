package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Date;

@JsonTypeName("RiderModel")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "modelType")
public class Rider extends User {

    private String emergencyContactNumber;
    private Boolean isInsured;
    private Boolean isLead;
    private ArrayList<Vehicle> vehicles;
    private String license;
    private ArrayList<Payment> payments;

    public Rider() {
        super();
        this.vehicles = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public Rider(String id, String username, String firstName, String lastName, Date dateOfBirth, String contactNumber, String emergencyContactNumber, Boolean isInsured, Boolean isLead, String license) {
        super(id, username, firstName, lastName, dateOfBirth, contactNumber);
        this.emergencyContactNumber = emergencyContactNumber;
        this.isInsured = isInsured;
        this.isLead = isLead;
        this.license = license;
        this.vehicles = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public Boolean isInsured() {
        return isInsured;
    }

    public void setInsured(Boolean insured) {
        isInsured = insured;
    }

    public Boolean isLead() {
        return isLead;
    }

    public void setLead(Boolean lead) {
        isLead = lead;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
}
