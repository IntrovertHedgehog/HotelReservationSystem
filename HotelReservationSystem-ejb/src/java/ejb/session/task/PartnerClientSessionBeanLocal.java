/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.business.PartnerReservation;
import entity.business.Reservation;
import entity.user.Partner;
import java.time.LocalDate;
import java.util.List;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotVisibleException;
import util.exception.RoomTypeNotFoundException;
/**
 *
 * @author brianlim
 */
@Local
public interface PartnerClientSessionBeanLocal {
    public Partner loginPartner(String username, String password) throws InvalidLoginCredentialsException;

    public List<ReservationSearchResult> partnerSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) throws InvalidTemporalInputException ;
    
    public Long partnerReserve(Long partnerId, Long roomTypeId, String passport, String name, LocalDate CheckInDate, LocalDate checkOutDate) throws NoMoreRoomException, PartnerNotFoundException, RoomTypeNotFoundException;

    public List<PartnerReservation> viewAllReservations(Long partnerId) throws PartnerNotFoundException;
    
    public PartnerReservation reservationDetails(Long partnerId, Long reservationId) throws PartnerNotFoundException, ReservationNotVisibleException;
}
