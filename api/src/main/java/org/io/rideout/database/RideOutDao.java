package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Field;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.model.*;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class RideOutDao {
    private static RideOutDao ourInstance = new RideOutDao();
    public static RideOutDao getInstance() {
        return ourInstance;
    }

    private RideOutDao() {}

    public ArrayList<RideOut> getAll(FilterBean filter) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        ArrayList<RideOut> result = new ArrayList<>();
        collection.aggregate(getFilterPipe(filter)).forEach((Consumer<RideOut>) result::add);
        return result;
    }

    public RideOut getById(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        return collection.find(eq("_id", id)).first();
    }

    public ArrayList<RideOut> search(String name, FilterBean filters) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        ArrayList<RideOut> result = new ArrayList<>();
        ArrayList<Bson> pipe = getFilterPipe(filters);
        pipe.add(match(regex("name", name, "i")));
        collection.aggregate(pipe).forEach((Consumer<RideOut>) result::add);

        return result;
    }

    public RideOut insert(RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ObjectId id = new ObjectId();
        rideout.setId(id);

        collection.insertOne(rideout);
        return getById(id);
    }

    public RideOut update(ObjectId id, RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), getRideOutUpdate(rideout));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut delete(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        return collection.findOneAndDelete(eq("_id", id));
    }

    public RideOut addRider(ObjectId id, User rider) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), addToSet("riders", rider));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut removeRider(ObjectId id, User rider) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), pull("riders", eq("_id", rider.getId())));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    private String getTypeName(String type) {
        switch (type) {
            case "ride": return RideOut.class.getName();
            case "stay": return StayOut.class.getName();
            case "tour": return TourOut.class.getName();
            default: throw new IllegalArgumentException("Unknown type name");
        }
    }

    private Bson getRideOutUpdate(RideOut rideout) {
        return combine(
                set("name", rideout.getName()),
                set("dateStart", rideout.getDateStart()),
                set("dateEnd", rideout.getDateEnd()),
                set("maxRiders", rideout.getMaxRiders()),
                set("leadRider", rideout.getLeadRider()),
                set("route", rideout.getRoute()),
                set("isPublished", rideout.isPublished()),
                set("minCancellationDate", rideout.getMinCancellationDate())
        );
    }

    private ArrayList<Bson> getFilterPipe(FilterBean filter) {
        ArrayList<Bson> pipe = new ArrayList<>();

        if (filter.showOnlyVacant) {
            pipe.add(addFields(
                    new Field<>("count", new Document("$size", "$riders")),
                    new Field<>("full", new Document("$cmp", Arrays.asList("$maxRiders", "$count")))
            ));
            pipe.add(match(eq("full", 1)));
        }

        if (filter.showOnlyUsers) {
            pipe.add(match(
                in("riders", eq("_id", new ObjectId(filter.securityContext.getUserPrincipal().toString())))
            ));
        }

        if (!filter.types.isEmpty() && filter.types.size() <= 3) {
            ArrayList<Bson> typeFilters = new ArrayList<>();

            for (String t : filter.types) {
                typeFilters.add(eq("_t", getTypeName(t)));
            }

            pipe.add(match(or(typeFilters)));
        } else if (filter.types.size() > 3) {
            throw new BadRequestException();
        }

        return pipe;
    }
}
