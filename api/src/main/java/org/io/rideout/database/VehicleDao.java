package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.io.rideout.model.User;
import org.io.rideout.model.Vehicle;

import java.util.ArrayList;

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
        MongoCollection<Vehicle> vehicleCollection = Database.getInstance().getCollection(Database.VEHICLE_COLLECTION, Vehicle.class);
        MongoCollection<User> userCollection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        ObjectId id = vehicle.getId();

        vehicleCollection.insertOne(vehicle);

        UpdateResult result = userCollection.updateOne(eq("_id", userId), combine(
                addToSet("riderInformation.vehicles", vehicle.getId()))
        );

        if (result.getModifiedCount() == 1) {
            return getById(userId, id);
        } else {
            vehicleCollection.deleteOne(eq("_id", vehicle.getId()));
            return null;
        }
    }

    public Vehicle update(Vehicle vehicle) {
        MongoCollection<Vehicle> collection = Database.getInstance().getCollection(Database.VEHICLE_COLLECTION, Vehicle.class);

        UpdateResult result = collection.updateOne(eq("_id", vehicle.getId()), combine(
                set("make", vehicle.getMake()),
                set("model", vehicle.getModel()),
                set("power", vehicle.getPower()),
                set("registration", vehicle.getRegistration()),
                set("checked", vehicle.isChecked())
        ));

        return result.getModifiedCount() == 1 ? collection.find(eq("_id", vehicle.getId())).first() : null;
    }

    public Vehicle delete(ObjectId userId, ObjectId vehicleId) {
        MongoCollection<Vehicle> vehicleCollection = Database.getInstance().getCollection(Database.VEHICLE_COLLECTION, Vehicle.class);
        MongoCollection<User> userCollection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        Vehicle vehicle = getById(userId, vehicleId);

        if (vehicle != null) {
            DeleteResult deleteResult = vehicleCollection.deleteOne(eq("_id", vehicleId));

            if (deleteResult.getDeletedCount() == 1) {
                UpdateResult updateResult = userCollection.updateOne(eq("_id", userId), combine(
                        pull("riderInformation.vehicles", vehicleId)
                ));
                if (updateResult.getModifiedCount() == 1) return vehicle;
            }
        }

        return null;
    }
}
