package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class VehicleDao {
    private static VehicleDao ourInstance = new VehicleDao();
    public static VehicleDao getInstance() {
        return ourInstance;
    }

    private VehicleDao() {}

    public ArrayList<Vehicle> getAll(ObjectId userId) {
        User user = UserDao.getInstance().getById(userId);

        if (user == null) return null;
        else if (user.getRiderInformation() == null) return new ArrayList<>();
        return user.getRiderInformation().getVehicles();
    }

    public Vehicle getById(ObjectId userId, ObjectId vehicleId) {
        ArrayList<Vehicle> vehicles = getAll(userId);

        for (Vehicle v : vehicles) {
            if (v.getId().equals(vehicleId)) {
                return v;
            }
        }

        return null;
    }

    public Vehicle insert(ObjectId userId, Vehicle vehicle) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        ObjectId id = new ObjectId();
        vehicle.setId(id);

        UpdateResult result = collection.updateOne(eq("_id", userId), combine(
                addToSet("riderInformation.vehicles", vehicle))
        );

        return result.getModifiedCount() == 1 ? getById(userId, id) : null;
    }

    public Vehicle update(ObjectId userId, ObjectId vehicleId, Vehicle vehicle) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        if (delete(userId, vehicleId) == null) return null;

        UpdateResult result = collection.updateOne(eq("_id", userId), combine(
                addToSet("riderInformation.vehicles", vehicle)
        ));

        return result.getModifiedCount() == 1 ? getById(userId, vehicleId) : null;
    }

    public Vehicle delete(ObjectId userId, ObjectId vehicleId) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        Vehicle vehicle = getById(userId, vehicleId);

        if (vehicle != null) {
            UpdateResult result = collection.updateOne(eq("_id", userId), combine(
                    pull("riderInformation.vehicles", eq("_id", vehicleId))
            ));
            if (result.getModifiedCount() == 1) return vehicle;
        }

        return null;
    }
}
