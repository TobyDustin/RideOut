package org.io.rideout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import javax.validation.constraints.NotNull;

public class SimpleUser {

    @BsonId
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    @Schema(type = "string")
    private ObjectId id;

    private String username;
    private String firstName;
    private String lastName;
    private Vehicle vehicle;

    public SimpleUser() {}

    public SimpleUser(ObjectId id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
