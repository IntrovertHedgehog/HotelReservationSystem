/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import entity.business.Allocation;
import entity.business.Reservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Occupant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 16)
    private String passport;
    @Column(length = 32)
    private String name;
    @OneToMany(mappedBy = "occupant", fetch = FetchType.LAZY)
    private List<Allocation> allocations;
    @OneToMany(mappedBy = "occupant", fetch = FetchType.LAZY)
    private List<Reservation> reservations;
    
    public Occupant() {
        allocations = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public Occupant(String passport, String name) {
        this();
        this.passport = passport;
        this.name = name;
    }

    /**
     * @return all the reservations of occupant
     */
    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public void addReservation(Reservation reservation) {
        if (reservation.getOccupant().equals(this) && !this.reservations.contains(reservation)) {
            this.reservations.add(reservation);
        }
    }
    
    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }
    
    /**
     * @return the passport
     */
    public String getPassport() {
        return passport;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }

    

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPassport() != null ? getPassport().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof Occupant)) {
            return false;
        }
        Occupant other = (Occupant) object;
        if ((this.getPassport() == null && other.getPassport() != null) || (this.getPassport() != null && !this.passport.equals(other.passport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Occupant | passport= " + getPassport();
    }
    
}
