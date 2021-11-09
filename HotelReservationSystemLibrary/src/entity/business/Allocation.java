/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Occupant;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Winter
 */
@Entity
public class Allocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allocationId;
    @ManyToOne(optional = false)
    @JoinColumns( {
        @JoinColumn(name = "room_floor", referencedColumnName = "floorNumber", nullable = false, updatable = false),
        @JoinColumn(name = "room_number", referencedColumnName = "roomNumber", nullable = false, updatable = false)
    })
    private Room room;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Reservation reservation;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Occupant occupant;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    public Allocation() {
    }

    public Allocation(Room room, Reservation reservation) {
        this.room = room;
        this.reservation = reservation;
        this.occupant = reservation.getOccupant();
        room.use();
    }

    public Long getAllocationId() {
        return allocationId;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the occupant
     */
    public Occupant getOccupant() {
        return occupant;
    }
    
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @return the checkInDate
     */
    public LocalDate getCheckInDate() {
        return reservation.getCheckInDate();
    }

    /**
     * @return the checkOutDate
     */
    public LocalDate getCheckOutDate() {
        return reservation.getCheckOutDate();
    }

    /**
     * @return the checkInTime
     */
    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    /**
     * @return the checkOutTime
     */
    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }
    
    public void checkIn(LocalTime checkInTime) {
        System.out.println("Checked in at " + checkInTime);
        this.checkInTime = checkInTime;
        room.setUnavailable();
    }

    public void checkOut(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
        room.setAvailable();
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (allocationId != null ? allocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the allocationId fields are not set
        if (!(object instanceof Allocation)) {
            return false;
        }
        Allocation other = (Allocation) object;
        if ((this.allocationId == null && other.allocationId != null) || (this.allocationId != null && !this.allocationId.equals(other.allocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.business.Allocation[ id=" + allocationId + " ]";
    }
    
}
