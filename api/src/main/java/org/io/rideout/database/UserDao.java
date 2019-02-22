package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
import org.io.rideout.model.RiderInformation;
import org.io.rideout.model.User;

import java.util.ArrayList;
import java.util.function.Consumer;

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

        ArrayList<User> result = new ArrayList<>();
        collection.find(User.class).forEach((Consumer<User>) result::add);
        return result;
    }

    public User getById(ObjectId id) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        return collection.find(eq("_id", id)).first();
    }

    public User getByUsername(String username) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        return collection.find(eq("username", username)).first();
    }

    public User insert(User user) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        ObjectId id = new ObjectId();
        user.setId(id);
        collection.insertOne(user);
        return getById(id);
    }

    public User update(ObjectId id, User user) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

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
                set("username", user.getUsername()),
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
                set("username", user.getUsername()),
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
