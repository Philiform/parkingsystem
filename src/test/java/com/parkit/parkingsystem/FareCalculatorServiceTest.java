package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.TimesDuration;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private Date inTime;
    private Date outTime;
    private static final ParkingSpot parkingSpotCar = new ParkingSpot(1, ParkingType.CAR, false);
    private static final ParkingSpot parkingSpotBike = new ParkingSpot(1, ParkingType.BIKE, false);
    private static final ParkingSpot parkingSpotNull = new ParkingSpot(1, null, false);

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        inTime = new Date();
    }

    @Test
    public void calculateFareCar(){
        outTime = new Date(inTime.getTime() + TimesDuration._1_HEURE);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotCar);
        
        fareCalculatorService.calculateFare(ticket);
        
        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareBike(){
        outTime = new Date(inTime.getTime() + TimesDuration._1_HEURE);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotBike);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
    }

    @Test
    public void calculateFareUnkownType(){
        outTime = new Date(inTime.getTime() + TimesDuration._1_HEURE);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotNull);

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        outTime = new Date(inTime.getTime() - TimesDuration._1_HEURE);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotBike);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        outTime = new Date(inTime.getTime() + TimesDuration._45_MINUTES); // 45 minutes parking time should give 3/4th parking fare
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotBike);

        fareCalculatorService.calculateFare(ticket);
        
        assertEquals( (0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        outTime = new Date(inTime.getTime() + TimesDuration._45_MINUTES); // 45 minutes parking time should give 3/4th parking fare
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);
        
        assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        outTime = new Date(inTime.getTime() + TimesDuration._1_JOUR_ET_3_HEURES); // 27 hours parking time should give 27 * parking fare per hour
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);
        
        assertEquals( (27 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWith40Minutes(){
        outTime = new Date(inTime.getTime() + TimesDuration._40_MINUTES); // 40 minutes parking time should give 2/3th parking fare
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);
        
        assertEquals( (0.67 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }
}
