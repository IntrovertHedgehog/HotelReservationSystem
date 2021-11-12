/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.business.Allocation;
import entity.business.ExceptionReport;
import entity.business.Reservation;
import entity.business.Room;
import enumeration.ExceptionStatus;
import enumeration.RoomStatus;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.exception.NoMoreRoomException;

/**
 *
 * @author Winter
 */
@Singleton
@Startup
public class AllocatingBotSessionBean implements AllocatingBotSessionBeanLocal, AllocationBotSessionBeanRemote {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    private Room searchRoom(Reservation reservation) throws NoResultException {
        List<Room> allRooms = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND r.status = :status")
                .setParameter("roomType", reservation.getRoomType())
                .setParameter("status", RoomStatus.AVAILABLE)
                .getResultList();
        
        List<Room> usedRooms = em.createQuery("SELECT r FROM Allocation a JOIN a.room r JOIN a.reservation re WHERE r.roomType = :roomType AND r.status = :status AND re.checkInDate < :checkOutDate AND re.checkOutDate > :checkInDate")
                .setParameter("roomType", reservation.getRoomType())
                .setParameter("status", RoomStatus.AVAILABLE)
                .setParameter("checkInDate", reservation.getCheckInDate())
                .setParameter("checkOutDate", reservation.getCheckOutDate())
                .getResultList();
        
        for (Room room : allRooms) {
            if (!usedRooms.contains(room)) {
                return room;
            }
        }
        
        throw new NoResultException("We ran out of room!");
    }

    private ExceptionReport createException(Reservation reservation) {
        List<Room> allRooms = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :nextRoomType AND r.status = :status")
                .setParameter("nextRoomType", reservation.getRoomType().getNextRoomType())
                .setParameter("status", RoomStatus.AVAILABLE)
                .setParameter("checkInDate", reservation.getCheckInDate())
                .setParameter("checkOutDate", reservation.getCheckOutDate())
                .getResultList();
        
        List<Room> usedRooms = em.createQuery("SELECT r FROM Allocation a JOIN a.room r JOIN a.reservation re WHERE r.roomType = :nextRoomType AND r.status = :status AND re.checkInDate < :checkOutDate AND re.checkOutDate > :checkInDate")
                .setParameter("status", RoomStatus.AVAILABLE)
                .setParameter("checkInDate", reservation.getCheckInDate())
                .setParameter("checkOutDate", reservation.getCheckOutDate())
                .getResultList();
        
        
                
        try {
            Room room = allRooms.stream()
                    .filter(r -> !usedRooms.contains(r))
                    .findFirst()
                    .orElseThrow(() -> new NoMoreRoomException("No more room to upgrade to"));

            Allocation allocation = new Allocation(room, reservation);
            em.persist(allocation);

            reservation.setAllocation(allocation);

            ExceptionReport exceptionReport = new ExceptionReport(reservation, allocation);
            em.persist(exceptionReport);

            em.flush();

            return exceptionReport;
        } catch (NoMoreRoomException ex) {
            ExceptionReport exceptionReport = new ExceptionReport(reservation);
            em.persist(exceptionReport);
            return exceptionReport;
        }
    }

    @Override
    public Long allocate(Reservation reservation) {
        try {
            Room room = searchRoom(reservation);
            Allocation allocation = new Allocation(room, reservation);
            em.persist(allocation);
            reservation.setAllocation(allocation);
            em.flush();
            return allocation.getAllocationId();
        } catch (NoResultException ex) {
            ExceptionReport exceptionReport = createException(reservation);

            if (exceptionReport.getStatus() == ExceptionStatus.PENDING) {
                return null;
            } else {
                return exceptionReport.getAllocation().getAllocationId();
            }
        }
    }
    
    @Schedule(hour = "2")
    public void automaticAllocation() {
        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r WHERE r.isProcessed = FALSE")
                .getResultList();
        reservations.forEach(r -> {
            allocate(r);
            r.setIsProcessed(Boolean.TRUE);
        });
    }

    @Override
    public void manualAllocate() {
        automaticAllocation();
    }
}
