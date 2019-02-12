package org.io.rideout.model;

public class Booking {

    private String id;
    private String name;
    private String reference;

    public Booking() {
        super();
    }

    public Booking(String id, String name, String reference) {
        super();

        this.id = id;
        this.name = name;
        this.reference = reference;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
