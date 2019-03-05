package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import java.util.ArrayList;
import java.util.Date;

@BsonDiscriminator
@JsonTypeName("Ride")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "rideoutType")
@JsonSubTypes({ @JsonSubTypes.Type(value = StayOut.class) })
public class RideOut {

    @BsonId
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    private ObjectId id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private int maxRiders;
    private String leadRider;
    private String route;
    private boolean isPublished;
    private Date minCancellationDate;
    private ArrayList<Checkpoint> checkpoints;
    private ArrayList<User> riders;

    @BsonProperty("full")
    private boolean isFull;

    public RideOut() {
        super();
        this.checkpoints = new ArrayList<>();
        this.riders = new ArrayList<>();
    }
    public RideOut(ObjectId id,String name,Date dateStart,Date dateEnd,int maxRiders,String leadRider, String route, Date minCancellationDate){
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.maxRiders = maxRiders;
        this.leadRider = leadRider;
        this.route = route;
        this.isPublished = false;
        this.minCancellationDate = minCancellationDate;

        this.checkpoints = new ArrayList<>();
        this.riders = new ArrayList<>();
    }

    //***
    //  START OF GETTERS AND SETTERS
    //***

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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getMaxRiders() {
        return maxRiders;
    }

    public void setMaxRiders(int maxRiders) {
        this.maxRiders = maxRiders;
    }

    public String getLeadRider() {
        return leadRider;
    }

    public void setLeadRider(String leadRider) {
        this.leadRider = leadRider;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public Date getMinCancellationDate() {
        return minCancellationDate;
    }

    public void setMinCancellationDate(Date minCancellationDate) {
        this.minCancellationDate = minCancellationDate;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public ArrayList<User> getRiders() {
        return riders;
    }

    public void setRiders(ArrayList<User> riders) {
        this.riders = riders;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
