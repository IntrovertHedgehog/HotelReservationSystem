/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import entity.business.OnlineReservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class Guest extends Occupant implements Serializable {
    private static final long serialVersionUID = 1L;    
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToMany()
    private List<OnlineReservation> onlineReservations;
    

    public Guest() {
        super();
        this.onlineReservations = new ArrayList<>();
    }

    public Guest(String username, String password) {
        this();
        this.username = username;
        this.password = password;
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
    
    public List<OnlineReservation> getOnlineReservations() {
        return onlineReservations;
    }
    
    public void addOnlineReservation() {
        
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
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.getPassport() == null && other.getPassport() != null) || (this.getPassport() != null && !super.getPassport().equals(other.getPassport()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Guest -> " + super.toString();
    }
}
