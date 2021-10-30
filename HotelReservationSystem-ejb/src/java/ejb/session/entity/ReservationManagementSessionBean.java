/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Reservation;
import entity.user.Guest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author brianlim
 */
@Stateless
public class ReservationManagementSessionBean implements ReservationManagementSessionBeanRemote, ReservationManagementSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    public Reservation viewMyReservationDetails(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        reservation.getRoomType();
        reservation.getOccupant();
        reservation.getAllocation();
        reservation.getRates();
        reservation.getAllocation();
        return reservation;
    }

    public List<Reservation> viewAllMyReservations(Guest guest) {
        /* if need to find guest using a query
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :inUsername");
        query.setParameter("inUsername", username);

        Guest guest = (Guest) query.getSingleResult();
         */

        List<Reservation> reservations = guest.getReservations();
        for (Reservation r : reservations) {
            r.getRoomType();
            r.getOccupant();
            r.getAllocation();
            r.getRates();
            r.getAllocation();

        }

        return reservations;
    }

}
