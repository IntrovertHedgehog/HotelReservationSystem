/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.entity.AccountManagementSessionBeanRemote;
import ejb.session.entity.ReservationManagementSessionBeanRemote;
import ejb.session.task.OnlineReservationSessionBeanRemote;
import entity.business.OnlineReservation;
import entity.user.Guest;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
public class MainApp {

    private ReservationManagementSessionBeanRemote reservationManagementSessionBeanRemote;
    
    private OnlineReservationSessionBeanRemote onlineReservationSessionBeanRemote;
    
    private AccountManagementSessionBeanRemote accountManagementSessionBeanRemote;

    private Guest currentGuest;

    private static final Scanner sc = new Scanner(System.in);

    public MainApp() {

    }

    public MainApp(ReservationManagementSessionBeanRemote reservationManagementSessionBeanRemote, OnlineReservationSessionBeanRemote onlineReservationSessionBeanRemote, AccountManagementSessionBeanRemote accountManagementSessionBeanRemote) {
        this.reservationManagementSessionBeanRemote = reservationManagementSessionBeanRemote;
        this.onlineReservationSessionBeanRemote = onlineReservationSessionBeanRemote;
        this.accountManagementSessionBeanRemote = accountManagementSessionBeanRemote;
    }

    public void run() {
        Integer response;

        while (true) {
            System.out.println("****** Hotel Reservation Client ******");

            System.out.println("1. Guest Login");
            System.out.println("2. Register As Guest");
            System.out.println("3. Search Hotel Room");
            System.out.println("4. Exit \n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.println(" > ");
                response = sc.nextInt();
                sc.nextLine();

                if (response == 1) {
                    guestLogin();
                    System.out.println("Login successful!\n");
                    loggedInMenu();

                } else if (response == 2) {
                    registerAsGuest();
                    loggedInMenu();

                } else if (response == 3) {
                    searchHotelRoom();

                } else if (response == 4) {
                    break;

                } else {
                    break;

                }
            }
            if (response == 4) {
                break;
            }
        }

    }

    public void loggedInMenu() {
        Integer response;

        System.out.println("****** Hotel Reservation Client ******");
        System.out.println("You are logged in as " + this.currentGuest.getName());
        System.out.println("1. Search Hotel Room");
        System.out.println("2. View My Reservation Details");
        System.out.println("3. View All My Reservations");
        System.out.println("4. Exit \n");
        response = 0;
        
        while (response < 1 || response > 4) {
                System.out.println(" > ");
                response = sc.nextInt();
                sc.nextLine();

                if (response == 1) {
                    searchHotelRoom();

                } else if (response == 2) {
                    viewMyReservationDetails();

                } else if (response == 3) {
                    viewAllMyReservations();

                } else if (response == 4) {
                    break;

                } else {
                    break;

                }
            }

    }

    public void guestLogin() {
        System.out.println("Enter Guest Username > ");
        String username = sc.nextLine();

        System.out.println("Enter Guest Password >");
        String password = sc.nextLine();
        try {
            this.currentGuest = this.onlineReservationSessionBeanRemote.loginGuest(username, password);
        } catch (InvalidLoginCredentialsException e) {
            System.out.println("Invalid Login Credentials!");
        }
        
    }

    public void registerAsGuest() {
        System.out.println("Enter Guest Username > ");
        String username = sc.nextLine().trim();

        System.out.println("Enter Guest Password >");
        String password = sc.nextLine().trim();
        
        System.out.println("Enter Guest Passport >");
        String passport = sc.nextLine().trim();

        System.out.println("Enter Guest Name >");
        String name = sc.nextLine().trim();
        
        try {
            Guest guest = this.accountManagementSessionBeanRemote.registerAsGuest(username, password, passport, name);
            this.currentGuest = guest;
            
        } catch (InvalidLoginCredentialsException e) {
            System.out.println(e.getMessage());
        }
        
        
    }

    public void searchHotelRoom() {
        System.out.println("Enter check in date (yyyy-MM-dd) > ");
        LocalDate dateStart = LocalDate.parse(sc.nextLine());
        System.out.println("Enter check out date (yyyy-MM-dd) > ");
        LocalDate dateEnd = LocalDate.parse(sc.nextLine());
        
        List<ReservationSearchResult> results = this.onlineReservationSessionBeanRemote.onlineSearchRoom(dateEnd, dateStart);

        System.out.println("*** HoRS Management Client :: Online Reservation :: Search Rooms ***\n");
        System.out.printf("\n%8s%20s%20s%20s%20s%20s%20s", "Index ID", "Room Type Name", "Quantity", "Check In Date", "Check Out Date", "Prevailing Rate", "Client Type");
        
        Integer counter = 0;
        for (ReservationSearchResult r : results) {
            System.out.printf("\n%8s%20s%20s%20s%20s%20s%20s", counter, r.getRoomType().getName(), r.getQuantity(), r.getCheckInDate(), r.getCheckOutDate(), r.getPrevailRate(), r.getClientType());
            counter++;
        }

        System.out.println("=====================================");
        System.out.println("1. Reserve Room");
        System.out.println("2. Exit\n");
        Integer response = 0;
        while (response < 1 || response > 2) {
            response = sc.nextInt();
            sc.nextLine();
            
            if(response == 1) {
                reserveHotelRoom();
                
            } else if (response == 2) {
                break;
                
            } else {
                System.out.println("Invalid option, please try again!\\n");
            }
        }
    }

    public void reserveHotelRoom() {
        System.out.println("Enter Index of Room Type");
        Integer index = sc.nextInt();
        Long reservationId;
        try {
            reservationId = this.onlineReservationSessionBeanRemote.onlineReserveRoom(index);
        } catch (NoMoreRoomException e) {
            System.out.println("No more room available!");
            return;
        }
        
        
        System.out.println("You successfully reserved a room type with reservation ID : " + reservationId);
    }

    public void viewMyReservationDetails() {
        System.out.println("Enter the S/N of reservation > ");
        Integer serialNum = sc.nextInt();
        OnlineReservation r;
        try {
            r =this.reservationManagementSessionBeanRemote.viewAllReservationByGuest(this.currentGuest.getPassport()).get(serialNum);
        } catch (GuestNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.println("*** HoRS Management Client :: Online Reservation :: View My Reservation Detail ***\n");
        System.out.printf("\n%3s%20s%20s%20s", "Rerservation ID", "Room Type Name", "Check In Date", "Check Out Date");
        System.out.printf("\n%3s%20s%20s%20s%20s%20s%20s", r.getReservationId(), r.getRoomType(), r.getCheckInDate(), r.getCheckOutDate());
    }

    public void viewAllMyReservations() {
        List<OnlineReservation> onlineReservations;
        try {
            onlineReservations =this.reservationManagementSessionBeanRemote.viewAllReservationByGuest(this.currentGuest.getPassport());
        } catch (GuestNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }
        
        System.out.println("*** HoRS Management Client :: Online Reservation :: View My Reservations ***\n");
        System.out.printf("\n%8s%20s%20s%20s", "S/N", "Room Type Name", "Check In Date", "Check Out Date");
        
        Integer counter = 0;
        for (OnlineReservation r : onlineReservations) {
            System.out.printf("\n%8s%20s%20s%20s%20s%20s%20s", counter, r.getRoomType(), r.getCheckInDate(), r.getCheckOutDate());
            counter++;
        }
    }
}
