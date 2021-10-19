/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import entity.user.Guest;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author brianlim
 */
@Entity
public class OnlineReservation extends Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Guest customer;
    
    public OnlineReservation() {
        super();
    }
    


    @Override
    public String toString() {
        return "entity.business.OnlineReservation[ id=" + super.getReservationId() + " ]";
    }
    
}
