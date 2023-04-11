package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket , boolean isRegularClient ) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();

        // Calculer la durée de stationnement en heures
        double duration = (double) (outTime - inTime) / (60 * 60 * 1000);

        // Appliquer la gratuité pour les 30 premières minutes
        if (duration < 0.5) {
            ticket.setPrice(0);
            return;
        }
        double price = 0;
        // Calculer le prix en fonction du type de stationnement
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                price = duration * Fare.CAR_RATE_PER_HOUR;
                break;
            }
            case BIKE: {
                price = duration * Fare.BIKE_RATE_PER_HOUR;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
                // Appliquer la réduction de 5% pour les clients réguliers
        if (isRegularClient) {
            price *= 0.95;
        }
        ticket.setPrice(price);
    }

}
