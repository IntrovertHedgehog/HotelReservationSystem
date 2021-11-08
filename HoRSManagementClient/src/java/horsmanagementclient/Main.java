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
import entity.user.Employee;
import enumeration.EmployeeType;
import java.util.Scanner;
import javax.ejb.EJB;
import util.exception.InvalidLoginCredentialsException;

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
        
        OUTER:
        while (ip < 1 || ip > 2) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("> ");
            ip = sc.nextInt();
            switch (ip) {
                case 1:
                    login();
                    break;
                case 2:
                    break OUTER;
                default:
                    System.out.println("Invalid command, please try again!\n");
                    break;
            }
        }
    }
    
    public static void login() {
        Scanner sc = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("****** Login ******");
        System.out.print("username> ");
        username = sc.nextLine().trim();
        System.out.print("password> ");
        password = sc.nextLine().trim();
        
        try {
            Employee employee = accountManagementSessionBean.loginEmployee(username, password);
            System.out.println("=====> Successful Login!\n");
            EmployeeType empType = employee.getEmployeeType();
            
            if (null != empType) switch (empType) {
                case SYSTEM_ADMINISTRATOR:
                    SystemAdminOperation session = new SystemAdminOperation(accountManagementSessionBean);
                    session.start();
                    break;
                case OPERATIONS_MANAGER:
                    break;
                case SALES_MANAGER:
                    break;
                case GUEST_RELATION_OFFICER:
                    break;
                default:
                    break;
            }
        } catch (InvalidLoginCredentialsException ex) {
            ex.printStackTrace();
        }
    }
}
