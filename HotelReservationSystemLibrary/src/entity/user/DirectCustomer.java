/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import entity.business.DirectReservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class DirectCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 16)
    private String passport;
    @Column(length = 32)
    private String name;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<DirectReservation> reservations;

    public DirectCustomer() {
        reservations = new ArrayList<>();
    }

    public DirectCustomer(String passport, String name, String username, String password) {
        this();
        this.passport = passport;
        this.name = name;
        this.username = username;
        this.password = password;
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
    
    public List<DirectReservation> getReservations() {
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
        if (!(object instanceof DirectCustomer)) {
            return false;
        }
        DirectCustomer other = (DirectCustomer) object;
        if ((this.getPassport() == null && other.getPassport() != null) || (this.getPassport() != null && !this.passport.equals(other.passport))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Direct Customer -> " + super.toString();
    }
}
