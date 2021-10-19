/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Guest;
import entity.user.Occupant;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Winter
 */
@Entity
public class WalkInReservation extends Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Occupant customer;

    public WalkInReservation() {
        super();
    }

    public WalkInReservation(Guest customer, RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        super(roomType, checkInDate, checkOutDate);
        this.customer = customer;
    }
    
    
    /**
     * @return the guest
     */
    public Occupant getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return String.format("Reservation | id = %d | guest passport = %s | room type = %s | period = %s - %s",
                getReservationId(), customer.getPassport(), getRoomType().getName(), getCheckInDate().toString(), getCheckOutDate().toString());
    }
    
}
