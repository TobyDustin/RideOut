package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.io.rideout.model.Payment;
import org.io.rideout.model.User;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.pull;

public class PaymentDao {
    private static PaymentDao ourInstance = new PaymentDao();
    public static PaymentDao getInstance() { return ourInstance; }

    private PaymentDao() {}

    public ArrayList<Payment> getAll(ObjectId userId) {
        User user = UserDao.getInstance().getById(userId);

        if (user == null) return null;
        else if (user.getRiderInformation() == null) return new ArrayList<>();
        return user.getRiderInformation().getPayments();
    }

    public Payment getById(ObjectId userId, ObjectId paymentId) {
        ArrayList<Payment> payments = getAll(userId);

        if (payments == null || payments.isEmpty()) return null;

        for (Payment p : payments) {
            if (p.getId().equals(paymentId)) {
                return p;
            }
        }

        return null;
    }

    public Payment insert(ObjectId userId, Payment payment) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        ObjectId id = new ObjectId();
        payment.setId(id);

        UpdateResult result = collection.updateOne(eq("_id", userId),
                addToSet("riderInformation.payments", payment));
        return result.getModifiedCount() == 1 ? getById(userId, id) : null;
    }

    public Payment update(ObjectId userId, ObjectId paymentId, Payment payment) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);

        if (delete(userId, payment.getId()) == null) return null;

        UpdateResult result = collection.updateOne(eq("_id", userId),
                addToSet("riderInformation.payments", payment));
        return result.getModifiedCount() == 1 ? getById(userId, paymentId) : null;
    }

    public Payment delete(ObjectId userId, ObjectId paymentId) {
        MongoCollection<User> collection = Database.getInstance().getCollection(Database.USER_COLLECTION, User.class);
        Payment payment = getById(userId, paymentId);

        if (payment != null) {
            UpdateResult result = collection.updateOne(eq("_id", userId),
                    pull("riderInformation.payments", eq("_id", paymentId)));
            return result.getModifiedCount() == 1 ? payment : null;
        }

        return null;
    }
}
