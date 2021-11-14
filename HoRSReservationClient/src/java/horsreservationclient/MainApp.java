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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.NoReservationFoundException;
import util.exception.RoomTypeNotFoundException;
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
                System.out.print("> ");

                try {
                    response = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input format, input an integer!\n");
                    break;
                }

                if (response == 1) {
                    try {
                        guestLogin();
                        System.out.println("Login successful!\n");
                        loggedInMenu();
                    } catch (InvalidLoginCredentialsException e) {
                        System.out.println("Invalid Login Credentials!\n");
                    }

                } else if (response == 2) {
                    registerAsGuest();
                    loggedInMenu();

                } else if (response == 3) {
                    searchHotelRoom();

                } else if (response == 4) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
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

        while (true) {
            System.out.println("****** Hotel Reservation Client ******");
            System.out.println("You are logged in as " + this.currentGuest.getName());
            System.out.println("1. Search Hotel Room");
            System.out.println("2. View My Reservation Details");
            System.out.println("3. View All My Reservations");
            System.out.println("4. Exit \n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                try {
                    response = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input format, input an integer!\n");
                    break;
                }

                if (response == 1) {
                    searchHotelRoom();

                } else if (response == 2) {
                    try {
                        viewMyReservationDetails();
                    } catch (NoReservationFoundException e) {
                        System.out.println(e.getMessage());
                    }

                } else if (response == 3) {
                    viewAllMyReservations();

                } else if (response == 4) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                    break;

                }
            }
            if (response == 4) {
                break;
            }

        }

    }

    public void guestLogin() throws InvalidLoginCredentialsException {
        System.out.print("Enter Guest Username > ");
        String username = sc.nextLine().trim();

        System.out.print("Enter Guest Password > ");
        String password = sc.nextLine().trim();

        this.currentGuest = this.onlineReservationSessionBeanRemote.loginGuest(username, password);

    }

    public void registerAsGuest() {
        System.out.print("Enter Guest Username > ");
        String username = sc.nextLine().trim();

        System.out.print("Enter Guest Password > ");
        String password = sc.nextLine().trim();

        System.out.print("Enter Guest Passport (min.lenth = 3) > ");
        String passport = sc.nextLine().trim();

        System.out.print("Enter Guest Name > ");
        String name = sc.nextLine().trim();
        System.out.println();

        try {
            Guest guest = this.accountManagementSessionBeanRemote.registerAsGuest(username, password, passport, name);
            this.currentGuest = guest;

        } catch (InvalidLoginCredentialsException e) {
            System.out.println(e.getMessage());
        }

    }

    public void searchHotelRoom() {
        System.out.print("Enter check in date (yyyy-MM-dd) > ");
        LocalDate dateStart;
        try {
            dateStart = LocalDate.parse(sc.nextLine());
        } catch (DateTimeException e) {
            System.out.println("Wrong date input\n");
            return;
        }

        System.out.print("Enter check out date (yyyy-MM-dd) > ");
        LocalDate dateEnd;
        try {
            dateEnd = LocalDate.parse(sc.nextLine());
        } catch (DateTimeException e) {
            System.out.println("Wrong date input\n");
            return;
        }

        List<ReservationSearchResult> results;
        try {
            results = this.onlineReservationSessionBeanRemote.onlineSearchRoom(dateStart, dateEnd);

            System.out.println("*** HoRS Management Client :: Online Reservation :: Search Rooms ***\n");
            System.out.printf("\n%8s%25s%20s%20s%20s%20s%20s", "Index ID", "Room Type Name", "Quantity", "Check In Date", "Check Out Date", "Prevailing Rate", "Client Type");

            Integer counter = 0;
            for (ReservationSearchResult r : results) {
                System.out.printf("\n%8s%25s%20s%20s%20s%20s%20s", counter, r.getRoomType().getName(), r.getQuantity(), r.getCheckInDate(), r.getCheckOutDate(), r.getPrevailRate(), r.getClientType());
                counter++;
            }

            Integer response = 0;
            while (response < 1 || response > 2) {
                System.out.println("\n=====================================");
                System.out.println("1. Reserve Room");
                System.out.println("2. Exit\n");
                System.out.print("> ");
                try {
                    response = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Wrong input format, input an integer!\n");
                }

                if (response == 1) {

                    reserveHotelRoom();

                } else if (response == 2) {
                    return;

                } else {
                    System.out.println("Invalid option, please try again!\n");

                }
            }
        } catch (InvalidTemporalInputException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void reserveHotelRoom() {

        System.out.print("Enter Index of Room Type > ");
        Integer index;

        try {
            index = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format, input an integer!\n");
            return;
        }

        Long reservationId;

        try {
            reservationId = this.onlineReservationSessionBeanRemote.onlineReserveRoom(index);
        } catch (NoMoreRoomException e) {
            System.out.println("No more room available!");
            return;
        } catch (RoomTypeNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (reservationId == null) {
            System.out.println("No more room of this type");
        } else {
            System.out.println("You successfully reserved a room type with reservation ID : " + reservationId);
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
                            reservationId = this.onlineReservationSessionBeanRemote.onlineReserveRoom(index);
                            if (reservationId != null) {
                                System.out.println("You have successfully reserved a room with Walk-In Reservation ID " + reservationId);
                            } else {
                                System.out.println("No more rooms are available!");
                                return;
                            }
                        } catch (NoMoreRoomException e) {
                            System.out.println("No more rooms are available!");
                            return;
                        } catch (RoomTypeNotFoundException e) {
                            System.out.println(e.getMessage());
                            return;
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
        }
    }

    public void viewMyReservationDetails() throws NoReservationFoundException {

        System.out.print("Enter the S/N of reservation (not reservation id)> ");
        Integer serialNum;
        try {
            serialNum = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Wrong input format, input an integer!\n");
            return;
        }
        OnlineReservation r;
        try {
            List<OnlineReservation> onlineReservations = this.reservationManagementSessionBeanRemote.viewAllReservationByGuest(this.currentGuest.getPassport());
            if (onlineReservations == null || onlineReservations.size() <= serialNum || serialNum < 0) {
                throw new NoReservationFoundException("No reservations were found!\n");
            }
            r = onlineReservations.get(serialNum);
        } catch (GuestNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("*** HoRS Management Client :: Online Reservation :: View My Reservation Detail ***\n");
        System.out.printf("%20s%35s%30s%30s%8s\n", "Rerservation ID", "Room Type Name", "Check In Date", "Check Out Date", "Fee");
        System.out.printf("%20s%35s%30s%30s%8.2f\n", r.getReservationId(), r.getRoomType().getName(), r.getCheckInDate().toString(), r.getCheckOutDate().toString(), r.getFee());
        System.out.println();
    }

    public void viewAllMyReservations() {
        List<OnlineReservation> onlineReservations;
        try {
            onlineReservations = this.reservationManagementSessionBeanRemote.viewAllReservationByGuest(this.currentGuest.getPassport());
        } catch (GuestNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("*** HoRS Management Client :: Online Reservation :: View My Reservations ***\n");
        System.out.printf("%8s%45s%30s%30s%8s\n", "S/N", "Room Type Name", "Check In Date", "Check Out Date", "Fee");

        Integer counter = 0;
        for (OnlineReservation r : onlineReservations) {
            System.out.printf("%8s%45s%30s%30s%8.2f\n", counter, r.getRoomType().getName(), r.getCheckInDate().toString(), r.getCheckOutDate().toString(), r.getFee());
            counter++;
        }
        System.out.println();
    }
}
