package org.io.rideout.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;


public class RideOut {

    private String id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private int maxRiders;
    private String leadRider;
    private String route;
    private boolean isPublished;
    private Date minCancellationDate;
    private ArrayList<Checkpoint> checkpoints;
    private ArrayList<Rider> riders;


    public RideOut(String id,String name,Date dateStart,Date dateEnd,int maxRiders,String leadRider, String route, Date minCancellationDate){
        this.id = id;
        this.name = name;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.maxRiders = maxRiders;
        this.leadRider = leadRider;
        this.isPublished = false;
        this.minCancellationDate = minCancellationDate;
    }

    //***
    //  START OF GETTERS AND SETTERS
    //***

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<Rider> getRiders() {
        return riders;
    }

    public void setRiders(ArrayList<Rider> riders) {
        this.riders = riders;
    }
}
