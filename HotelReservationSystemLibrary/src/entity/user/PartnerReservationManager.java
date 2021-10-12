/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import entity.business.PartnerAllocation;
import entity.business.PartnerReservation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class PartnerReservationManager extends PartnerEmployee implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
    private List<PartnerReservation> reservations;
    @OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
    private List<PartnerAllocation> allocations;

    public PartnerReservationManager() {
        reservations = new ArrayList<>();
        allocations = new ArrayList<>();
    }

    public PartnerReservationManager(String name, String username, String password) {
        super(name, username, password);
        reservations = new ArrayList<>();
        allocations = new ArrayList<>();
    }
    
    /**
     * @return the reservations
     */
    public List<PartnerReservation> getReservations() {
        return reservations;
    }

    /**
     * @return the allocations
     */
    public List<PartnerAllocation> getAllocations() {
        return allocations;
    }

    @Override
    public String toString() {
        return "Partner Reservation Manager -> " + super.toString();
    }
    
}
