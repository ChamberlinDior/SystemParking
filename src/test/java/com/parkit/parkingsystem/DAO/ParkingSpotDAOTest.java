package com.parkit.parkingsystem.DAO;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ParkingSpotDAOTest {
    private static ParkingSpotDAO parkingSpotDAO;

    @Mock
    private static DataBaseConfig dataBaseConfig;

    @Mock
    private static Connection connection;

    @Mock
    private static PreparedStatement preparedStatement;

    @Mock
    private static ResultSet resultSet;

    @BeforeEach
    private void setUpPerTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        parkingSpotDAO = new ParkingSpotDAO();
        when(dataBaseConfig.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    }

    @Test
    @DisplayName("Get next available slot")
    void getNextAvailableSlotTest() throws Exception {
        assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    @DisplayName("Update parking slot availability")
    void updateParkingTest() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        assertTrue(parkingSpotDAO.updateParking(parkingSpot));
    }

    @Test
    @DisplayName("Get parking spot by id")
    void getParkingSpotTest() {
        assertNull(parkingSpotDAO.getParkingSpot(1));
    }
}
