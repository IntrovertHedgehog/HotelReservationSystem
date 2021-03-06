/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.AccountManagementSessionBeanLocal;
import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.RoomType;
import entity.user.Guest;
import enumeration.ClientType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.RoomTypeNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Stateful
public class OnlineReservationSessionBean implements OnlineReservationSessionBeanRemote {

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;

    private List<ReservationSearchResult> searchResults;
    private Guest guest;
    
    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public List<ReservationSearchResult> onlineSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) throws InvalidTemporalInputException {
        return this.searchResults = reservationManagementSessionBean.searchReservation(checkInDate, checkOutDate, ClientType.ONLINE);
    }

    @Override
    public Long onlineReserveRoom(Integer indexOfRoomType) throws NoMoreRoomException, RoomTypeNotFoundException {
        
        if (searchResults == null || searchResults.size() <= indexOfRoomType) {
            throw new RoomTypeNotFoundException("Room Type Not Found !");
        }
        ReservationSearchResult target = searchResults.get(indexOfRoomType);
        RoomType roomType = target.getRoomType();
        LocalDate checkInDate = target.getCheckInDate();
        LocalDate checkOutDate = target.getCheckOutDate();
        BigDecimal fee = target.getPrevailRate();
        
        return reservationManagementSessionBean.createOnlineReservation(roomType, checkInDate, checkOutDate, guest, fee);
    }

    @Override
    public Guest loginGuest(String username, String password) throws InvalidLoginCredentialsException {
        return this.guest = accountManagementSessionBean.guestLogin(username, password);
    }
    
    @Override
    public void loginGuest(Guest guest) {
        this.guest = em.merge(guest);
    }
}
