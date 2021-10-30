/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import entity.business.PartnerReservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partnerId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
    private List<PartnerReservation> reservations;

    public Partner() {
        reservations = new ArrayList<>();
    }

    public Partner(String name, String username, String password) {
        this();
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the partnerId
     */
    public Long getPartnerId() {
        return partnerId;
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

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username, String password) {
        if (this.password.equals(password)) {
            this.username = username;
        }
    }

    public void setPassword(String newPassword, String oldPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
        }
    }
    
    /**
     * @return the reservations
     */
    public List<PartnerReservation> getReservations() {
        return reservations;
    }
    
    public void addReservation(PartnerReservation partnerReservation) {
        if (partnerReservation.getPartner().equals(this) && !reservations.contains(partnerReservation)) {
            reservations.add(partnerReservation);
        }
    }
    
    public void removeReservation(PartnerReservation partnerReservation) {
        reservations.remove(partnerReservation);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPartnerId() != null ? getPartnerId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.getPartnerId() == null && other.getPartnerId() != null) || (this.getPartnerId() != null && !this.partnerId.equals(other.partnerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Partner Reservation Manager | id = " + getPartnerId() + " | name = " + getName();
    }
    
}
