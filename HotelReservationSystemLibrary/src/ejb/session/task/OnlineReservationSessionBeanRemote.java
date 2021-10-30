/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Winter
 */
@Remote
public interface OnlineReservationSessionBeanRemote {

    public List<Object[]> walkInSearchRoom(LocalDate checkInDate, LocalDate checkOutDate);

    public Long walkInReserveRoom(Integer indexOfRoomType, String username, LocalDate checkInDate, LocalDate checkOutDate);
    
}
