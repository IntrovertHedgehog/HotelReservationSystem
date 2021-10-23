/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Room;
import entity.business.RoomType;
import enumeration.BedSize;
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
    
}
