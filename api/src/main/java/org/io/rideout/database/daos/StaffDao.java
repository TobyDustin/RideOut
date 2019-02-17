package org.io.rideout.database.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
import org.io.rideout.database.Database;
import org.io.rideout.model.Staff;

public class StaffDao {
    private static StaffDao ourInstance = new StaffDao();
    public static StaffDao getInstance() {
        return ourInstance;
    }

    private StaffDao() {
    }

    public void insert(Staff staff) {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<Staff> collection = database.getCollection("users", Staff.class);

        Document doc = new Document()
                .append("username", staff.getUsername())
                .append("password", PasswordManager.hashPassword(staff.getPassword()))
                .append("firstName", staff.getFirstName())
                .append("lastName", staff.getLastName())
                .append("dateOfBirth", staff.getDateOfBirth())
                .append("contactNumber", staff.getContactNumber())
                .append("isAdmin", staff.isAdmin());
        collection.insertOne(staff);
    }
}
