package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.stream.Stream;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }


    static Stream<? extends Arguments> provideArguments() {
        return Stream.of(
                Arguments.of(ParkingType.CAR, 1, 0),
                Arguments.of(ParkingType.BIKE, 1, 0),
                Arguments.of(ParkingType.CAR, 29, 0),
                Arguments.of(ParkingType.BIKE, 29, 0),
                Arguments.of(ParkingType.CAR, 30, 0.5 *  Fare.CAR_RATE_PER_HOUR),
                Arguments.of(ParkingType.BIKE, 30, 0.5 *  Fare.BIKE_RATE_PER_HOUR),
                Arguments.of(ParkingType.CAR, 31, (31.0 / 60) * Fare.CAR_RATE_PER_HOUR),
                Arguments.of(ParkingType.BIKE, 31, (31.0 / 60) * Fare.BIKE_RATE_PER_HOUR),
                Arguments.of(ParkingType.CAR, 45, 0.75 * Fare.CAR_RATE_PER_HOUR),
                Arguments.of(ParkingType.BIKE, 45, 0.75 * Fare.BIKE_RATE_PER_HOUR),
                Arguments.of(ParkingType.CAR, 60, 1 * Fare.CAR_RATE_PER_HOUR),
                Arguments.of(ParkingType.BIKE, 60, 1 * Fare.BIKE_RATE_PER_HOUR),
                Arguments.of(ParkingType.CAR, 120, 2 * Fare.CAR_RATE_PER_HOUR),
                Arguments.of(ParkingType.BIKE, 120, 2 * Fare.BIKE_RATE_PER_HOUR)
        );
    }


    @ParameterizedTest
    @MethodSource("provideArguments")
    public void calculateFare(ParkingType parkingType, long duration, double expectedPrice){
        //GIVEN
        Date inTime = new Date( System.currentTimeMillis() - (  duration * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, parkingType,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        // WHEN
        fareCalculatorService.calculateFare(ticket, false);

        //THEN
        assertEquals(ticket.getPrice(),  expectedPrice);
    }


    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, false));
    }
    @Test
    public void calculateFareForRegularClient(){
        //GIVEN
        long duration = 60; //in minutes
        Date inTime = new Date( System.currentTimeMillis() - (  duration * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Simulate past ticket for same vehicle registration number
        TicketDAO ticketDAO = new TicketDAO();
        Ticket pastTicket = new Ticket();
        pastTicket.setVehicleRegNumber("ABCDEF");
        ticketDAO.saveTicket(pastTicket);

        //Set the ticket's vehicle registration number to match the past ticket
        ticket.setVehicleRegNumber("ABCDEF");

        //WHEN
        fareCalculatorService.calculateFare(ticket, true);

        //THEN
        double expectedPrice = 0.95 * Fare.CAR_RATE_PER_HOUR;
        assertEquals(expectedPrice, ticket.getPrice());
    }

}
