/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.entity.RoomManagementSessionBeanRemote;
import entity.business.Rate;
import entity.business.RoomType;
import entity.user.Employee;
import enumeration.RateType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author brianlim
 */
public class SalesManagerClient {

    private RoomManagementSessionBeanRemote roomManagementSessionBeanRemote;

    private Employee currentEmployee;

    private static final Scanner sc = new Scanner(System.in);

    public SalesManagerClient() {

    }

    public SalesManagerClient(RoomManagementSessionBeanRemote roomManagementSessionBeanRemote, Employee currentEmployee) {
        this.roomManagementSessionBeanRemote = roomManagementSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuSalesManagerOperations() {

        Integer response;

        while (true) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("You are logged in as a " + this.currentEmployee.getEmployeeType().toString());
            System.out.println("1. Create New Room Rate");
            System.out.println("2. View Room Rate Details");
            System.out.println("3. View All Room Rates");
            System.out.println("4. Exit \n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print(" > ");
                response = Integer.parseInt(sc.nextLine());

                if (response == 1) {
                    createNewRoomRate();
                } else if (response == 2) {
                    viewRoomRateDetails();

                } else if (response == 3) {
                    viewAllRoomRates();

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

    public void createNewRoomRate() {
        System.out.print("Enter new room rate name > ");
        String name = sc.nextLine();

        List<RoomType> roomTypes = this.roomManagementSessionBeanRemote.retrieveAllRoomTypes();

        System.out.print("*** HoRS Management Client :: Sales Manager Operation :: View List of Room Types ***\n");
        System.out.printf("%3s%40s%40s\n", "Room Type ID", "Room Type Name", "Room Type Quantity");
        roomTypes.forEach(rt -> {
            System.out.printf("%3s%40s%40s\n", rt.getRoomTypeId(), rt.getName(), rt.getQuantityAvailable().toString());
        });
        System.out.print("Enter new room rate's room type ID > ");
        Long roomTypeId = Long.parseLong(sc.nextLine());
        RoomType rt;
        try {
            rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(roomTypeId);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room Type Not Found !");
            return;
        }

        System.out.println("Rate types: PUBLISHED, NORMAL, PEAK, PROMOTION");
        System.out.print("Enter new room rate type > ");
        String rateTypeString = sc.nextLine();
        RateType rateType = RateType.valueOf(rateTypeString);

        System.out.print("Enter new room rate's per night > ");
        BigDecimal ratePerNight = new BigDecimal(sc.nextLine().trim());

        if (rateType != RateType.PUBLISHED && rateType != RateType.NORMAL) {
            System.out.print("Enter new room rate's start date (yyyy-MM-dd) > ");
            LocalDate dateStart = LocalDate.parse(sc.nextLine());
            System.out.print("Enter new room rate's end date (yyyy-MM-dd) > ");
            LocalDate dateEnd = LocalDate.parse(sc.nextLine());
            Rate roomRate = new Rate(name, rt, rateType, ratePerNight, dateStart, dateEnd);
            this.roomManagementSessionBeanRemote.createRate(roomRate);
        } else {
            Rate roomRate = new Rate(name, rt, rateType, ratePerNight);
            this.roomManagementSessionBeanRemote.createRate(roomRate);
        }

    }

    public void viewRoomRateDetails() {
        System.out.print("Enter Room Rate ID > ");
        Long roomRateId = Long.parseLong(sc.nextLine());

        Rate r = this.roomManagementSessionBeanRemote.viewRateDetails(roomRateId);
        System.out.println("*** HoRS Management Client :: Sales Manager Operation :: View Room Rates Details ***\n");
        System.out.printf("%14s%30s%20s%20s%20s%20s\n", "Room Rate ID", "Room Rate Name", "Room Rate Type", "Room Rate Per Night", "Start Date", "End Date");
        System.out.printf("%14s%30s%20s%20s%20s%20s\n", r.getRateId(), r.getRateName(), r.getRateType(), r.getRatePerNight(), r.getPeriodStart(), r.getPeriodEnd());
        System.out.println("=====================================");

        Integer rrDetailsChoice = 0;

        while (rrDetailsChoice < 1 || rrDetailsChoice > 3) {
            System.out.println("1. Update Room Rate");
            System.out.println("2. Delete Room Rate");
            System.out.println("3. Exit\n");
            System.out.println("> ");
            rrDetailsChoice = Integer.parseInt(sc.nextLine());
            
            if (rrDetailsChoice == 1) {
                updateRoomRate(r);
            } else if (rrDetailsChoice == 2) {
                deleteRoomType(r);
            } else if (rrDetailsChoice == 3) {
                break;
            } else {
                System.out.print("Invalid option, please try again!\n");
            }
        }
    }

    public void viewAllRoomRates() {
        List<Rate> rates = this.roomManagementSessionBeanRemote.viewAllRates();

        System.out.println("*** HoRS Management Client :: Sales Manager Operation :: View List of Room Rates ***\n");
        System.out.printf("%14s%40s%30s%30s%20s%20s\n", "Room Rate ID", "Room Rate Name", "Room Rate Type", "Room Rate Per Night", "Start Date", "End Date");
        rates.forEach(r -> {
            System.out.printf("%14s%40s%30s%30s%20s%20s\n", r.getRateId(), r.getRateName(), r.getRateType(), r.getRatePerNight(), r.getPeriodStart(), r.getPeriodEnd());
        });
    }

    public void updateRoomRate(Rate rate) {
        System.out.print("Enter new room rate name (Leave Blank If No Change)> ");
        String name = sc.nextLine();
        if (name.length() > 0) {
            rate.setRateName(name);
        }

        List<RoomType> roomTypes = this.roomManagementSessionBeanRemote.retrieveAllRoomTypes();

        System.out.println("*** HoRS Management Client :: Sales Manager Operation :: View List of Room Types ***\n");
        System.out.printf("%14s%45s%20s\n", "Room Type ID", "Room Type Name", "Room Type Quantity");
        for (RoomType rt : roomTypes) {
            System.out.printf("%14s%45s%20s\n", rt.getRoomTypeId(), rt.getName(), rt.getQuantityAvailable().toString());
        }
        System.out.print("Enter new room rate's room type ID (Type 0 If No Change)> ");

        Long roomTypeId = Long.parseLong(sc.nextLine());
        if (roomTypeId > 0) {
            RoomType rt;
            try {
                rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(roomTypeId);
                rate.setRoomType(rt);
            } catch (RoomTypeNotFoundException e) {
                System.out.println("Room Type Not Found !");
                return;
            }

        }

        System.out.print("Enter new room rate type (Leave Blank If No Change)> ");
        String rateTypeString = sc.nextLine();
        if (rateTypeString.length() > 0) {
            RateType rateType = RateType.valueOf(rateTypeString);
            rate.setRateType(rateType);
        }

        System.out.print("Enter new room rate's per night (Type 0 If No Change)> ");
        BigDecimal ratePerNight = new BigDecimal(sc.nextLine().trim());
        if (!ratePerNight.equals(0)) {
            rate.setRatePerNight(ratePerNight);
        }

        System.out.print("Enter new room rate's start date (yyyy-MM-dd) (Leave Blank If No Change)> ");
        String stringStartDate = sc.nextLine();
        if (stringStartDate.length() > 0) {
            LocalDate dateStart = LocalDate.parse(stringStartDate);
            rate.setPeriodStart(dateStart);
        }

        System.out.print("Enter new room rate's end date (yyyy-MM-dd) (Leave Blank If No Change)> ");
        String stringEndDate = sc.nextLine();
        if (stringEndDate.length() > 0) {
            LocalDate dateEnd = LocalDate.parse(stringEndDate);
            rate.setPeriodEnd(dateEnd);
        }

        this.roomManagementSessionBeanRemote.updateRate(rate);

    }

    public void deleteRoomType(Rate rate) {
        Long rateId = rate.getRateId();
        this.roomManagementSessionBeanRemote.deleteRate(rateId);

    }
}
