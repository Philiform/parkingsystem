package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotTest {

    private static ParkingSpot parkingSpot;
    
    @BeforeEach
    private void setUpPerTest() {
    	parkingSpot = new ParkingSpot(9, ParkingType.CAR, true);
    }

    @Test
    public void setIdTest() {
    	parkingSpot.setId(12);
    	int id = parkingSpot.getId();
    	assertEquals(12, id);
    }

    @Test
    public void setParkingTypeTest() {
    	parkingSpot.setParkingType(ParkingType.BIKE);
    	ParkingType parkingType = parkingSpot.getParkingType();
    	assertEquals(ParkingType.BIKE, parkingType);
    }

    @Test
    public void equalsTest() {
    	ParkingSpot parkingSpot2 = new ParkingSpot(9, ParkingType.CAR, true);
    	boolean parkingSpotEquals = parkingSpot.equals(parkingSpot2);
    	assertEquals(true, parkingSpotEquals);
    }

    @Test
    public void equalsItSelfTest() {
    	boolean parkingSpotEquals = parkingSpot.equals(parkingSpot);
    	assertEquals(true, parkingSpotEquals);
    }

    @Test
    public void noEqualsTest() {
    	ParkingSpot parkingSpot2 = new ParkingSpot(12, ParkingType.CAR, true);
    	boolean parkingSpotEquals = parkingSpot.equals(parkingSpot2);
    	assertEquals(false, parkingSpotEquals);
    }
    
    @Test
    public void EqualsNullTest() {
    	ParkingSpot parkingSpot2 = null;
    	boolean parkingSpotEquals = parkingSpot.equals(parkingSpot2);
    	assertEquals(false, parkingSpotEquals);
    }
    
    @Test
    public void hashCodeTest() {
    	int hashCode = parkingSpot.hashCode();
    	assertEquals(9, hashCode);
    }
}
