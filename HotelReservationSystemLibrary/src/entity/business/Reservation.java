/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Guest;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
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
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReservationId;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private RoomType roomType;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Guest guest;
    @Column(nullable = false, updatable = false)
    private LocalDate checkInDate;
    @Column(nullable = false, updatable = false)
    private LocalDate checkOutDate;

    public Reservation() {
    }

    public Reservation(RoomType roomType, Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        this.roomType = roomType;
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * @return the checkInDate
     */
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    

    public Long getReservationId() {
        return ReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ReservationId != null ? ReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the ReservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.ReservationId == null && other.ReservationId != null) || (this.ReservationId != null && !this.ReservationId.equals(other.ReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Reservation | id = %d | guest passport = %s | room type = %s | period = %s - %s",
                ReservationId, guest.getPassport(), roomType.getName(), checkInDate.toString(), checkOutDate.toString());
    }
    
}
