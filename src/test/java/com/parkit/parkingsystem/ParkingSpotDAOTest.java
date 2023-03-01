package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

	private DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private ParkingSpotDAO parkingSpotDAO;
	private DataBasePrepareService dataBasePrepareService;
	private ParkingSpot parkingSpot;

	@Mock
	private DataBaseTestConfig dataBaseTestConfigMock;
	
	@Test
	public void getNextAvailableSlotForBike_Return4() throws Exception {
		// GIVEN
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();

		// WHEN
		int response = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);

		// THEN
		assertEquals(4, response);
	}


	@Test
	public void getNextAvailableSlot_ReturnException() throws ClassNotFoundException, SQLException {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfigMock;

        assertThrows(Exception.class, () -> parkingSpotDAO.getNextAvailableSlot(ParkingType.AVION));
	}

	@Test
	public void updateParkingForParkingSpot_ReturnTrue() {
		// GIVEN
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		dataBasePrepareService.clearDataBaseEntries();
		parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		// WHEN
		boolean response = parkingSpotDAO.updateParking(parkingSpot);

		// THEN
		assertEquals(true, response);
	}
}
