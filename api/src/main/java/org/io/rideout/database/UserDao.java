package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
import org.io.rideout.model.User;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UserDao {
    private static UserDao ourInstance = new UserDao();
    public static UserDao getInstance() {
        return ourInstance;
    }

    private UserDao() {
    }

    public ArrayList<User> getAll() {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<User> collection = database.getCollection("users", User.class);

        ArrayList<User> result = new ArrayList<>();
        collection.find(eq("_t", User.class.getName()), User.class).forEach((Consumer<User>) result::add);
        return result;
    }

    public User getById(ObjectId id) {
        MongoCollection<User> collection = Database.getInstance().getDatabase().getCollection("users", User.class);

        return collection.find(eq("_id", id)).first();
    }

    public void insert(User staff) {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<User> collection = database.getCollection("users", User.class);
        collection.insertOne(staff);
    }

    public User update(ObjectId id, User staff) {
        MongoCollection<User> collection = Database.getInstance().getDatabase().getCollection("users", User.class);

        UpdateResult result = collection.updateOne(eq("_id", id), combine(
                set("username", staff.getUsername()),
                set("password", PasswordManager.hashPassword(staff.getPassword())),
                set("firstName", staff.getFirstName()),
                set("lastName", staff.getLastName()),
                set("dateOfBirth", staff.getDateOfBirth()),
                set("contactNumber", staff.getContactNumber())
        ));

        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public User delete(ObjectId id) {
        MongoCollection<User> collection = Database.getInstance().getDatabase().getCollection("users", User.class);

        User staff = getById(id);

        if (staff != null) {
            DeleteResult result = collection.deleteOne(eq("_id", id));
        }

        return staff;
    }
}
