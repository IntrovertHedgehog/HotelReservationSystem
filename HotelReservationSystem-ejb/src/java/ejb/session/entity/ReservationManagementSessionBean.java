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
import entity.business.WalkInReservation;
import entity.user.Guest;
import entity.user.Occupant;
import entity.user.Partner;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.PartnerNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Stateless
public class ReservationManagementSessionBean implements ReservationManagementSessionBeanRemote, ReservationManagementSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public List<Reservation> viewUnallocatedReservation() {
        return em.createQuery("SELECT r FROM Reservation r WHERE r.isAllocated = false")
                .getResultList();
    }

    @Override
    public List<OnlineReservation> viewAllReservationByGuest(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) {
            throw new GuestNotFoundException(String.format("Guest with id %d does not exist", guestId));
        }
        
        return guest.getOnlineReservations();
    }

    @Override
    public List<PartnerReservation> viewAllReservationByPartner(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        if (partner == null) {
            throw new PartnerNotFoundException(String.format("Partner with id %d does not exist", partnerId));
        }
        
        return partner.getReservations();
    }

    @Override
    public List<ReservationSearchResult> searchReservation(LocalDate checkInDate, LocalDate checkOutDate, ClientType clientType) {
        List<ReservationSearchResult> results = new ArrayList<>();
        List<Object[]> rawResult = em.createQuery("SELECT rt, rt.quantityAvailable - COUNT(r) FROM Reservation r JOIN r.roomType rt WHERE (r.checkInDate <= :checkInDate AND r.checkOutDate > :checkInDate) OR (:checkInDate <= r.checkInDate AND :checkOutDate > r.checkInDate) GROUP BY rt")
                .setParameter("checkInDate", checkInDate)
                .setParameter("checkOutDate", checkOutDate)
                .getResultList();
        for (Object[] obj : rawResult) {
            results.add(new ReservationSearchResult((RoomType) obj[0], (Long) obj[1], checkInDate, checkOutDate, clientType));
        }
        
        return results;
    }

    @Override
    public void createOnlineReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Guest guest) {
        OnlineReservation onlineReservation = new OnlineReservation(roomType, guest, roomType.getRates(), checkInDate, checkOutDate);
        em.persist(onlineReservation);
    }

    @Override
    public void createWalkInReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Occupant occupant) {
        WalkInReservation walkInReservation = new WalkInReservation(roomType, occupant, roomType.getRates(), checkInDate, checkOutDate);
        em.persist(walkInReservation);
    }

    @Override
    public void createPartnerReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Partner partner, Occupant occupant) {
        PartnerReservation partnerReservation = new PartnerReservation(roomType, occupant, partner, roomType.getRates(), checkInDate, checkOutDate);
        em.persist(partnerReservation);
    }
}
