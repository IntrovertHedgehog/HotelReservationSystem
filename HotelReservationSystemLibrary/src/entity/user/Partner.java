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
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 64)
    @Column(nullable = false, length = 64)
    private String name;
    @Size(min = 1, max = 32)
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Size(min = 1)
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
    private List<PartnerReservation> partnerReservations;

    public Partner() {
        partnerReservations = new ArrayList<>();
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
        this.setName(name);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public Boolean matchPassword(String password) {
        return this.password.equals(password);
    }
    
    /**
     * @param partnerId the partnerId to set
     */
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param partnerReservations the partnerReservations to set
     */
    public void setPartnerReservations(List<PartnerReservation> partnerReservations) {
        this.partnerReservations = partnerReservations;
    }
    
    
    
    /**
     * @return the partnerReservations
     */
    public List<PartnerReservation> getPartnerReservations() {
        return partnerReservations;
    }
    
    public void hardNullify() {
        this.setPartnerReservations(null);
    }
    
    public void softNullify() {
        for (PartnerReservation res : partnerReservations) {
            res.nullify();
        }
    }
    
    public void addReservation(PartnerReservation partnerReservation) {
        if (partnerReservation.getPartner().equals(this) && !partnerReservations.contains(partnerReservation)) {
            partnerReservations.add(partnerReservation);
        }
    }
    
    public void removeReservation(PartnerReservation partnerReservation) {
        partnerReservations.remove(partnerReservation);
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
