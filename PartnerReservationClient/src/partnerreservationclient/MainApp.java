/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package partnerreservationclient;

import java.util.Scanner;
import ws.client.InvalidLoginCredentialsException_Exception;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import ws.client.InvalidTemporalInputException_Exception;
import ws.client.Partner;
import ws.client.PartnerReservationService;
import ws.client.PartnerReservationService_Service;
import ws.client.ReservationSearchResult;

/**
 *
 * @author Winter
 */
public class MainApp {
    private Scanner sc = new Scanner(System.in);
    
    public void start() {
        
        OUTER:
        while (true) {
            System.out.println("****** Hotel Reservation Partner Client");
            System.out.println("1. Partner login");
            System.out.println("2. Search reservation");
            System.out.println("3. Exit");
            int input = 0;
            
            while (input < 1 || input > 3) {
                try {
                System.out.print(" > ");
                input = Integer.parseInt(sc.nextLine().trim());
                switch (input) {
                    case 1:
                        loginPartner();
                        break;
                    case 2:
                        searchReservation();
                        break;
                    case 3:
                        break OUTER;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid input");
                }
            }
        }
    }
    
    private void loginPartner() {
        PartnerReservationService_Service service = new PartnerReservationService_Service();
        PartnerReservationService port = service.getPartnerReservationServicePort();
        System.out.print("username > ");
        String username = sc.nextLine().trim();
        System.out.print("password > ");
        String password = sc.nextLine().trim();
        try {
            Partner partner = port.loginPartner(username, password);
            System.out.println("***** You are logged in as " + partner.getName());
            
            LoggedInSession session = new LoggedInSession(partner);
            session.start();
        } catch (InvalidLoginCredentialsException_Exception ex) {
            System.out.println("Invalid Login Credentials: " + ex.getMessage());
        }
    }

    private void searchReservation() {
        PartnerReservationService_Service service = new PartnerReservationService_Service();
        PartnerReservationService port = service.getPartnerReservationServicePort();
        try {
            
            System.out.print("Enter check in date (yyyy-MM-dd) > ");
            LocalDate checkInDate = LocalDate.parse(sc.nextLine());
            System.out.print("Enter check out date (yyyy-MM-dd) > ");
            LocalDate checkOutDate = LocalDate.parse(sc.nextLine());

            List<ReservationSearchResult> results;
            results = port.partnerSearchRoom(checkInDate.toString(), checkOutDate.toString());

            System.out.println("*** HoRS Partner Client :: Search Rooms ***\n");
            System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", "Index ID", "Room Type Name", "Quantity", "Check In Date", "Check Out Date", "Prevailing Rate", "Client Type");

            Integer counter = 0;
            for (ReservationSearchResult r : results) {
                System.out.printf("%8s%20s%20s%20s%20s%20s%20s\n", counter, r.getRoomType().getName(), r.getQuantity(), r.getCheckInDate(), r.getCheckOutDate(), r.getPrevailRate(), r.getClientType());
                counter++;
            }
        } catch (DateTimeParseException ex) {
            System.out.println("Unrecognised date format");
        } catch (InvalidTemporalInputException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
