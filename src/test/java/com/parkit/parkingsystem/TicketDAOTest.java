package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.VehicleRegNumber;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAOTest {

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private TicketDAO ticketDAO;
	private ParkingSpot parkingSpot;
	private DataBasePrepareService dataBasePrepareService;

	@Test
	public void isRecurringUserReturnTrue() {
		// GIVEN
		Date inTime = new Date();
		Date outTime = new Date();
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket1 = new Ticket();
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();

		ticket1.setId(1);
		ticket1.setParkingSpot(parkingSpot);
		ticket1.setVehicleRegNumber(VehicleRegNumber.VEHICLE_REG_NUMBER);
		ticket1.setInTime(inTime);

		ticketDAO.saveTicket(ticket1);

		ticket1 = ticketDAO.getTicket(VehicleRegNumber.VEHICLE_REG_NUMBER);

		ticket1.setPrice(4.5);
		outTime.setTime(inTime.getTime() + (3 * 60 * 60 * 1000)); // 3 hours
		ticket1.setOutTime(outTime);

		ticketDAO.updateTicket(ticket1);

		// WHEN
		boolean response = ticketDAO.isRecurringUser(VehicleRegNumber.VEHICLE_REG_NUMBER);

		// THEN
		assertEquals(true, response);
	}

	@Test
	public void isRecurringUserReturnFalse() {
		// GIVEN
		Date inTime = new Date();
		Date outTime = new Date();
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		Ticket ticket1 = new Ticket();
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();

		ticket1.setId(1);
		ticket1.setParkingSpot(parkingSpot);
		ticket1.setVehicleRegNumber(VehicleRegNumber.VEHICLE_REG_NUMBER);
		ticket1.setInTime(inTime);

		ticketDAO.saveTicket(ticket1);

		ticket1 = ticketDAO.getTicket(VehicleRegNumber.VEHICLE_REG_NUMBER);

		ticket1.setPrice(7.5);
		outTime.setTime(inTime.getTime() + (5 * 60 * 60 * 1000)); // 5 hours
		ticket1.setOutTime(outTime);

		ticketDAO.updateTicket(ticket1);

		// WHEN
		boolean response = ticketDAO.isRecurringUser("ZZZZZZ");

		// THEN
		assertEquals(false, response);
	}

}
