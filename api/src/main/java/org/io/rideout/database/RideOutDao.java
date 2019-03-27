package org.io.rideout.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.io.rideout.model.*;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;

public class RideOutDao {
    private static RideOutDao ourInstance = new RideOutDao();
    public static RideOutDao getInstance() {
        return ourInstance;
    }

    private RideOutDao() {}

    public ArrayList<RideOut> getAll(FilterBean filter) {
        return search(null, filter);
    }

    public RideOut getById(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        return collection.aggregate(getPipe()).first();
    }

    public ArrayList<RideOut> search(String name, FilterBean filters) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ArrayList<RideOut> result = new ArrayList<>();
        collection.aggregate(getPipe(name, filters)).forEach((Consumer<RideOut>) result::add);

        return result;
    }

    public RideOut insert(RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ObjectId id = rideout.getId();
        rideout.setId(id);
        rideout.getRiders().clear();

        collection.insertOne(rideout);
        return getById(id);
    }

    public RideOut update(RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ObjectId id = rideout.getId();

        UpdateResult result = collection.updateOne(eq("_id", id), getRideOutUpdate(rideout));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut delete(ObjectId id) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        return collection.findOneAndDelete(eq("_id", id));
    }

    public RideOut addRider(ObjectId id, SimpleUser rider, Vehicle vehicle) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(
                eq("_id", id),
                addToSet("riders", new Document()
                        .append("rider", rider.getId())
                        .append("vehicle", vehicle.getId())
                )
        );
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    public RideOut removeRider(ObjectId id, SimpleUser rider) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(eq("_id", id), pull("riders", eq("rider", rider.getId())));
        return result.getModifiedCount() == 1 ? getById(id) : null;
    }

    private String getTypeName(String type) {
        switch (type) {
            case RideOut.RIDE: return RideOut.class.getName();
            case RideOut.STAY: return StayOut.class.getName();
            case RideOut.TOUR: return TourOut.class.getName();
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

    private ArrayList<Bson> getPipe() {
        return getPipe(null, null);
    }

    private ArrayList<Bson> getPipe(String name, FilterBean filter) {
        ArrayList<Bson> pipe = new ArrayList<>();

        if (name != null) pipe.add(match(regex("name", name, "i")));
        if (filter != null) pipe.addAll(getFilterPipe(filter));

        UnwindOptions unwindOptions = new UnwindOptions();
        unwindOptions.preserveNullAndEmptyArrays(true);

        pipe.addAll(asList(
                unwind("$riders", unwindOptions),
                addFields(new Field<>("vehicle", "$riders.vehicle")),
                lookup("users", "riders.rider", "_id", "rider"),
                unwind("$rider", unwindOptions),
                project(exclude("riders", "rider.riderInformation", "rider.password")),
                lookup("vehicles", "vehicle", "_id", "vehicle"),
                unwind("$vehicle", unwindOptions),
                addFields(new Field<>("rider.vehicle", "$vehicle")),
                project(exclude("vehicle")),
                group("$_id",
                        first("_t", "$_t"),
                        first("name", "$name"),
                        first("dateStart", "$dateStart"),
                        first("dateEnd", "$dateEnd"),
                        first("maxRiders", "$maxRiders"),
                        first("leadRider", "$leadRider"),
                        first("route", "$route"),
                        first("isPublished", "$isPublished"),
                        first("minCancellationDate", "$minCancellationDate"),
                        first("checkpoints", "$checkpoints"),
                        Accumulators.addToSet("riders", "$rider"),
                        first("accommodationList", "$accommodationList"),
                        first("restaurantList", "$restaurantList"),
                        first("travelBookings", "$travelBookings")
                )
        ));

        return pipe;
    }

    private ArrayList<Bson> getFilterPipe(FilterBean filter) {
        ArrayList<Bson> pipe = new ArrayList<>();

        if (filter.showOnlyVacant) {
            pipe.add(addFields(
                    new Field<>("count", new Document("$size", "$riders")),
                    new Field<>("full", new Document("$cmp", asList("$maxRiders", "$count")))
            ));
            pipe.add(match(eq("full", 1)));
        }

        if (filter.showOnlyUsers) {
            pipe.add(match(
                in("riders", eq("_id", new ObjectId(filter.securityContext.getUserPrincipal().getName())))
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
