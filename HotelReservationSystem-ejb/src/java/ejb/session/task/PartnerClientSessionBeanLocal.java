/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.user.Occupant;
import entity.user.Partner;
import java.time.LocalDate;
import java.util.List;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;
import javax.ejb.Local;
/**
 *
 * @author brianlim
 */
@Local
public interface PartnerClientSessionBeanLocal {
    public List<ReservationSearchResult> partnerSearchRoom(LocalDate CheckInDate, LocalDate checkOutDate);
    
    public Long partnerReserve(ReservationSearchResult target, Partner partner, Occupant occupant) throws NoMoreRoomException;
}
