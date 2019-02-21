package org.io.rideout.model;

import java.util.Date;

public class User {

    public final static String STAFF = "staff";
    public final static String RIDER = "rider";

    private String id;
    private String username;
    private String password;
    private String role = User.RIDER;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String contactNumber;
    private RiderInformation riderInformation;

    public User() {}

    public User(String id, String username, String password, String role, String firstName, String lastName, Date dateOfBirth, String contactNumber, RiderInformation riderInformation) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.riderInformation = riderInformation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public RiderInformation getRiderInformation() {
        return riderInformation;
    }

    public void setRiderInformation(RiderInformation riderInformation) {
        this.riderInformation = riderInformation;
    }
}
