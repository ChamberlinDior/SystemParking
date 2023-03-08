package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }


        Date inTime = ticket.getInTime();
        Date outTime = ticket.getOutTime();

        // Convertir en temps GMT
        ZoneOffset gmtOffset = ZoneOffset.UTC;
        Instant inInstant = inTime.toInstant();
        Instant outInstant = outTime.toInstant();
        LocalDateTime gmtInTime = LocalDateTime.ofInstant(inInstant, gmtOffset);
        LocalDateTime gmtOutTime = LocalDateTime.ofInstant(outInstant, gmtOffset);

        // Calculer la durée de stationnement en heures
        long duration = Duration.between(gmtInTime, gmtOutTime).toHours();

        // Appliquer la gratuité pour les 30 premières minutes
        if (duration <= 0.5) {
            ticket.setPrice(0);
            return;
        }

        // Calculer le prix en fonction du type de stationnement
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                double price = duration * Fare.CAR_RATE_PER_HOUR;
                // Appliquer la réduction de 5% pour les clients réguliers
                if (isRegularClient(ticket)) {//boolean
                    price *= 0.95;
                }
                ticket.setPrice(price);
                break;
            }
            case BIKE: {
                double price = duration * Fare.BIKE_RATE_PER_HOUR;
                // Appliquer la réduction de 5% pour les clients réguliers
                if (isRegularClient(ticket)) {
                    price *= 0.95;
                }
                ticket.setPrice(price);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown Parking Type");
        }
    }

    private boolean isRegularClient(Ticket ticket){
        return false;
    }

}
