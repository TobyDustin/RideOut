package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
import org.io.rideout.model.Staff;

import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class StaffDao {
    private static StaffDao ourInstance = new StaffDao();
    public static StaffDao getInstance() {
        return ourInstance;
    }

    private StaffDao() {
    }

    public ArrayList<Staff> getAll() {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<Staff> collection = database.getCollection("users", Staff.class);

        ArrayList<Staff> result = new ArrayList<>();
        collection.find(eq("_t", Staff.class.getName()), Staff.class).forEach((Consumer<Staff>) result::add);
        return result;
    }

    public Staff getById(ObjectId id) {
        MongoCollection<Staff> collection = Database.getInstance().getDatabase().getCollection("users", Staff.class);

        return collection.find(eq("_id", id)).first();
    }

    public void insert(Staff staff) {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<Staff> collection = database.getCollection("users", Staff.class);
        collection.insertOne(staff);
    }

    public Staff update(ObjectId id, Staff staff) {
        MongoCollection<Staff> collection = Database.getInstance().getDatabase().getCollection("users", Staff.class);

        UpdateResult result = collection.updateOne(eq("_id", id), combine(
                set("username", staff.getUsername()),
                set("password", PasswordManager.hashPassword(staff.getPassword())),
                set("firstName", staff.getFirstName()),
                set("lastName", staff.getLastName()),
                set("dateOfBirth", staff.getDateOfBirth()),
                set("contactNumber", staff.getContactNumber()),
                set("admin", staff.isAdmin())
        ));

        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public Staff delete(ObjectId id) {
        MongoCollection<Staff> collection = Database.getInstance().getDatabase().getCollection("users", Staff.class);

        Staff staff = getById(id);

        if (staff != null) {
            DeleteResult result = collection.deleteOne(eq("_id", id));
        }

        return staff;
    }
}
