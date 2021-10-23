/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Room;
import entity.business.RoomType;
import java.util.List;
import javax.ejb.Remote;
import keyclass.RoomId;
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

    public Long createNewRoomType(String name, String description, Double size, String bedsize, Long capacity, String amenities);

    public RoomType viewRoomTypeDetails(Long roomTypeId);

    public void updateRoomType(RoomType roomType) throws RoomTypeNotFoundException, UpdateRoomTypeException;

    public void deleteRoomType(RoomType roomType);

    public RoomType retrieveRoomTypeByRoomTypeId(Long roomTypeId) throws RoomTypeNotFoundException;

    public List<RoomType> retrieveAllRoomTypes();

    public RoomId createNewRoom(Long floorNumber, Long roomNumber, RoomType roomType);

    public void deleteRoom(Room room);

    public void updateRoom(Room room) throws RoomNotFoundException, UpdateRoomException;
    
}
