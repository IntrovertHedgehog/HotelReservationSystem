/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Occupant;
import entity.user.Partner;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Winter
 */
@Entity
public class PartnerReservation extends Reservation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Partner partner;
    
    public PartnerReservation() {
        super();
    }

    public PartnerReservation(RoomType roomType, Occupant occupant, Partner partner, List<Rate> rates, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal fee) {
        super(roomType, occupant, rates, checkInDate, checkOutDate, fee);
        this.partner = partner;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }
    
    public void setPartner(Partner partner) {
        this.partner = partner;
    }
    
    public void nullify() {
        super.nullify();
        partner = null;
    }

    @Override
    public String toString() {
        return String.format("Reservation | id = %d | guest passport = %s | room type = %s | period = %s - %s",
                getReservationId(), super.getOccupant().getPassport(), getRoomType().getName(), getCheckInDate().toString(), getCheckOutDate().toString());
    }
    
}
