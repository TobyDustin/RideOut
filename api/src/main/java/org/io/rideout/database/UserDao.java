package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

public class UserDao {
    private static UserDao ourInstance = new UserDao();
    public static UserDao getInstance() {
        return ourInstance;
    }

    private UserDao() {
    }

    public ArrayList<User> getAll() {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        List<Bson> pipe = Collections.singletonList(
                lookup(Database.VEHICLE_COLLECTION, "riderInformation.vehicles", "_id", "riderInformation.vehicles")
        );

        ArrayList<User> result = new ArrayList<>();
        collection.aggregate(pipe).forEach((Consumer<User>) result::add);
        return result;
    }

    public User getById(ObjectId id) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        List<Bson> pipe = Arrays.asList(
                match(eq("_id", id)),
                lookup(Database.VEHICLE_COLLECTION, "riderInformation.vehicles", "_id", "riderInformation.vehicles")
        );

        return collection.aggregate(pipe).first();
    }

    public User getByUsername(String username) {
        username = username.toLowerCase();
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        List<Bson> pipe = Arrays.asList(
                match(eq("username", username)),
                lookup(Database.VEHICLE_COLLECTION, "riderInformation.vehicles", "_id", "riderInformation.vehicles")
        );

        return collection.aggregate(pipe).first();
    }

    public User insert(User user) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        ObjectId id = user.getId();
        user.setUsername(user.getUsername().toLowerCase());
        collection.insertOne(user);
        return getById(id);
    }

    public User update(User user) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        ObjectId id = user.getId();

        UpdateResult result = collection.updateOne(eq("_id", id),
                user.getRiderInformation() == null ? getUpdateUser(user) : getUpdateUserWithRiderInfo(user));

        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public User delete(ObjectId id) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        User staff = getById(id);

        if (staff != null) {
            DeleteResult result = collection.deleteOne(eq("_id", id));
            if (result.getDeletedCount() == 1) return staff;
        }

        return null;
    }

    private Bson getUpdateUser(User user) {
        return combine(
                set("username", user.getUsername().toLowerCase()),
                set("password", user.getPassword()),
                set("firstName", user.getFirstName()),
                set("lastName", user.getLastName()),
                set("dateOfBirth", user.getDateOfBirth()),
                set("contactNumber", user.getContactNumber()),
                unset("riderInformation")
        );
    }

    private Bson getUpdateUserWithRiderInfo(User user) {
        RiderInformation info = user.getRiderInformation();
        return combine(
                set("username", user.getUsername().toLowerCase()),
                set("password", user.getPassword()),
                set("firstName", user.getFirstName()),
                set("lastName", user.getLastName()),
                set("dateOfBirth", user.getDateOfBirth()),
                set("contactNumber", user.getContactNumber()),
                set("riderInformation.emergencyContactNumber", info.getEmergencyContactNumber()),
                set("riderInformation.isInsured", info.isInsured()),
                set("riderInformation.license", info.getLicense())
        );
    }
}
