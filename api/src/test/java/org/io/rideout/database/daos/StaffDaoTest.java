package org.io.rideout.database.daos;

import org.io.rideout.database.Database;
import org.io.rideout.model.Staff;
import org.junit.Test;

import java.util.Date;

public class StaffDaoTest {

    //@Test
    public void insertStaffTest() {
        StaffDao dao = StaffDao.getInstance();
        Staff dummy = new Staff(
                null,
                "jsmith",
                "john123",
                "John",
                "Smith",
                new Date(100),
                "07491012345",
                false
        );
        dao.insert(dummy);
    }
}
