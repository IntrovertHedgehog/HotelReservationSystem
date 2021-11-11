/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import ejb.session.task.AllocatingBotSessionBeanLocal;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.PartnerNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
@Stateless
public class ReservationManagementSessionBean implements ReservationManagementSessionBeanRemote, ReservationManagementSessionBeanLocal {

    @EJB
    private AllocatingBotSessionBeanLocal allocatingBotSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public List<OnlineReservation> viewAllReservationByGuest(String guestId) throws GuestNotFoundException {
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
    public List<ReservationSearchResult> searchReservation(LocalDate checkInDate, LocalDate checkOutDate, ClientType clientType) throws InvalidTemporalInputException {
        if (!checkInDate.isBefore(checkOutDate)) {
            throw new InvalidTemporalInputException("Check in date must be before checkout date");
        }
        List<ReservationSearchResult> results = new ArrayList<>();
        List<Object[]> rawResult = em.createNativeQuery("SELECT rt.roomTypeId, rt.quantityAvailable - COUNT(r.RESERVATIONID)\n"
                + "FROM roomType rt LEFT JOIN \n"
                + "(SELECT * FROM Reservation r \n"
                + "    WHERE r.checkInDate < ? \n"
                + "    AND r.checkOutDate > ?) r\n"
                + "ON r.ROOMTYPE_ROOMTYPEID = rt.ROOMTYPEID\n"
                + "GROUP BY rt.roomTypeId")
                .setParameter(2, checkInDate)
                .setParameter(1, checkOutDate)
                .getResultList();
        for (Object[] obj : rawResult) {
            RoomType roomType = em.find(RoomType.class, (Long) obj[0]);
            Long quantity = (Long) obj[1];
            results.add(new ReservationSearchResult(roomType, quantity, checkInDate, checkOutDate, clientType));
        }

        return results;
    }

    private void sameDayCheckIn(Reservation reservation) throws NoMoreRoomException {
        if (reservation.getCheckInDate().equals(LocalDate.now()) && LocalTime.now().isAfter(LocalTime.of(2, 0))) {
            Long allocationId = allocatingBotSessionBean.allocate(reservation);
            if (allocationId == null) {
                throw new NoMoreRoomException("No more room for this reservation");
            }
        }
    }

    @Override
    public Long createOnlineReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Guest guest) throws NoMoreRoomException {
        try {
            List<ReservationSearchResult> results = searchReservation(checkInDate, checkOutDate, ClientType.ONLINE);

            for (ReservationSearchResult r : results) {
                if (r.getRoomType().equals(roomType)) {
                    if (r.getQuantity() <= 0) {
                        return null;
                    }

                    break;
                }
            }
            OnlineReservation onlineReservation = new OnlineReservation(roomType, guest, roomType.getRates(), checkInDate, checkOutDate);
            em.persist(onlineReservation);
            em.flush();
            guest.addOnlineReservation(onlineReservation);
            sameDayCheckIn(onlineReservation);
            return onlineReservation.getReservationId();
        } catch (InvalidTemporalInputException ex) {
            return null;
        }
    }

    @Override
    public Long createWalkInReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Occupant occupant) throws NoMoreRoomException {
        try {
            List<ReservationSearchResult> results = searchReservation(checkInDate, checkOutDate, ClientType.ONLINE);

            for (ReservationSearchResult r : results) {
                if (r.getRoomType().equals(roomType)) {
                    if (r.getQuantity() <= 0) {
                        return null;
                    }

                    break;
                }
            }

            if (em.contains(occupant)) {
                occupant = em.merge(occupant);
            } else {
                em.persist(occupant);
            }
            
            Occupant occupantInDb = em.find(Occupant.class, occupant.getPassport());

            if (occupantInDb == null) {
                try {
                    em.persist(occupant);
                    em.flush();
                } catch (ConstraintViolationException ex) {
                    System.out.println("Violated constraints! " + ex.getMessage());
                }
                System.out.println("This occupant is not here before!");
            } else {
                occupant = em.merge(occupant);
                System.out.println("This occupant existed!");
            }
            em.flush();

            WalkInReservation walkInReservation = new WalkInReservation(roomType, occupant, roomType.getRates(), checkInDate, checkOutDate);
            em.persist(walkInReservation);
            em.flush();
            occupant.addReservation(walkInReservation);
            sameDayCheckIn(walkInReservation);
            return walkInReservation.getReservationId();
        } catch (InvalidTemporalInputException ex) {
            return null;
        }
    }

    @Override
    public Long createPartnerReservation(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, Partner partner, Occupant occupant) throws NoMoreRoomException {
        try {
            List<ReservationSearchResult> results = searchReservation(checkInDate, checkOutDate, ClientType.ONLINE);

            for (ReservationSearchResult r : results) {
                if (r.getRoomType().equals(roomType)) {
                    if (r.getQuantity() <= 0) {
                        return null;
                    }

                    break;
                }
            }

            if (em.contains(occupant)) {
                occupant = em.merge(occupant);
            } else {
                em.persist(occupant);
            }

            em.flush();
            PartnerReservation partnerReservation = new PartnerReservation(roomType, occupant, partner, roomType.getRates(), checkInDate, checkOutDate);
            em.persist(partnerReservation);
            em.flush();
            partner.addReservation(partnerReservation);
            occupant.addReservation(partnerReservation);
            sameDayCheckIn(partnerReservation);
            return partnerReservation.getReservationId();
        } catch (InvalidTemporalInputException ex) {
            return null;
        }
    }
}
