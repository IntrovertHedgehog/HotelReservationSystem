/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.DirectCustomer;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Winter
 */
@Entity
public class DirectReservation extends Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private DirectCustomer customer;

    public DirectReservation() {
        super();
    }

    public DirectReservation(DirectCustomer customer, RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        super(roomType, checkInDate, checkOutDate);
        this.customer = customer;
    }
    
    
    /**
     * @return the guest
     */
    public DirectCustomer getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return String.format("Reservation | id = %d | guest passport = %s | room type = %s | period = %s - %s",
                getReservationId(), customer.getPassport(), getRoomType().getName(), getCheckInDate().toString(), getCheckOutDate().toString());
    }
    
}
