package org.io.rideout.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Checkpoint {

    @BsonId
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    @Schema(type = "string")
    private ObjectId id;

    @NotNull
    private String name;

    @NotNull
    @Max(value = 90)
    @Min(value = -90)
    private double lat;


    @NotNull
    @Max(value = 180)
    @Min(value = -180)
    private double lon;
    private String description;

    public Checkpoint() {}

    public Checkpoint(ObjectId id, String name,double lat,double lon, String description){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this. description = description;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
