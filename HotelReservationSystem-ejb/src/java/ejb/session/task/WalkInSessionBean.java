/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.business.Allocation;
import entity.business.Reservation;
import entity.business.Room;
import entity.business.RoomType;
import entity.business.WalkInReservation;
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
 * @author brianlim
 */
@Stateful
public class WalkInSessionBean implements WalkInSessionBeanRemote, WalkInSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    private List<Object[]> availableRooms;
    private Occupant currentOccupant;

    @Override
    public List<Object[]> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate) {

        this.availableRooms = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.roomType.name = :inRoomType AND r.checkOutDate >= :checkInDate AND r.checkInDate <= :checkOutDate");
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        String[] roomName = {"Deluxe Room", "Premier Room", "Family Room", "Junior Suite", "Grand Suite"};
        Query roomTypeQuery = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :inName");

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
    public Long walkInReserveRoom(Integer indexOfRoomType, String passport, String name, LocalDate checkInDate, LocalDate checkOutDate) {
        
        //set the current occupant
        if (this.currentOccupant == null) {
            Occupant occupant = em.find(Occupant.class, passport);
            
            //check if this occupant exists, if it doesnt, we create and persist this occupant first.
            if (occupant == null) {
                occupant = new Occupant(passport, name);
                em.persist(occupant);
                em.flush();
            }
            this.currentOccupant = occupant;
        }
        
        //find the room type of the chosen room type to be reserved
        RoomType roomType = (RoomType) this.availableRooms.get(indexOfRoomType)[0];

        //create the reservation
        //associate the reservation with the occupant
        Reservation reservation = new WalkInReservation(roomType, this.currentOccupant, roomType.getRates(), checkInDate, checkOutDate);
        this.currentOccupant.getReservations().add(reservation);

        //persist into database
        em.persist(reservation);
        em.merge(this.currentOccupant);
        em.flush();

        //decrease the available rooms for this room type
        this.availableRooms.get(indexOfRoomType)[1] = (int) this.availableRooms.get(indexOfRoomType)[1] - 1;

        return reservation.getReservationId();
    }

    @Override
    public List<String> checkInGuest(LocalDate checkInDate, Occupant occupant) {

        List<String> rooms = new ArrayList<>();
        if (!occupant.getAllocations().isEmpty()) {
            for (Allocation a : occupant.getAllocations()) {
                if (checkInDate.equals(a.getCheckInDate())) {
                    rooms.add(a.getRoom().getRoomId().toString());
                }
            }
        }
        /*
        Exception are dealt with manually
        if(!occupant.getReservations().isEmpty()) {
            //check if there are any exception reports to return to the customer
        }
         */
        return rooms;
    }

    @Override
    public void checkOutGuest(LocalDate checkOutDate, Occupant occupant) {
        Query query = em.createQuery("SELECT a FROM Allocation a WHERE a.checkOutDate = :inCheckOutDate AND a.occupant = :inOccupant");
        query.setParameter("inCheckOutDate", checkOutDate);
        query.setParameter("inOccupant", occupant);

        List<Allocation> allocations = query.getResultList();
        for (Allocation a : allocations) {
            Room tempRoom = a.getRoom();
            tempRoom.setAvailable();
        }
    }
}
