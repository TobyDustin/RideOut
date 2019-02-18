package org.io.rideout.database;

import org.bson.types.ObjectId;
import org.io.rideout.model.Rider;
import org.io.rideout.model.Vehicle;

import java.util.Date;

public class RiderDaoTest {

    //@Test
    public void insertRiderTest() {
        Rider dummy = new Rider(
                new ObjectId(),
                "jsmith",
                "john123",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                "999",
                true,
                false,
                "A"
        );
        dummy.addVehicle(new Vehicle(new ObjectId(), "Honda", "Monkey", 125, "REG123"));
        dummy.addVehicle(new Vehicle(new ObjectId(), "Suzuki", "GSXR", 1000, "REG987"));
        RiderDao.getInstance().insert(dummy);
    }
}
