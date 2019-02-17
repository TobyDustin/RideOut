package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.Date;

@JsonSubTypes({ @JsonSubTypes.Type(value = Rider.class), @JsonSubTypes.Type(value = Staff.class) })
public abstract class User {

    @BsonId
    protected String id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected Date dateOfBirth;
    protected String contactNumber;

    protected User() {}

    protected User(String id, String username, String password, String firstName, String lastName, Date dateOfBirth, String contactNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
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
}
