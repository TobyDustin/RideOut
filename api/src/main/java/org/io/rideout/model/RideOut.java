package org.io.rideout.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;
import org.io.rideout.helpers.ObjectIdJsonDeserializer;
import org.io.rideout.helpers.ObjectIdJsonSerializer;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@BsonDiscriminator
@JsonTypeName("Ride")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "rideoutType")
@JsonSubTypes({ @JsonSubTypes.Type(value = StayOut.class) })
public class RideOut {

    public static final String RIDE = "ride";
    public static final String STAY = "stay";
    public static final String TOUR = "tour";

    @BsonId
    @NotNull
    @JsonSerialize(using = ObjectIdJsonSerializer.class)
    @JsonDeserialize(using = ObjectIdJsonDeserializer.class)
    @Schema(type = "string")
    private ObjectId id;

    @NotNull
    @Length(min = 5)
    private String name;
    private String description;
    private Date dateStart;
    private Date dateEnd;
    private int maxRiders;
    private SimpleUser leadRider;
    private String route;
    private boolean isPublished = false;
    private Date minCancellationDate;
    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();
    private ArrayList<SimpleUser> riders = new ArrayList<>();
    private ArrayList<Booking> restaurantList = new ArrayList<>();

    public RideOut() {}

    public RideOut(ObjectId id,String name,Date dateStart,Date dateEnd,int maxRiders,SimpleUser leadRider, String route, Date minCancellationDate) {
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.maxRiders = maxRiders;
        this.leadRider = leadRider;
        this.route = route;
        this.minCancellationDate = minCancellationDate;
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

    public SimpleUser getLeadRider() {
        return leadRider;
    }

    public void setLeadRider(SimpleUser leadRider) {
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

    public ArrayList<SimpleUser> getRiders() {
        return riders;
    }

    public void setRiders(ArrayList<SimpleUser> riders) {
        this.riders = riders;
    }

    public ArrayList<Booking> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<Booking> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public void addRestaurant(Booking restaurant) {
        this.restaurantList.add(restaurant);
    }
}
