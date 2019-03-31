package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.io.rideout.model.Checkpoint;
import org.io.rideout.model.RideOut;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.set;

public class CheckpointDao {
    private static CheckpointDao ourInstance = new CheckpointDao();
    public static CheckpointDao getInstance() {
        return ourInstance;
    }

    private RideOutDao rideoutDao;

    private CheckpointDao() {
        rideoutDao = RideOutDao.getInstance();
    }

    public ArrayList<Checkpoint> getAll(ObjectId rideoutId) {
        RideOut rideout = rideoutDao.getById(rideoutId);
        if (rideout == null) return null;

        return rideout.getCheckpoints();
    }

    public  Checkpoint getById(ObjectId rideoutId, ObjectId checkpoinId) {
        RideOut rideout = rideoutDao.getById(rideoutId);
        if (rideout == null) return null;

        for (Checkpoint c : rideout.getCheckpoints()) {
            if (c.getId().equals(checkpoinId)) {
                return c;
            }
        }

        return null;
    }

    public Checkpoint insert(ObjectId rideoutId, Checkpoint checkpoint) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", rideoutId), addToSet("checkpoints", combine(
                set("_id", checkpoint.getId()),
                set("name", checkpoint.getName()),
                set("lat", checkpoint.getLat()),
                set("lon", checkpoint.getLon()),
                set("description", checkpoint.getDescription())
        )));
        return result.getModifiedCount() == 1 ? getById(rideoutId, checkpoint.getId()) : null;
    }

    public Checkpoint update(ObjectId rideoutId, Checkpoint checkpoint) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(and(
                eq("_id", rideoutId), eq("checkpoints._id", checkpoint.getId())), combine(
                set("checkpoints.$.name", checkpoint.getName()),
                set("checkpoints.$.lat", checkpoint.getLat()),
                set("checkpoints.$.lon", checkpoint.getLon()),
                set("checkpoints.$.description", checkpoint.getDescription())
        ));
        return result.getModifiedCount() == 1 ? getById(rideoutId, checkpoint.getId()) : null;
    }

    public Checkpoint delete(ObjectId rideoutId, ObjectId checkpointId) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        Checkpoint checkpoint = getById(rideoutId, checkpointId);
        if (checkpoint == null) return null;

        UpdateResult result = collection.updateOne(eq("_id", rideoutId), pull("checkpoints", eq("_id", checkpointId)));
        return result.getModifiedCount() == 1 ? checkpoint : null;
    }
}
