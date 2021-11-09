/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.ExceptionReport;
import entity.business.Rate;
import entity.business.Room;
import entity.business.RoomType;
import enumeration.BedSize;
import enumeration.RoomStatus;
import enumeration.RoomTypeConfig;
import enumeration.RoomTypeStatus;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import keyclass.RoomId;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UpdateRoomException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author Winter
 */
@Stateless
public class RoomManagementSessionBean implements RoomManagementSessionBeanRemote, RoomManagementSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    
    @Override
    public Long createRate(Rate rate) {
        em.persist(rate);
        em.flush();
        
        rate.getRoomType().addRate(rate);
        
        return rate.getRateId();
    }
    
    @Override
    public Rate viewRateDetails(Long id) {
        return em.find(Rate.class, id);
    }
    
    @Override
    public Rate viewRateDetails(String name) {
        return (Rate) em.createQuery("SELECT r FROM Rate r WHERE r.rateName = :name")
                .setParameter("name", name)
                .getSingleResult();
    }
    
    @Override
    public Boolean updateRate(Rate rate) {
        Rate oldRate = em.find(Rate.class, rate.getRateId());
        
        if (oldRate == null) {
            return false;
        }
        
        if (!oldRate.getRoomType().equals(rate.getRoomType())) {
            oldRate.getRoomType().removeRate(rate);
            rate.getRoomType().addRate(rate);
        }
        
        em.merge(rate);
        
        return true;
    }
    
    @Override
    public Boolean deleteRate(Long id) {
        Rate rate = em.find(Rate.class, id);
        
        if (rate == null) {
            return false;
        }
        
        rate.getRoomType().removeRate(rate);
        em.remove(rate);
        return true;
    }
    
    @Override
    public List<Rate> viewAllRates() {
        return em.createQuery("SELECT r FROM Rate r")
                .getResultList();
    }

    @Override
    public Long createNewRoomType(String name, String description, BigDecimal size, BedSize bedSize, Long capacity, String amenities, RoomTypeConfig roomTypeConfig) {
        
        RoomType newRoomType = new RoomType(name, description, size, bedSize, capacity, amenities, roomTypeConfig);
        
        em.persist(newRoomType);
        
        em.flush();
        return newRoomType.getRoomTypeId();
    }
    
    
    @Override
    public RoomType viewRoomTypeDetails(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        roomType.getRates().size();
        
        return roomType;
    }
    
    @Override
    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, UpdateRoomTypeException {
        if(roomType != null && roomType.getRoomTypeId()!= null)
        {
            RoomType roomTypeToUpdate = retrieveRoomTypeByRoomTypeId(roomType.getRoomTypeId());
            
            if(roomTypeToUpdate.getName().equals(roomType.getName()))
            {
                em.merge(roomType);
            
            }
            else
            {
                throw new UpdateRoomTypeException("Name of room type record to be updated does not match the existing record");
            }
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type ID not provided for room type to be updated");
        }
        
    }
    
    @Override
    public void deleteRoomType(RoomType roomType) throws DeleteRoomTypeException {
        
        if (!roomType.getRates().isEmpty())
        {
            List<Rate> rates = roomType.getRates();
            for(Rate rate : rates) {
                em.remove(rate);
            }
        }
        
        if(roomType.getStatus() == RoomTypeStatus.UNUSED)
        {
            em.remove(roomType);
        }
        else if (roomType.getStatus() == RoomTypeStatus.USED) 
        {
            roomType.setDisable();
        }
    }

    @Override
    public RoomType retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if(roomType != null)
        {
            return roomType;
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type ID " + roomTypeId + " does not exist!");
        }
        
    }
    
    @Override
    public RoomType retrieveRoomTypeByRoomTypeName(String name) {
         return (RoomType) em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :name")
                .setParameter("name", name)
                .getSingleResult();
        
        
    }
    
    
    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        Query query = em.createQuery("SELECT r FROM RoomType r");
        return query.getResultList();
    }
    
    
    @Override
    public RoomId createNewRoom(Long floorNumber, Long roomNumber, RoomType roomType, RoomStatus status) {
        Room newRoom = new Room(floorNumber, roomNumber, roomType, status);
        em.persist(newRoom);
        em.flush();
        
        return newRoom.getRoomId();
    }
    
    //update details of a particular room record.
    //Room status also updated using this method
    @Override
    public void updateRoom(Room room) throws RoomNotFoundException, UpdateRoomException{
        if(room != null && room.getRoomId()!= null)
        {           
            em.merge(room);
        }
        else
        {
            throw new RoomNotFoundException("Room ID not provided for room to be updated");
        }
    }
    
    @Override
    public void deleteRoom(RoomId roomId) throws RoomNotFoundException {
        Room room = retrieveRoomByRoomId(roomId);
        if(!room.isUsed())
        {
            em.remove(room);
        }
        
        else 
        {
            room.setDisabled();
        }
    }
    
    @Override
    public List<Room> retrieveAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r");
        return query.getResultList();
    }
    
    @Override
    public Room retrieveRoomByRoomId(RoomId roomId) throws RoomNotFoundException {
        Room room = em.find(Room.class, roomId);
        if(room != null)
        {
            return room;
        }
        else
        {
            throw new RoomNotFoundException("Room ID " + roomId.toString() + " does not exist!");
        }
        
    }
    
    
    public List<ExceptionReport> viewRoomAllocationExceptionReport() {
        Query query = em.createQuery("SELECT er FROM ExceptionReport er");
        return query.getResultList();
    }
}
