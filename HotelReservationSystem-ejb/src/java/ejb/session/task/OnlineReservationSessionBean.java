/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.Reservation;
import entity.business.RoomType;
import entity.business.WalkInReservation;
import entity.user.Guest;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Stateful
public class OnlineReservationSessionBean implements OnlineReservationSessionBeanRemote {

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    private List<ReservationSearchResult> searchResults;
    private Guest guest;

    @Override
    public List<ReservationSearchResult> onlineSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) {
        return this.searchResults = reservationManagementSessionBean.searchReservation(checkInDate, checkOutDate, ClientType.ONLINE);
    }

    @Override
    public Long onlineReserveRoom(Integer indexOfRoomType, LocalDate checkInDate, LocalDate checkOutDate) throws NoMoreRoomException {
        RoomType roomType = (RoomType) this.searchResults.get(indexOfRoomType).getRoomType();
        return reservationManagementSessionBean.createOnlineReservation(roomType, checkInDate, checkOutDate, guest);
    }

    @Override
    public void loginGuest(Guest guest) {
       this.guest = em.merge(guest);
    }
}
