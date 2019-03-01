package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.model.RideOut;
import org.io.rideout.model.StayOut;
import org.io.rideout.model.TourOut;
import org.io.rideout.model.User;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class RideOutDao {
    private static RideOutDao ourInstance = new RideOutDao();
    public static RideOutDao getInstance() {
        return ourInstance;
    }

    private RideOutDao() {}

    public ArrayList<RideOut> getAll() {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        ArrayList<RideOut> result = new ArrayList<>();
        collection.find().forEach((Consumer<RideOut>) result::add);
        return result;
    }

    public ArrayList<RideOut> getAllByType(String... filters) {
        if (filters.length == 3) return getAll();
        else if (filters.length == 0) throw new IllegalArgumentException("No filters passed");
        else if (filters.length > 3) throw new IllegalArgumentException("Invalid filter options");

        Bson filter;

        if (filters.length == 1) {
            filter = eq("_t", getTypeName(filters[0]));
        } else {
            filter = or(eq("_t", getTypeName(filters[0])), eq("_t", getTypeName(filters[1])));
        }

        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        ArrayList<RideOut> result = new ArrayList<>();
        collection.find(filter).forEach((Consumer<RideOut>) result::add);
        return result;
    }

    public RideOut getById(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        return collection.find(eq("_id", id)).first();
    }

    public ArrayList<RideOut> search(String name) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        ArrayList<RideOut> result = new ArrayList<>();
        collection.find(regex("name", name)).forEach((Consumer<RideOut>) result::add);

        return result;
    }

    public RideOut insert(RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ObjectId id = new ObjectId();
        rideout.setId(id);

        collection.insertOne(rideout);
        return getById(id);
    }

    public RideOut update(ObjectId id, RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), getRideOutUpdate(rideout));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut delete(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        return collection.findOneAndDelete(eq("_id", id));
    }

    public RideOut addRider(ObjectId id, User rider) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), addToSet("riders", rider));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut removeRider(ObjectId id, User rider) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), pull("riders", eq("_id", rider.getId())));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    private String getTypeName(String type) {
        switch (type) {
            case "ride": return RideOut.class.getName();
            case "stay": return StayOut.class.getName();
            case "tour": return TourOut.class.getName();
            default: throw new IllegalArgumentException("Unknown type name");
        }
    }

    private Bson getRideOutUpdate(RideOut rideout) {
        return combine(
                set("name", rideout.getName()),
                set("dateStart", rideout.getDateStart()),
                set("dateEnd", rideout.getDateEnd()),
                set("maxRiders", rideout.getMaxRiders()),
                set("leadRider", rideout.getLeadRider()),
                set("route", rideout.getRoute()),
                set("isPublished", rideout.isPublished()),
                set("minCancellationDate", rideout.getMinCancellationDate())
        );
    }
}
