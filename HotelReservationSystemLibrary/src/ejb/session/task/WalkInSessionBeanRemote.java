/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import entity.user.Occupant;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author brianlim
 */
@Remote
public interface WalkInSessionBeanRemote {

    public List<Object[]> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate);

    public Long walkInReserveRoom(Integer indexOfRoomType, String passport, String name, LocalDate checkInDate, LocalDate checkOutDate);

    public List<String> checkInGuest(LocalDate checkInDate, Occupant occupant);

    public void checkOutGuest(LocalDate checkOutDate, Occupant occupant);
    
}
