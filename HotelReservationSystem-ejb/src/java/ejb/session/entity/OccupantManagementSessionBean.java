/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.user.Occupant;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.OccupantAlreadyExistsException;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author Winter
 */
@Stateless
public class OccupantManagementSessionBean implements OccupantManagementSessionBeanRemote {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public Occupant retrieveOccupant(String passport) throws OccupantNotFoundException {
        Occupant occupant = em.find(Occupant.class, passport);
        
        if (occupant == null) {
            throw new OccupantNotFoundException("Occupant with is passport is not found");
        }
        
        occupant.getReservations();
        occupant.getAllocations();
        return occupant;
    }

    @Override
    public Occupant createOccupant(Occupant occupant) throws OccupantAlreadyExistsException {
        em.persist(occupant);
        occupant.getReservations();
        occupant.getAllocations();
        em.flush();
        return occupant;
    }
}
