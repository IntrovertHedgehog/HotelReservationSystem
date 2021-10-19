/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Occupant;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author Winter
 */
@Entity
public class WalkInReservation extends Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    public WalkInReservation() {
        super();
    }

    public WalkInReservation(RoomType roomType, Occupant occupant, List<Rate> rates, LocalDate checkInDate, LocalDate checkOutDate) {
        super(roomType, occupant, rates, checkInDate, checkOutDate);
    }
    
    @Override
    public String toString() {
        return String.format("Reservation | id = %d | guest passport = %s | room type = %s | period = %s - %s",
                getReservationId(), super.getOccupant().getPassport(), getRoomType().getName(), getCheckInDate().toString(), getCheckOutDate().toString());
    }
    
}
