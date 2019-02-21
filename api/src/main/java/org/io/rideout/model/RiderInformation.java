package org.io.rideout.model;

import java.util.ArrayList;

public class RiderInformation {

    private String emergencyContactNumber;
    private Boolean isInsured;
    private ArrayList<Vehicle> vehicles;
    private String license;
    private ArrayList<Payment> payments;

    public RiderInformation() {
        super();
        this.vehicles = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public RiderInformation(String emergencyContactNumber, Boolean isInsured, String license) {
        this.emergencyContactNumber = emergencyContactNumber;
        this.isInsured = isInsured;
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
