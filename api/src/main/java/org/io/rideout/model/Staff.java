package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Date;

@JsonTypeName("StaffModel")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "modelType")
public class Staff extends User {

    private boolean isAdmin;

    public Staff() {
        super();
    }

    public Staff(String id, String username, String password, String firstName, String lastName, Date dateOfBirth, String contactNumber, boolean isAdmin) {
        super(id, username, password, firstName, lastName, dateOfBirth, contactNumber);
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
