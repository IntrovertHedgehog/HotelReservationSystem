/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.entity.AccountManagementSessionBeanRemote;
import ejb.session.entity.ExceptionReportManagementSessionBeanRemote;
import ejb.session.entity.OccupantManagementSessionBeanRemote;
import ejb.session.entity.ReservationManagementSessionBeanRemote;
import ejb.session.entity.RoomManagementSessionBeanRemote;
import ejb.session.task.WalkInSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Winter
 */
public class Main {

    @EJB
    private static WalkInSessionBeanRemote walkInSessionBeanRemote;

    @EJB
    private static RoomManagementSessionBeanRemote roomManagementSessionBeanRemote;

    @EJB
    private static ReservationManagementSessionBeanRemote reservationManagementSessionBeanRemote;

    @EJB
    private static OccupantManagementSessionBeanRemote occupantManagementSessionBeanRemote;

    @EJB
    private static ExceptionReportManagementSessionBeanRemote exceptionReportManagementSessionBeanRemote;

    @EJB
    private static AccountManagementSessionBeanRemote accountManagementSessionBeanRemote;

    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(walkInSessionBeanRemote, roomManagementSessionBeanRemote,
                reservationManagementSessionBeanRemote, occupantManagementSessionBeanRemote,
                        exceptionReportManagementSessionBeanRemote, accountManagementSessionBeanRemote);
        mainApp.run();  
    }  
}
