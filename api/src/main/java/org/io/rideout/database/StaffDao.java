package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.io.rideout.model.Staff;

import java.util.ArrayList;

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
        for (Staff s : collection.find()) {
            result.add(s);
        }
        return result;
    }

    public void insert(Staff staff) {
        MongoDatabase database = Database.getInstance().getDatabase();
        MongoCollection<Staff> collection = database.getCollection("users", Staff.class);
        collection.insertOne(staff);
    }
}
