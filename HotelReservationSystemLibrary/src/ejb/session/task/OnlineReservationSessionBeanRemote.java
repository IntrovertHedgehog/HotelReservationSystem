/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.user.Guest;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.RoomTypeNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Remote
public interface OnlineReservationSessionBeanRemote {

    public Guest loginGuest(String username, String password) throws InvalidLoginCredentialsException;
    
    public List<ReservationSearchResult> onlineSearchRoom(LocalDate checkInDate, LocalDate checkOutDate)  throws InvalidTemporalInputException ;

    public Long onlineReserveRoom(Integer indexOfRoomType) throws NoMoreRoomException, RoomTypeNotFoundException;    

    public void loginGuest(Guest guest);
}
