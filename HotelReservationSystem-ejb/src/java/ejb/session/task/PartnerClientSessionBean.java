/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.AccountManagementSessionBeanLocal;
import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.RoomType;
import entity.user.Occupant;
import entity.user.Partner;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Stateless
public class PartnerClientSessionBean implements PartnerClientSessionBeanRemote, PartnerClientSessionBeanLocal {

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;
    
    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    @Override
    public List<ReservationSearchResult> partnerSearchRoom(LocalDate CheckInDate, LocalDate checkOutDate) throws InvalidTemporalInputException {
        return reservationManagementSessionBean.searchReservation(CheckInDate, checkOutDate, ClientType.ONLINE);
    }

    @Override
    public Long partnerReserve(ReservationSearchResult target, Partner partner, Occupant occupant) throws NoMoreRoomException {
        RoomType roomType = target.getRoomType();
        LocalDate checkInDate = target.getCheckInDate();
        LocalDate checkOutDate = target.getCheckOutDate();
        return reservationManagementSessionBean.createPartnerReservation(roomType, checkInDate, checkOutDate, partner, occupant);
    }
}
