package org.io.rideout.model;

public class Checkpoint {
    private String id;
    private String name;
    private double lat;
    private double lon;
    private String description;


    public Checkpoint(String id, String name,double lat,double lon, String description){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this. description = description;
    }

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
