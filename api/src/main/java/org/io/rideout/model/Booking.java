package org.io.rideout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Booking {

    public final static String RESTAURANT = "restaurant";
    public final static String ACCOMMODATION = "accommodation";
    public final static String TRAVEL = "travel";

    @BsonId
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    @Schema(type = "string")
    private ObjectId id;
    private String name;
    private String reference;

    @NotNull
    @Pattern(regexp = (RESTAURANT + "|" + ACCOMMODATION + "|" + TRAVEL))
    private String type = RESTAURANT;

    public Booking() {}

    public Booking(ObjectId id, String name, String reference) {
        super();

        this.id = id;
        this.name = name;
        this.reference = reference;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
