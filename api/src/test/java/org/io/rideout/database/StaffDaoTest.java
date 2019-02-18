package org.io.rideout.database;

import org.bson.types.ObjectId;
import org.io.rideout.model.Staff;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class StaffDaoTest {

    //@Test
    public void insertStaffTest() {
        StaffDao dao = StaffDao.getInstance();
        Staff dummy = new Staff(
                new ObjectId("5c6aa820fb08e869dfc548da"),
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

    //@Test
    public void testGetStaffByIdSuccess() {
        Staff result = StaffDao.getInstance().getById(new ObjectId("5c6aa820fb08e869dfc548da"));
        assertNotNull(result);
    }

    //@Test
    public void testGetStaffByIdNotFound() {
        Staff result = StaffDao.getInstance().getById(new ObjectId());
        assertNull(result);
    }

    //@Test
    public void testUpdateStaffSuccess() {
        StaffDao dao = StaffDao.getInstance();
        Staff dummy = new Staff(
                new ObjectId("5c6aa820fb08e869dfc548da"),
                "jsmith",
                "jake123",
                "Jake",
                "Smith",
                new Date(100),
                "07491012345",
                false
        );
        assertNotNull(dao.update(dummy.getId(), dummy));
    }

    //@Test
    public void testUpdateStaffNotFound() {
        StaffDao dao = StaffDao.getInstance();
        Staff dummy = new Staff(
                new ObjectId(),
                "jsmith",
                "jake123",
                "Jake",
                "Smith",
                new Date(100),
                "07491012345",
                false
        );
        assertNull(dao.update(dummy.getId(), dummy));
    }

    //@Test
    public void testDeleteStaffSuccess() {
        StaffDao dao = StaffDao.getInstance();
        assertNotNull(dao.delete(new ObjectId("5c6aa820fb08e869dfc548da")));
    }

    //@Test
    public void testDeletStaffNotFound() {
        StaffDao dao = StaffDao.getInstance();
        assertNull(dao.delete(new ObjectId()));
    }
}
