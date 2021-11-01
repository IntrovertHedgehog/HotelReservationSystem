/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.business.Allocation;
import entity.business.Reservation;
import entity.business.Room;
import enumeration.RoomStatus;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Winter
 */
@Singleton
public class AllocatingBotSessionBean implements AllocatingBotSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    private Room searchRoom(Reservation reservation) {
        return (Room) em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomtype AND r.status <> :status"
                + "EXCEPT"
                + "SELECT r FROM Allocation a JOIN a.room r WHERE r.roomType = :roomtype AND r.status <> :status AND a.checkInDate < :checkOutDate AND a.checkOutDate > :checkInDate")
                .setParameter("roomType", reservation.getRoomType())
                .setParameter("status", RoomStatus.DISABLE)
                .setParameter(":checkInDate", reservation.getCheckInDate())
                .setParameter(":checkOutDate", reservation.getCheckOutDate())
                .setMaxResults(1)
                .getSingleResult();
    }
    
    private void createException(Reservation reservation) {
        
    }
    
    @Override
    public Long allocate(Reservation reservation) {
        Room room = searchRoom(reservation);
        if (room != null) {
            Allocation allocation = new Allocation(room, reservation);
            em.persist(allocation);
            reservation.setAllocation(allocation);
            em.flush();
            return allocation.getAllocationId();
        } else {
            createException(reservation);
            return null;
        }
    }

    @Schedule(hour = "2")
    public void automaticAllocation() {
        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r WHERE r.isAllocated = FALSE")
                .getResultList();
        reservations.forEach(r -> allocate(r));
    }
}
