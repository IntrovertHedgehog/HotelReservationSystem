/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;
import entity.business.Rate;
import entity.business.Room;
import entity.business.RoomType;
import enumeration.BedSize;
import enumeration.RoomStatus;
import enumeration.RoomTypeConfig;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface RoomManagementSessionBeanRemote {

    public Long createNewRoomType(String name, String description, BigDecimal size, BedSize bedSize, Long capacity, String amenities, RoomTypeConfig roomTypeConfig);

    public RoomType viewRoomTypeDetails(Long roomTypeId);

    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, UpdateRoomTypeException;

    public void deleteRoomType(RoomType roomType)throws DeleteRoomTypeException;

    public RoomType retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException;

    public List<RoomType> retrieveAllRoomTypes();

    public RoomId createNewRoom(Long floorNumber, Long roomNumber, RoomType roomType, RoomStatus status);

    public void deleteRoom(RoomId roomId) throws RoomNotFoundException;

    public void updateRoom(Room room) throws RoomNotFoundException, UpdateRoomException;
    
    public Room retrieveRoomByRoomId(RoomId roomId) throws RoomNotFoundException;
    
    public List<Room> retrieveAllRooms();

    public Long createRate(Rate rate);

    public Rate viewRateDetails(Long id);

    public Rate viewRateDetails(String name);

    public Boolean updateRate(Rate rate);

    public Boolean deleteRate(Long id);

    public List<Rate> viewAllRates();

    public RoomType retrieveRoomTypeByRoomTypeName(String name);
}
