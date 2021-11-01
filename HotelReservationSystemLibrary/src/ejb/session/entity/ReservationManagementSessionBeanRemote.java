/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.OnlineReservation;
import entity.business.PartnerReservation;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.PartnerNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Remote
public interface ReservationManagementSessionBeanRemote {
    public List<OnlineReservation> viewAllReservationByGuest(Long guestId) throws GuestNotFoundException ;
    public List<PartnerReservation> viewAllReservationByPartner(Long partnerId) throws PartnerNotFoundException;
    public List<ReservationSearchResult> searchReservation(LocalDate checkInDate, LocalDate checkOutDate, ClientType clientType);
}
