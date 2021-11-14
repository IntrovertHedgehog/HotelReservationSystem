/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.entity.OccupantManagementSessionBeanRemote;
import ejb.session.task.WalkInSessionBeanRemote;
import entity.user.Employee;
import entity.user.Occupant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.OccupantNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
public class GuestRelationOfficerClient {

    private OccupantManagementSessionBeanRemote occupantManagementSessionBeanRemote;
    private WalkInSessionBeanRemote walkInSessionBeanRemote;

    private Employee currentEmployee;

    private static final Scanner sc = new Scanner(System.in);

    public GuestRelationOfficerClient() {

    }

    public GuestRelationOfficerClient(OccupantManagementSessionBeanRemote occupantManagementSessionBeanRemote, WalkInSessionBeanRemote walkInSessionBeanRemote, Employee currentEmployee) {
        this.occupantManagementSessionBeanRemote = occupantManagementSessionBeanRemote;
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
                System.out.print(" > ");
                try {
                    response = Integer.parseInt(sc.nextLine());
                    if (response == 1) {
                        walkInSearchRoom();

                    } else if (response == 2) {
                        checkInGuest();;

                    } else if (response == 3) {
                        checkOutGuest();

                    } else if (response == 4) {
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Unrecognized input format");
                }
            }
            if (response == 4) {
                break;
            }

        }
    }

    public void walkInSearchRoom() {
        try {
            System.out.print("Enter check in date (yyyy-MM-dd) > ");
            LocalDate dateStart = LocalDate.parse(sc.nextLine());
            System.out.print("Enter check out date (yyyy-MM-dd) > ");
            LocalDate dateEnd = LocalDate.parse(sc.nextLine());

            List<ReservationSearchResult> results;
            results = this.walkInSessionBeanRemote.walkInSearchRoom(dateStart, dateEnd);

            System.out.println("*** HoRS Management Client :: Guest Relation Officer Operation :: Search Rooms ***\n");
            System.out.printf("%8s%40s%20s%20s%20s%20s%20s\n", "Index ID", "Room Type Name", "Quantity", "Check In Date", "Check Out Date", "Prevailing Rate", "Client Type");

            Integer counter = 0;
            for (ReservationSearchResult r : results) {
                System.out.printf("%8s%40s%20s%20s%20s%20s%20s\n", counter, r.getRoomType().getName(), r.getQuantity(), r.getCheckInDate(), r.getCheckOutDate(), r.getPrevailRate(), r.getClientType());
                counter++;
            }

            System.out.println("=====================================");
            System.out.println("1. Reserve Room");
            System.out.println("2. Exit\n");
            Integer response = 0;
            while (response < 1 || response > 2) {
                System.out.print(" > ");
                response = Integer.parseInt(sc.nextLine());

                if (response == 1) {
                    walkInReserveRoom();
                } else if (response == 2) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
        } catch (InvalidTemporalInputException ex) {
            System.out.println(ex.getMessage());
        } catch (DateTimeParseException ex) {
            System.out.println("Unrecognised date format");
        }
    }

    public void walkInReserveRoom() {

        System.out.print("Enter occupant passport (min.lenth = 3) > ");
        String passport = sc.nextLine().trim();
        Occupant o = null;
        try {
            o = occupantManagementSessionBeanRemote.retrieveOccupant(passport);
            System.out.println("This occupant has registered before!");
            System.out.println("Occupant name > " + o.getName());
        } catch (OccupantNotFoundException ex) {
        }

        if (o == null) {
            System.out.print("Enter occupant name > ");
            String name = sc.nextLine().trim();

            o = new Occupant(passport, name);
        }

        System.out.print("Enter index of room to book > ");
        Integer index = Integer.parseInt(sc.nextLine());

        try {
            Long reservationId = walkInSessionBeanRemote.walkInReserveRoom(index, o);
            if (reservationId != null) {
                System.out.println("You have successfully reserved a room with Walk-In Reservation ID " + reservationId);

                while (true) {
                    Integer response = 0;

                    while (response < 1 || response > 2) {
                        System.out.println("1. Reserve Another Room");
                        System.out.println("2. Exit\n");
                        System.out.print("> ");
                        response = Integer.parseInt(sc.nextLine());

                        if (response == 1) {
                            System.out.print("Enter index of room to book > ");
                            index = Integer.parseInt(sc.nextLine());

                            try {
                                reservationId = walkInSessionBeanRemote.walkInReserveRoom(index, o);
                                if (reservationId != null) {
                                    System.out.println("You have successfully reserved a room with Walk-In Reservation ID " + reservationId);
                                } else {
                                    System.out.println("No more rooms are available!");
                                }
                            } catch (NoMoreRoomException e) {
                                System.out.println("No more rooms are available!");
                            }
                        } else if (response == 2) {
                            break;
                        } else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }
                    if (response == 2) {
                        break;
                    }
                }

            } else {
                System.out.println("No more rooms are available!");
            }
        } catch (NoMoreRoomException e) {
            System.out.println("No more rooms are available!");
        }

    }

    public void checkInGuest() {
        try {
            System.out.print("Enter check in date (yyyy-MM-dd HH:mm) > ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateStart = LocalDateTime.parse(sc.nextLine(), formatter);

            System.out.print("Enter occupant passport > ");
            String passport = sc.nextLine().trim();
            List<String> roomIds = walkInSessionBeanRemote.checkInGuest(dateStart, passport);
            System.out.println("Occupant room ids");
            roomIds.forEach(r -> System.out.println(r));
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid dattime format");
        }
    }

    public void checkOutGuest() {
        try {
            System.out.print("Enter check out date (yyyy-MM-dd HH:mm) > ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateEnd = LocalDateTime.parse(sc.nextLine(), formatter);

            System.out.print("Enter occupant passport > ");
            String passport = sc.nextLine().trim();
            
            List<String> str = walkInSessionBeanRemote.checkOutGuest(dateEnd, passport);
            
            System.out.println("Room checked out for today: ");
            str.forEach(System.out::println);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid datetime format");
        }
    }
}
