/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.PartnerReservationManager;
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
public class PartnerReservation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long partnerReservationId;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private RoomType roomType;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private PartnerReservationManager partner;
    @Column(nullable = false, updatable = false)
    private LocalDate checkInDate;
    @Column(nullable = false, updatable = false)
    private LocalDate checkOutDate;

    public PartnerReservation() {
    }

    public PartnerReservation(RoomType roomType, PartnerReservationManager partner, LocalDate checkInDate, LocalDate checkOutDate) {
        this.roomType = roomType;
        this.partner = partner;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Long getPartnerReservationId() {
        return partnerReservationId;
    }

    /**
     * @return the room
     */
    public RoomType getRoomType() {
        return roomType;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partnerReservationId != null ? partnerReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the partnerReservationId fields are not set
        if (!(object instanceof PartnerReservation)) {
            return false;
        }
        PartnerReservation other = (PartnerReservation) object;
        if ((this.partnerReservationId == null && other.partnerReservationId != null) || (this.partnerReservationId != null && !this.partnerReservationId.equals(other.partnerReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.business.Allocation[ id=" + partnerReservationId + " ]";
    }
    
}
