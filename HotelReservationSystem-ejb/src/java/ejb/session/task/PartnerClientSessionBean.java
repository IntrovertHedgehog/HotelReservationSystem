/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import ejb.session.entity.ReservationManagementSessionBeanLocal;
import entity.business.PartnerReservation;
import entity.business.RoomType;
import entity.user.Occupant;
import entity.user.Partner;
import enumeration.ClientType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotVisibleException;
import util.exception.RoomTypeNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@Stateless
public class PartnerClientSessionBean implements PartnerClientSessionBeanLocal {

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;
    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public Partner loginPartner(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Partner partner = (Partner) em.createQuery("SELECT p FROM Partner p WHERE p.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();

            if (partner.matchPassword(password)) {
                em.detach(partner);
                partner.hardNullify();
                return partner;
            } else {
                throw new InvalidLoginCredentialsException("Password incorrect");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialsException("Username not found!");
        }
    }

    @Override
    public List<ReservationSearchResult> partnerSearchRoom(LocalDate CheckInDate, LocalDate checkOutDate) throws InvalidTemporalInputException {
        List<ReservationSearchResult> results =  reservationManagementSessionBean.searchReservation(CheckInDate, checkOutDate, ClientType.ONLINE);
        for (ReservationSearchResult r : results) {
            RoomType rt = r.getRoomType();
            em.detach(rt);
            rt.nullify();
        }
        
        return results;
    }

    @Override
    public Long partnerReserve(Long partnerId, Long roomTypeId, String passport, String name, LocalDate checkInDate, LocalDate checkOutDate) throws NoMoreRoomException, PartnerNotFoundException, RoomTypeNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner == null) {
            throw new PartnerNotFoundException("Partner not found of this id");
        }

        RoomType roomType = em.find(RoomType.class, roomTypeId);

        if (roomType == null) {
            throw new RoomTypeNotFoundException("This room type does not exist");
        }

        Occupant occupant = em.find(Occupant.class, passport);

        if (occupant == null) {
            occupant = new Occupant(passport, name);
            em.persist(occupant);
            em.flush();
        }

        return reservationManagementSessionBean.createPartnerReservation(roomType, checkInDate, checkOutDate, partner, occupant);
    }

    @Override
    public List<PartnerReservation> viewAllReservations(Long partnerId) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner == null) {
            throw new PartnerNotFoundException("Partner not found of this id");
        }

        em.detach(partner);
        partner.softNullify();
        return partner.getPartnerReservations();
    }

    @Override
    public PartnerReservation reservationDetails(Long partnerId, Long reservationId) throws PartnerNotFoundException, ReservationNotVisibleException {
        Partner partner = em.find(Partner.class, partnerId);

        if (partner == null) {
            throw new PartnerNotFoundException("Partner not found of this id");
        }

        PartnerReservation reservation = em.find(PartnerReservation.class, reservationId);

        if (reservation == null) {
            throw new ReservationNotVisibleException("Reservation of this id is not found");
        } else if (!reservation.getPartner().equals(partner)) {
            throw new ReservationNotVisibleException("Reservation does not belong to this partner");
        }
        
        em.detach(reservation);
        reservation.nullify();
        return reservation;
    }
}
