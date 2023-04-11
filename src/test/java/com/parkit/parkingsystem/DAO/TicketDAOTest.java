package com.parkit.parkingsystem.DAO;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Date;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

class TicketDAOTest {

    private TicketDAO ticketDAO;
    private Ticket ticket;

    @BeforeEach
    void setUp() throws Exception {
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = new DataBaseTestConfig();

        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
    }

    @Test
    @DisplayName("Save ticket")
    void saveTicket() throws Exception {
        //GIVEN a ticket

        //WHEN the ticket is saved
        boolean response = ticketDAO.saveTicket(ticket);

        //THEN the response is TRUE
        assertTrue(response);

        //AND the ticket is saved
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");
        assertNotNull(savedTicket);
        assertEquals(ticket.getPrice(), savedTicket.getPrice() );
       // .....
    }





    @Test
    @DisplayName("Get ticket")
    void getTicket() {
        //GIVEN a saved ticket
        ticketDAO.saveTicket(ticket);

        //WHEN
        Ticket savedTicket = ticketDAO.getTicket("ABCDEF");

        //THEN
        assertNotNull(savedTicket);
        assertEquals(ticket.getPrice(), savedTicket.getPrice() );
    }

   /** @Test
    @DisplayName("Update ticket")
    void updateTicket() {
        ticketDAO.saveTicket(ticket);
        ticket.setPrice(10);
        ticket.setOutTime(new Date());
        assertTrue(ticketDAO.updateTicket(ticket));

       // verifier les autres
    }*/

   @Test
   @DisplayName("Update ticket")
   void updateTicket() {
       // Créer un nouveau ticket
       Ticket ticket = new Ticket(/*paramètres du ticket*/);

       // Enregistrer le ticket dans la base de données
       ticketDAO.saveTicket(ticket);

       // Modifier le prix et l'heure de sortie du ticket
       ticket.setPrice(10);
       ticket.setOutTime(new Date());

       // Mettre à jour le ticket dans la base de données
       assertTrue(ticketDAO.updateTicket(ticket));

       // Récupérer le ticket modifié
       Ticket updatedTicket = ticketDAO.getTicketById(ticket.getId());

       // Vérifier si les informations du ticket ont été mises à jour correctement
       assertEquals(10, updatedTicket.getPrice());
       assertNotNull(updatedTicket.getOutTime());
   }



    @Test
    public void testIsRegularClient() {
        //WHEN
        // Vérifier si la méthode renvoie la valeur attendue
        boolean isRegular = ticketDAO.isRegularClient("ABCDEF");
        assertFalse(isRegular);

        //THEN
        // Comparer la valeur renvoyée par la méthode avec la valeur attendue
        assertEquals(false, isRegular); // et le true ?
    }

}
