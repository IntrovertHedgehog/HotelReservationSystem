/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.OnlineReservation;
import entity.business.PartnerReservation;
import entity.business.Reservation;
import entity.business.RoomType;
import entity.user.Guest;
import entity.user.Occupant;
import entity.user.Partner;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.NoMoreRoomException;
import util.exception.PartnerNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Local
public interface ReservationManagementSessionBeanLocal {
    public List<Reservation> viewUnallocatedReservation();
    public List<OnlineReservation> viewAllReservationByGuest(Long guestId) throws GuestNotFoundException ;
    public List<PartnerReservation> viewAllReservationByPartner(Long partnerId) throws PartnerNotFoundException;
    public List<ReservationSearchResult> searchReservation(LocalDate checkInDate, LocalDate checkOutDate, ClientType clientType);
    public Long createOnlineReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Guest guest) throws NoMoreRoomException;
    public Long createWalkInReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Occupant occupant) throws NoMoreRoomException;
    public Long createPartnerReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Partner partner, Occupant occupant) throws NoMoreRoomException;
    
}
