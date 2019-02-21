package org.io.rideout.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.io.rideout.PasswordManager;
import org.io.rideout.model.*;

import java.util.ArrayList;
import java.util.Date;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class TestDatabase {
    private static TestDatabase ourInstance = new TestDatabase();
    public static TestDatabase getInstance() {
        return ourInstance;
    }

    public static final ObjectId GET_RIDER = new ObjectId("5c6ec378b1a052279dbbf710");
    public static final ObjectId GET_STAFF = new ObjectId("5c6ec378b1a052279dbbf711");
    public static final ObjectId PUT_STAFF = new ObjectId("5c6ec378b1a052279dbbf712");
    public static final ObjectId DELETE_RIDER = new ObjectId("5c6ec378b1a052279dbbf713");
    public static final ObjectId VEHICLE_RIDER = new ObjectId("5c6ec4762008840f8e405829");
    public static final ObjectId GET_VEHICLE = new ObjectId("5c6ec378b1a052279dbbf714");
    public static final ObjectId PUT_VEHICLE = new ObjectId("5c6ec378b1a052279dbbf715");
    public static final ObjectId DELETE_VEHICLE = new ObjectId("5c6ec378b1a052279dbbf716");

    public static final ObjectId GET_RIDEOUT = new ObjectId("5c6ecf7bd759b5053e6e3b29");
    public static final ObjectId GET_STAYOUT = new ObjectId("5c6ecf7bd759b5053e6e3b2a");
    public static final ObjectId GET_TOUROUT = new ObjectId("5c6ecf7bd759b5053e6e3b2b");
    public static final ObjectId ADD_RIDER_RIDEOUT = new ObjectId("5c6ee075e45bfd32651c69f5");
    public static final ObjectId REMOVE_RIDER_RIDEOUT = new ObjectId("5c6ee075e45bfd32651c69f6");
    public static final ObjectId PUT_RIDEOUT = new ObjectId("5c6ecf7bd759b5053e6e3b2d");
    public static final ObjectId DELETE_RIDEOUT = new ObjectId("5c6ecf7bd759b5053e6e3b2e");
    public static final ObjectId BOOKING_1 = new ObjectId("5c6ed0717f47612dd4cd1b10");
    public static final ObjectId BOOKING_2 = new ObjectId("5c6ed0717f47612dd4cd1b11");
    public static final ObjectId BOOKING_3 = new ObjectId("5c6ed0717f47612dd4cd1b12");

    private TestDatabase() { }

    public static void setUp() {
        // Create connection to database
        String connectionString = "mongodb+srv://test:test@rideout-9fffk.gcp.mongodb.net/rideout?retryWrites=true";
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString(connectionString))
                .build();

        MongoClient client = MongoClients.create(settings);
        MongoDatabase database = client.getDatabase("test");

        // Force the system to use test database
        Database.getInstance().setDatabase(database);
        insertDummyData(database);
    }

    public static void tearDown() {
        MongoDatabase database = Database.getInstance().getDatabase();
        database.getCollection("rideouts").drop();
        database.getCollection("users").drop();
    }

    private static void insertDummyData(MongoDatabase database) {
        insertDummyUsers(database.getCollection("users", User.class));
        insertDummyRideOuts(database.getCollection("rideouts", RideOut.class));
    }

    private static void insertDummyRideOuts(MongoCollection<RideOut> collection) {
        ArrayList<RideOut> rideOuts = new ArrayList<>();
        rideOuts.add(getDummyRideOut(GET_RIDEOUT));
        rideOuts.add(getDummyStayOut(GET_STAYOUT));
        rideOuts.add(getDummyTourOut(GET_TOUROUT));
        rideOuts.add(getDummyTourOut(ADD_RIDER_RIDEOUT));
        rideOuts.add(getDummyTourOut(REMOVE_RIDER_RIDEOUT));
        rideOuts.add(getDummyTourOut(PUT_RIDEOUT));
        rideOuts.add(getDummyRideOut(DELETE_RIDEOUT));

        collection.insertMany(rideOuts);
    }

    private static void insertDummyUsers(MongoCollection<User> collection) {
        ArrayList<User> users = new ArrayList<>();
        users.add(getDummyRider(GET_RIDER, GET_VEHICLE)); // GET RIDER
        users.add(getDummyStaff(GET_STAFF)); // GET STAFF
        users.add(getDummyRider(VEHICLE_RIDER, GET_VEHICLE, PUT_VEHICLE, DELETE_VEHICLE)); // VEHICLE RIDER
        users.add(getDummyStaff(PUT_STAFF)); // PUT STAFF
        users.add(getDummyRider(DELETE_RIDER, GET_VEHICLE)); // DELETE RIDER

        collection.insertMany(users);
    }

    private static User getDummyRider(ObjectId uid, ObjectId... vids) {
        User dummy = new User(
                uid,
                "jsmith",
                PasswordManager.hashPassword("john123"),
                User.RIDER,
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                new RiderInformation(
                        "999",
                        true,
                        "A"
                )
        );
        for (ObjectId vid : vids) {
            dummy.getRiderInformation().addVehicle(new Vehicle(vid, "Honda", vid.equals(PUT_VEHICLE) ? "Bear" : "Monkey", 125, "REG123"));
        }

        return dummy;
    }

    private static User getDummyStaff(ObjectId id) {
        return new User(
                id,
                "jsmith",
                PasswordManager.hashPassword("john123"),
                User.STAFF,
                id.equals(PUT_STAFF) ? "Jack" : "John",
                "Smith",
                new Date(100),
                "07491012345",
                null
        );
    }

    private static RideOut getDummyRideOut(ObjectId id) {
        RideOut dummy = new RideOut(
                id,
                "Ride around the candovers",
                new Date(100),
                new Date(100),
                id.equals(PUT_RIDEOUT) ? 30 : 15,
                "54321",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(100)

        );

        if (id.equals(REMOVE_RIDER_RIDEOUT)) {
            dummy.getRiders().add(getDummyRider(GET_RIDER, GET_VEHICLE));
        }

        return dummy;
    }

    private static StayOut getDummyStayOut(ObjectId id) {
        StayOut dummy = new StayOut(
                id,
                "Stay around the candovers",
                new Date(200),
                new Date(200),
                10,
                "1234",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(200)
        );
        Booking accommodation = new Booking(BOOKING_1, "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking(BOOKING_2, "KFC", "");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        return dummy;
    }

    private static TourOut getDummyTourOut(ObjectId id) {
        TourOut dummy = new TourOut(
                id,
                "Tour around the candovers",
                new Date(300),
                new Date(300),
                5,
                "2345",
                "https://www.walkhighlands.co.uk/skye/profiles/marsco.gpx",
                new Date(300)
        );
        Booking accommodation = new Booking(BOOKING_1, "Marriot Hotel", "ABCDE");
        Booking restaurant = new Booking(BOOKING_2, "KFC", "");
        Booking travel = new Booking(BOOKING_3, "Condor Ferries", "QWERTY");
        dummy.addAccommodation(accommodation);
        dummy.addRestaurant(restaurant);
        dummy.addTravelBooking(travel);
        return dummy;
    }
}
