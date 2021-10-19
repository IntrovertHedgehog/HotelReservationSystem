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
    @JoinColumn(nullable = false, updatable = false)
    private Room room;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Occupant occupant;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Reservation reservation;
    @Column(nullable = false, updatable = false)
    private LocalDate checkInDate;
    @Column(nullable = false, updatable = false)
    private LocalDate checkOutDate;
    @Column(updatable = false)
    private LocalTime checkInTime;
    @Column(updatable = false)
    private LocalTime checkOutTime;

    public Allocation() {
    }

    public Allocation(Room room, Occupant occupant, Reservation reservation, LocalDate checkInDate, LocalDate checkOutDate) {
        this.room = room;
        this.occupant = occupant;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservation = reservation;
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
        return checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
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
        this.checkInTime = checkInTime;
    }

    public void checkOut(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
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
