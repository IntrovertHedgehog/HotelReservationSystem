/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.Allocation;
import entity.business.RoomType;
import entity.user.Occupant;
import enumeration.ClientType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
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
    public List<ReservationSearchResult> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) throws InvalidTemporalInputException  {
        return this.searchResults = reservationManagementSessionBean.searchReservation(checkInDate, checkOutDate, ClientType.WALKIN);
    }

    @Override
    public Long walkInReserveRoom(Integer indexOfRoomType, Occupant occupant) throws NoMoreRoomException {
        ReservationSearchResult target = searchResults.get(indexOfRoomType);
        RoomType roomType = target.getRoomType();
        LocalDate checkInDate = target.getCheckInDate();
        LocalDate checkOutDate = target.getCheckOutDate();

        return reservationManagementSessionBean.createWalkInReservation(roomType, checkInDate, checkOutDate, occupant);
    }

    @Override
    public List<String> checkInGuest(LocalDateTime checkInDateTime, String passport) {
        Occupant occupant = em.find(Occupant.class, passport);
        
        LocalDate checkInDate = checkInDateTime.toLocalDate();
        LocalTime checkInTime = checkInDateTime.toLocalTime();
        List<String> rooms = new ArrayList<>();
        for (Allocation a : occupant.getAllocations()) {
            if (checkInDate.equals(a.getCheckInDate()) && 
                    checkInTime.isAfter(LocalTime.of(14, 0))) {
                a.checkIn(checkInTime);
                rooms.add(a.getRoom().getRoomId().toString());
            }
        }
        return rooms;
    }

    //Did not control late checkout
    @Override
    public void checkOutGuest(LocalDateTime checkOutDateTime, String passport) {
        Occupant occupant = em.find(Occupant.class, passport);
        LocalDate checkOutDate = checkOutDateTime.toLocalDate();
        LocalTime checkOutTime = checkOutDateTime.toLocalTime();

        for (Allocation a : occupant.getAllocations()) {
            if (checkOutDate.equals(a.getCheckOutDate())
                    && checkOutTime.isBefore(LocalTime.of(12, 0))) {
                a.checkOut(checkOutTime);
            }
        }
    }
}
