/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.business.Reservation;
import entity.business.RoomType;
import entity.business.WalkInReservation;
import entity.user.Guest;
import entity.user.Occupant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Winter
 */
@Stateful
public class OnlineReservationSessionBean implements OnlineReservationSessionBeanRemote, OnlineReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    private List<Object[]> availableRooms;
    private Guest currentGuest;
    
    
    public List<Object[]> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) {

        this.availableRooms = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.roomType.name = :inRoomType AND r.checkOutDate >= :checkInDate AND r.checkInDate <= :checkOutDate");
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        String[] roomName = {"Deluxe Room", "Premier Room", "Family Room", "Junior Suite", "Grand Suite"};
        Query roomTypeQuery = em.createQuery("SELECT rt FROM RoomType WHERE rt.name = :inName");

        for (int i = 0; i < 5; i++) {
            //find total number of reservations for this particular room type
            query.setParameter("inRoomType", roomName[i]);
            List<Reservation> reservations = query.getResultList();
            int numReserved = reservations.size();

            //find the capacity of this particular room type
            roomTypeQuery.setParameter("inName", roomName[i]);
            RoomType roomType = (RoomType) roomTypeQuery.getSingleResult();
            Long capacity = roomType.getCapacity();

            //if there are available rooms, calculate the number of available rooms
            //add this room type and its available number of rooms to the attribute List<Object[]> availableRooms;
            if (capacity > numReserved) {
                Long availRoom = capacity - numReserved;
                Object[] roomTypeAndAvailNumbers = {roomType, availRoom};
                this.availableRooms.add(roomTypeAndAvailNumbers);
            }

        }
        return this.availableRooms;
    }

    @Override
    public Long walkInReserveRoom(Integer indexOfRoomType, String username, LocalDate checkInDate, LocalDate checkOutDate) {
        
        //set the current guest
        if (this.currentGuest == null) {
            Query guestQuery = em.createQuery("SELECT g FROM Guest g WHERE g.username = :inUsername");
            guestQuery.setParameter("inUsername", username);
            Guest guest = (Guest)guestQuery.getSingleResult();
            
            this.currentGuest = guest;
        }
        
        //find the room type of the chosen room type to be reserved
        RoomType roomType = (RoomType) this.availableRooms.get(indexOfRoomType)[0];

        //create the reservation
        //associate the reservation with the occupant
        Reservation reservation = new WalkInReservation(roomType, this.currentGuest, roomType.getRates(), checkInDate, checkOutDate);
        this.currentGuest.getReservations().add(reservation);

        //persist into database
        em.persist(reservation);
        em.merge(this.currentGuest);
        em.flush();

        //decrease the available rooms for this room type
        this.availableRooms.get(indexOfRoomType)[1] = (int) this.availableRooms.get(indexOfRoomType)[1] - 1;

        return reservation.getReservationId();
    }

}
