/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.user.Occupant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
@Remote
public interface WalkInSessionBeanRemote {

    public List<ReservationSearchResult> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate)  throws InvalidTemporalInputException ;

    public Long walkInReserveRoom(Integer indexOfRoomType, Occupant occupant) throws NoMoreRoomException;

    public List<String> checkInGuest(LocalDateTime checkInDateTime, String passport);
    
    public List<String> checkOutGuest(LocalDateTime checkOutDateTime, String passport);
    
}
