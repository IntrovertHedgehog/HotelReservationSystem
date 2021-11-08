/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.task.WalkInSessionBeanRemote;
import entity.user.Employee;
import entity.user.Occupant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.exception.NoMoreRoomException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
public class GuestRelationOfficerClient {

    private WalkInSessionBeanRemote walkInSessionBeanRemote;

    private Employee currentEmployee;

    private static final Scanner sc = new Scanner(System.in);

    public GuestRelationOfficerClient() {

    }

    public GuestRelationOfficerClient(WalkInSessionBeanRemote walkInSessionBeanRemote, Employee currentEmployee) {
        this.walkInSessionBeanRemote = walkInSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuGuestRelationOfficerOperations() {

        Integer response;

        while (true) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("You are logged in as a " + this.currentEmployee.getEmployeeType().toString());
            System.out.println("1. Walk-in Search Room");
            System.out.println("2. Check-in Guest");
            System.out.println("3. Check-out Guest");
            System.out.println("4. Exit \n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.println(" > ");
                response = sc.nextInt();
                sc.nextLine();

                if (response == 1) {
                    walkInSearchRoom();

                } else if (response == 2) {
                    checkInGuest();;

                } else if (response == 3) {
                    checkOutGuest();

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

    public void walkInSearchRoom() {
        System.out.println("Enter check in date (yyyy-MM-dd) > ");
        LocalDate dateStart = LocalDate.parse(sc.nextLine());
        System.out.println("Enter check out date (yyyy-MM-dd) > ");
        LocalDate dateEnd = LocalDate.parse(sc.nextLine());

        List<ReservationSearchResult> results = this.walkInSessionBeanRemote.walkInSearchRoom(dateStart, dateEnd);

        System.out.println("*** HoRS Management Client :: Guest Relation Officer Operation :: Search Rooms ***\n");
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
                walkInReserveRoom();
                
            } else if (response == 2) {
                break;
                
            } else {
                System.out.println("Invalid option, please try again!\\n");
            }
        }
    }

    public void walkInReserveRoom() {
        
        System.out.println("Enter occupant passport > ");
        String passport = sc.nextLine();
        System.out.println("Enter occupant name > ");
        String name = sc.nextLine();
        
        Occupant o = new Occupant(passport, name);
        
        System.out.println("Enter index of room to book > ");
        Integer index = sc.nextInt();
        sc.nextLine();
        
        try {
            Long reservationId = walkInSessionBeanRemote.walkInReserveRoom(index, o);
            System.out.println("You have successfully reserved a room with Walk-In Reservation ID " + reservationId);
            
        } catch (NoMoreRoomException e) {
            System.out.println("No more rooms are available!");
        }
        
    }

    public void checkInGuest() {
        System.out.println("Enter check in date (yyyy-MM-dd HH:mm) > ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateStart = LocalDateTime.parse(sc.nextLine(), formatter);
        
        System.out.println("Enter occupant passport > ");
        String passport = sc.nextLine();
        walkInSessionBeanRemote.checkInGuest(dateStart, passport);
    }

    public void checkOutGuest() {
        System.out.println("Enter check out date (yyyy-MM-dd HH:mm) > ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateEnd = LocalDateTime.parse(sc.nextLine(), formatter);
        
        System.out.println("Enter occupant passport > ");
        String passport = sc.nextLine();
        walkInSessionBeanRemote.checkOutGuest(dateEnd, passport);

    }
}
