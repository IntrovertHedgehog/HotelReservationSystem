/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.Allocation;
import entity.business.Room;
import entity.business.RoomType;
import entity.user.Occupant;
import enumeration.ClientType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
@Stateful
public class WalkInSessionBean implements WalkInSessionBeanRemote {

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    private List<ReservationSearchResult> searchResults;

    @Override
    public List<ReservationSearchResult> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) throws InvalidTemporalInputException {
        return this.searchResults = reservationManagementSessionBean.searchReservation(checkInDate, checkOutDate, ClientType.WALKIN);
    }

    @Override
    public Long walkInReserveRoom(Integer indexOfRoomType, Occupant occupant) throws NoMoreRoomException {
        ReservationSearchResult target = searchResults.get(indexOfRoomType);
        RoomType roomType = target.getRoomType();
        LocalDate checkInDate = target.getCheckInDate();
        LocalDate checkOutDate = target.getCheckOutDate();
        BigDecimal fee = target.getPrevailRate();

        return reservationManagementSessionBean.createWalkInReservation(roomType, checkInDate, checkOutDate, occupant, fee);
    }

    @Override
    public List<String> checkInGuest(LocalDateTime checkInDateTime, String passport) {
        Occupant occupant = em.find(Occupant.class, passport);
        List<String> rooms = new ArrayList<>();

        if (occupant != null) {
            LocalDate checkInDate = checkInDateTime.toLocalDate();
            LocalTime checkInTime = checkInDateTime.toLocalTime();

            if (checkInTime.isBefore(LocalTime.of(14, 0))) {
                rooms.add("Please check in after 2PM, your rooms are not ready!");
                return rooms;
            }

            for (Allocation a : occupant.getAllocations()) {
                if (checkInDate.equals(a.getCheckInDate())) {
                    String str = a.getRoom().getRoomId().toString();

                    if (a.getCheckInTime() == null) {
                        a.checkIn(checkInTime);
                    } else {
                        str += " (this room has been checked in)";
                    }
                    
                    if (isOccupied(checkInDate, a.getRoom())) {
                        str += " (Previous occupant has not checked out - checking in proceeds!)";
                    }

                    rooms.add(str);
                }
            }
        } else {
            rooms.add("This occupant has not booked any room");
        }

        if (rooms.size() == 0) {
            rooms.add("This occupant has not booked any room");
        }
        return rooms;
    }

    //Did not control late checkout
    @Override
    public List<String> checkOutGuest(LocalDateTime checkOutDateTime, String passport) {
        Occupant occupant = em.find(Occupant.class, passport);
        List<String> rooms = new ArrayList<>();

        if (occupant != null) {
            LocalDate checkOutDate = checkOutDateTime.toLocalDate();
            LocalTime checkOutTime = checkOutDateTime.toLocalTime();

            for (Allocation a : occupant.getAllocations()) {
                
                if (checkOutDate.equals(a.getCheckOutDate())
                        && a.getCheckInDate() != null) {
                    String str = a.getRoom().getRoomId().toString();

                    if (a.getCheckOutTime() == null) {
                        if (isBooked(checkOutDate, a.getRoom())) {
                            str += ": this room is booked for today, please pay late checkout fee";
                        }
                        
                        a.checkOut(checkOutTime);
                    } else {
                        str += " (This room has been checked out)";
                    }
                    
                    rooms.add(str);
                }

            }
        } else {
            rooms.add("This occupant is not found");
        }
        
        
        return rooms;
    }

    public Boolean isBooked(LocalDate date, Room room) {
        try {
            Allocation a = (Allocation) em.createQuery("SELECT a FROM Allocation a JOIN a.reservation r WHERE a.room = :room AND r.checkInDate = :date")
                    .setParameter("room", room)
                    .setParameter("date", date)
                    .setMaxResults(1)
                    .getSingleResult();

            if (a != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        }
    }

    public Boolean isOccupied(LocalDate date, Room room) {
        try {
            Allocation a = (Allocation) em.createQuery("SELECT a FROM Allocation a JOIN a.reservation r WHERE a.room = :room AND r.checkOutDate = :date")
                    .setParameter("room", room)
                    .setParameter("date", date)
                    .setMaxResults(1)
                    .getSingleResult();

            if (a != null && a.getCheckOutTime() == null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        }
    }
}
