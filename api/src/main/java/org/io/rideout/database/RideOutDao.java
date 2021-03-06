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
        RideOut result = collection.aggregate(getPipe(id)).first();

        if (result != null && result.getRiders().size() == 1 && result.getRiders().get(0).getId() == null) {
            result.setRiders(new ArrayList<>());
        }

        return result;
    }

    public ArrayList<RideOut> search(String name, FilterBean filters) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ArrayList<RideOut> result = new ArrayList<>();
        collection.aggregate(getPipe(name, filters)).forEach((Consumer<RideOut>) result::add);

        for (RideOut ride : result) {
            if (ride.getRiders().size() == 1 && ride.getRiders().get(0).getId() == null) {
                ride.setRiders(new ArrayList<>());
            }
        }

        return result;
    }

    public RideOut insert(RideOut rideout) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);
        ObjectId id = rideout.getId();
        rideout.setId(id);
        rideout.getRiders().clear();
        rideout.setLeadRider(null);

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

        RideOut rideout = getById(id);
        return collection.deleteOne(eq("_id", id)).getDeletedCount() == 1 ? rideout : null;
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

    public RideOut updateLeadRider(ObjectId id, SimpleUser lead, Vehicle vehicle) {
        MongoCollection<RideOut> collection = Database.getInstance().getCollection(Database.RIDEOUT_COLLECTION, RideOut.class);

        UpdateResult result = collection.updateOne(
                eq("_id", id),
                combine(
                        set("leadRider.staff", lead.getId()),
                        set("leadRider.vehicle", vehicle.getId())
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
                set("route", rideout.getRoute()),
                set("isPublished", rideout.isPublished()),
                set("minCancellationDate", rideout.getMinCancellationDate())
        );
    }

    private ArrayList<Bson> getPipe() {
        return getPipe(null, null, null);
    }

    private ArrayList<Bson> getPipe(ObjectId id) {
        return getPipe(id, null, null);
    }

    private ArrayList<Bson> getPipe(String name, FilterBean filter) {
        return getPipe(null, name, filter);
    }

    private ArrayList<Bson> getPipe(ObjectId id, String name, FilterBean filter) {
        ArrayList<Bson> pipe = new ArrayList<>();

        if (id != null) pipe.add(match(eq("_id", id)));
        if (name != null) pipe.add(match(regex("name", name, "i")));
        if (filter != null) pipe.addAll(getFilterPipe(filter));

        UnwindOptions unwindOptions = new UnwindOptions();
        unwindOptions.preserveNullAndEmptyArrays(true);

        pipe.addAll(asList(
                addFields(new Field<>("leadVehicle", "$leadRider.vehicle")),
                lookup(Database.USER_COLLECTION, "leadRider.staff", "_id", "leadRider"),
                unwind("$leadRider", unwindOptions),
                lookup(Database.VEHICLE_COLLECTION, "leadVehicle", "_id", "leadRider.vehicle"),
                unwind("$leadRider.vehicle", unwindOptions),
                project(exclude("leadRider.riderInformation", "leadRider.password")),
                unwind("$riders", unwindOptions),
                addFields(new Field<>("vehicle", "$riders.vehicle")),
                lookup("users", "riders.rider", "_id", "rider"),
                unwind("$rider", unwindOptions),
                project(exclude("riders", "rider.riderInformation", "rider.password")),
                lookup("vehicles", "vehicle", "_id", "vehicle"),
                unwind("$vehicle", unwindOptions),
                addFields(new Field<>("rider.vehicle", "$vehicle")),
                project(exclude("vehicle", "leadVehicle")),
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
