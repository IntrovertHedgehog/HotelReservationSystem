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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class PartnerCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private PartnerReservationManager partner;
    @Id
    @Column(length = 16)
    private String passport;
    @Column(length = 32)
    private String name;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<PartnerReservation> reservations;

    public PartnerCustomer() {
        reservations = new ArrayList<>();
    }

    public PartnerCustomer(PartnerReservationManager partner, String passport, String name) {
        this();
        this.partner = partner;
        this.passport = passport;
        this.name = name;
    }

    /**
     * @return the partner
     */
    public PartnerReservationManager getPartner() {
        return partner;
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

    public List<PartnerReservation> getReservations() {
        return reservations;
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
        if (!(object instanceof PartnerCustomer)) {
            return false;
        }
        PartnerCustomer other = (PartnerCustomer) object;
        if ((this.getPassport() == null && other.getPassport() != null) || (this.getPassport() != null && !this.passport.equals(other.passport))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Partner Customer -> " + super.toString();
    }
}
