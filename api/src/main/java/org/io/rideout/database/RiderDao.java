package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import org.io.rideout.model.Rider;

public class RiderDao {
    private static RiderDao ourInstance = new RiderDao();
    public static RiderDao getInstance() {
        return ourInstance;
    }

    private RiderDao() {
    }

    public void insert(Rider rider) {
        MongoCollection<Rider> collection = Database.getInstance().getDatabase().getCollection("users", Rider.class);
        collection.insertOne(rider);
    }
}
