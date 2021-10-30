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
import java.util.Scanner;
import javax.ejb.EJB;

/**
 *
 * @author Winter
 */
public class Main {

    @EJB
    private static WalkInSessionBeanRemote walkInSessionBean;

    @EJB
    private static RoomManagementSessionBeanRemote roomManagementSessionBean;

    @EJB
    private static ReservationManagementSessionBeanRemote reservationManagementSessionBean;

    @EJB
    private static OccupantManagementSessionBeanRemote occupantManagementSessionBean;

    @EJB
    private static ExceptionReportManagementSessionBeanRemote exceptionReportManagementSessionBean;

    @EJB
    private static AccountManagementSessionBeanRemote accountManagementSessionBean;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ip = 0;
        
        while (ip < 1 && ip > 2) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            
            ip = sc.nextInt();
            
            switch (ip) {
                case 1:
                    login();
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }
    
    public static void login() {
        
    }
}
