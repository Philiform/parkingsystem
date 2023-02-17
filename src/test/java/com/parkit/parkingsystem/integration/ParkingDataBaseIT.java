package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.VehicleRegNumber;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(VehicleRegNumber.VEHICLE_REG_NUMBER);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

	@Test
	public void testParkingACar() throws Exception {
		// GIVEN
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability

		Ticket ticket = ticketDAO.getTicket(VehicleRegNumber.VEHICLE_REG_NUMBER);
		Date date = new Date(ticket.getInTime().getTime());
		ParkingSpot parkingSpot = ticket.getParkingSpot();
		ParkingType parkingType = ParkingType.CAR;
		boolean available = false;

		Iterable<String> expectedList = new ArrayList<>(
				Arrays.asList("1",
						VehicleRegNumber.VEHICLE_REG_NUMBER,
						"0.0",
						String.format("%tF %<tT", date),
						"1",
						parkingType.toString(),
						String.valueOf(available)));

		// WHEN
		Iterable<String> actualList = new ArrayList<>(
				Arrays.asList(String.valueOf(ticket.getId()),
						ticket.getVehicleRegNumber(),
						String.valueOf(ticket.getPrice()),
						String.format("%tF %<tT", ticket.getInTime()),
						String.valueOf(parkingSpot.getId()),
						parkingSpot.getParkingType().toString(),
						String.valueOf(parkingSpot.isAvailable())));

		// THEN
		verify(inputReaderUtil).readSelection();
		verify(inputReaderUtil).readVehicleRegistrationNumber();
		assertAll(() -> assertIterableEquals(expectedList, actualList));
	}

	@Test
	public void testParkingLotExit() throws Exception {
		// GIVEN
		testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		parkingService.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database

		Date date;
		Ticket ticket = ticketDAO.getTicket(VehicleRegNumber.VEHICLE_REG_NUMBER);

		date = ticket.getOutTime();

		Iterable<String> expectedList = new ArrayList<>(Arrays.asList(
				"0.0",
				String.format("%tF %<tT", date)
				));

		// WHEN
		Iterable<String> actualList = new ArrayList<>(Arrays.asList(
				String.valueOf(ticket.getPrice()),
				String.format("%tF %<tT", ticket.getOutTime())
				));

		// THEN
		assertAll(() -> assertIterableEquals(expectedList, actualList));
	}

}

