package org.io.rideout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Payment {

    @BsonId
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    private ObjectId id;
    private Date date;
    private String rideOutId;
    private Float amount;

    public Payment(ObjectId id, Date date, String rideOutId, Float amount) {
        this.id = id;
        this.date = date;
        this.rideOutId = rideOutId;
        this.amount = amount;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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
