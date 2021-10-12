/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.PartnerReservationManager;
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

/**
 *
 * @author Winter
 */
@Entity
public class PartnerAllocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long partnerAllocationId;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Room room;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private PartnerReservationManager partner;
    @Column(nullable = false, updatable = false)
    private LocalDate checkInDate;
    @Column(nullable = false, updatable = false)
    private LocalDate checkOutDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    public PartnerAllocation() {
    }

    public PartnerAllocation(Room room, PartnerReservationManager partner, LocalDate checkInDate, LocalDate checkOutDate) {
        this.room = room;
        this.partner = partner;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Long getPartnerAllocationId() {
        return partnerAllocationId;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the partner
     */
    public PartnerReservationManager getPartner() {
        return partner;
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
        hash += (partnerAllocationId != null ? partnerAllocationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerAllocationId fields are not set
        if (!(object instanceof PartnerAllocation)) {
            return false;
        }
        PartnerAllocation other = (PartnerAllocation) object;
        if ((this.partnerAllocationId == null && other.partnerAllocationId != null) || (this.partnerAllocationId != null && !this.partnerAllocationId.equals(other.partnerAllocationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.business.Allocation[ id=" + partnerAllocationId + " ]";
    }
    
}
