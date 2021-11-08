  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.entity.AccountManagementSessionBeanRemote;
import ejb.session.entity.ReservationManagementSessionBeanRemote;
import ejb.session.task.OnlineReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author brianlim
 */
public class Main {

    @EJB
    private static ReservationManagementSessionBeanRemote reservationManagementSessionBeanRemote;

    @EJB
    private static OnlineReservationSessionBeanRemote onlineReservationSessionBeanRemote;

    @EJB
    private static AccountManagementSessionBeanRemote accountManagementSessionBeanRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(reservationManagementSessionBeanRemote, onlineReservationSessionBeanRemote, accountManagementSessionBeanRemote);
        mainApp.run();
    }
    
}
