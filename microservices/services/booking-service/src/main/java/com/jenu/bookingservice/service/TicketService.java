package com.jenu.bookingservice.service;


import com.jenu.bookingservice.model.Booking;
import com.jenu.bookingservice.model.Ticket;
import com.jenu.exception.ResourceNotFoundException;

import java.util.List;

public interface TicketService {

    List<Ticket> generateTicketsForBooking(Booking booking);

    Ticket getTicketByNumber(String ticketNumber) throws ResourceNotFoundException;

    List<Ticket> getTicketsByBooking(Long bookingId);

    List<Ticket> getTicketsByPassenger(Long passengerId);

    Ticket cancelTicket(Long ticketId) throws ResourceNotFoundException;

    Ticket markTicketAsUsed(Long ticketId) throws ResourceNotFoundException;

    Ticket refundTicket(Long ticketId) throws ResourceNotFoundException;
}
