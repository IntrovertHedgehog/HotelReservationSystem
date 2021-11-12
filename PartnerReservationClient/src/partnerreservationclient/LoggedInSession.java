/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerreservationclient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import ws.client.InvalidTemporalInputException_Exception;
import ws.client.NoMoreRoomException_Exception;
import ws.client.Partner;
import ws.client.PartnerNotFoundException_Exception;
import ws.client.PartnerReservation;
import ws.client.PartnerReservationService;
import ws.client.PartnerReservationService_Service;
import ws.client.ReservationNotVisibleException_Exception;
import ws.client.ReservationSearchResult;
import ws.client.RoomTypeNotFoundException_Exception;

/**
 *
 * @author Winter
 */
public class LoggedInSession {

    private Scanner sc = new Scanner(System.in);
    private Partner partner;

    public LoggedInSession(Partner partner) {
        this.partner = partner;
    }

    public void start() {
        OUTER:
        while (true) {
            System.out.println("****** Hotel Reservation Partner Client");
            System.out.println("1. Search reservation");
            System.out.println("2. View all reservation");
            System.out.println("3. View reservation details");
            System.out.println("4. Log out");
            int input = 0;
            while (input < 1 || input > 4) {
                System.out.print(" > ");
                input = Integer.parseInt(sc.nextLine().trim());
                switch (input) {
                    case 1:
                        searchReservation();
                        break;
                    case 2:
                        viewAllReservation();
                        break;
                    case 3:
                        viewReservationDetails();
                        break;
                    case 4:
                        break OUTER;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }
        }

    }

    public void searchReservation() {
        PartnerReservationService_Service service = new PartnerReservationService_Service();
        PartnerReservationService port = service.getPartnerReservationServicePort();

        try {
            System.out.print("Enter check in date (yyyy-MM-dd) > ");
            LocalDate checkInDate = LocalDate.parse(sc.nextLine());
            System.out.print("Enter check out date (yyyy-MM-dd) > ");
            LocalDate checkOutDate = LocalDate.parse(sc.nextLine());

            List<ReservationSearchResult> results;
            results = port.partnerSearchRoom(checkInDate.toString(), checkOutDate.toString());

            System.out.println("*** HoRS Management Client :: Guest Relation Officer Operation :: Search Rooms ***\n");
            System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", "Index ID", "Room Type Name", "Quantity", "Check In Date", "Check Out Date", "Prevailing Rate", "Client Type");

            for (ReservationSearchResult r : results) {
                System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", r.getRoomType().getRoomTypeId(), r.getRoomType().getName(), r.getQuantity(), r.getCheckInDate(), r.getCheckOutDate(), r.getPrevailRate(), r.getClientType());
            }

            System.out.println("=====================================");
            System.out.println("1. Reserve Room");
            System.out.println("2. Exit\n");
            Integer response = 0;
            while (response < 1 || response > 2) {
                System.out.print(" > ");
                response = Integer.parseInt(sc.nextLine());

                if (response == 1) {
                    reserve(checkInDate.toString(), checkOutDate.toString());
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
        } catch (DateTimeParseException ex) {
            System.out.println("Unrecognised date format");
        } catch (InvalidTemporalInputException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void reserve(String checkInDate, String checkOutDate) {
        String response = "Y";
        do {
            PartnerReservationService_Service service = new PartnerReservationService_Service();
            PartnerReservationService port = service.getPartnerReservationServicePort();

            System.out.print("Occupant passport > ");
            String passport = sc.nextLine().trim();
            System.out.print("Occupant Name> ");
            String name = sc.nextLine().trim();

            System.out.print("Room Type Id > ");
            Long roomTypeId = Long.parseLong(sc.nextLine().trim());
            
            Long reservationId = null;

            try {
                reservationId = port.partnerReserve(partner.getPartnerId(), roomTypeId, passport, name, checkInDate, checkOutDate);
            } catch (NoMoreRoomException_Exception ex) {
                System.out.println("No more room of this type");
            } catch (PartnerNotFoundException_Exception ex) {
                System.out.println("Partner not existing!");
            } catch (RoomTypeNotFoundException_Exception ex) {
                System.out.println("Invalid room type");
            }

            if (reservationId == null) {
                System.out.println("No more room of this type");
            } else {
                System.out.println("Successfully reserved room with reservation id: " + reservationId);
            }

            System.out.print("Continue? (Y) > ");
            response = sc.nextLine().trim();
        } while (response.equals("Y"));
    }

    public void viewAllReservation() {
        PartnerReservationService_Service service = new PartnerReservationService_Service();
        PartnerReservationService port = service.getPartnerReservationServicePort();

        try {
            List<PartnerReservation> reservations = port.viewAllReservations(partner.getPartnerId());

            System.out.println("***** All reservation booked by this partner");
            System.out.printf("%15s%15s%15s%25s\n", "Reservation ID", "Checkin date", "Checkout date", "Occupant Passport");

            reservations.forEach(
                    r -> System.out.printf("%15s%15s%15s%25s\n", r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(), r.getOccupant().getPassport()));
        } catch (PartnerNotFoundException_Exception ex) {
            System.out.println("Partner not found");
        }

    }

    public void viewReservationDetails() {
        PartnerReservationService_Service service = new PartnerReservationService_Service();
        PartnerReservationService port = service.getPartnerReservationServicePort();

        System.out.print("Reservation Id > ");
        Long reservationId = Long.parseLong(sc.nextLine().trim());

        try {
            PartnerReservation r = port.reservationDetails(partner.getPartnerId(), reservationId);
            System.out.printf("%15s%15s%15s%25s\n", "Reservation ID", "Checkin date", "Checkout date", "Occupant Passport");
            System.out.printf("%15s%15s%15s%25s\n", r.getReservationId(), r.getCheckInDate(), r.getCheckOutDate(), r.getOccupant().getPassport());
        } catch (PartnerNotFoundException_Exception ex) {
            System.out.println("Partner not found");
        } catch (ReservationNotVisibleException_Exception ex) {
            System.out.println("This reservation is not managed by you");
        }
    }

}
