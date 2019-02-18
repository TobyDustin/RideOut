package org.io.rideout.database;

import org.io.rideout.model.Staff;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

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

    //@Test
    public void testGetAllStaff() {
        ArrayList<Staff> result = StaffDao.getInstance().getAll();
        assertEquals(1, result.size());
    }
}
