/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.entity.ExceptionReportManagementSessionBeanRemote;
import ejb.session.entity.RoomManagementSessionBeanRemote;
import ejb.session.task.AllocationBotSessionBeanRemote;
import entity.business.ExceptionReport;
import entity.business.Room;
import entity.business.RoomType;
import entity.user.Employee;
import enumeration.BedSize;
import enumeration.RoomStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import keyclass.RoomId;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UpdateRoomException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author brianlim
 */
public class OperationManagerClient {

    private AllocationBotSessionBeanRemote allocatingBotSessionBean;

    private RoomManagementSessionBeanRemote roomManagementSessionBeanRemote;

    private ExceptionReportManagementSessionBeanRemote exceptionReportManagementSessionBeanRemote;

    private Employee currentEmployee;

    public OperationManagerClient() {

    }

    public OperationManagerClient(AllocationBotSessionBeanRemote allocatingBotSessionBean, RoomManagementSessionBeanRemote roomManagementSessionBeanRemote,
            ExceptionReportManagementSessionBeanRemote exceptionReportManagementSessionBeanRemote, Employee currentEmployee) {
        this.allocatingBotSessionBean = allocatingBotSessionBean;
        this.roomManagementSessionBeanRemote = roomManagementSessionBeanRemote;
        this.exceptionReportManagementSessionBeanRemote = exceptionReportManagementSessionBeanRemote;
        this.currentEmployee = currentEmployee;

    }

    public void menuOperationManagerOperations() {
        Scanner sc = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("You are logged in as a " + this.currentEmployee.getEmployeeType().toString());
            System.out.println("1. Create New Room Type");
            System.out.println("2. View Room Type Details");
            System.out.println("3. View All Room Types");
            System.out.println("4. Create New Room");
            System.out.println("5. Update Room");
            System.out.println("6. Delete Room");
            System.out.println("7. View All Rooms");
            System.out.println("8. View Room Allocation Exception Report");
            System.out.println("9. Allocate Room");
            System.out.println("10. Exit \n");
            response = 0;

            while (response < 1 || response > 9) {
                System.out.print(" > ");
                response = Integer.parseInt(sc.nextLine());

                if (response == 1) {
                    System.out.print("Enter new room type name > ");
                    String name = sc.nextLine().trim();

                    System.out.print("Enter new room type description > ");
                    String description = sc.nextLine().trim();
                    System.out.print("Enter new room type room size > ");
                    BigDecimal roomSize = new BigDecimal(sc.nextLine().trim());

                    System.out.println("1. King");
                    System.out.println("2. Queen");
                    System.out.println("3. Full");
                    System.out.println("4. Twin");
                    System.out.print("Enter new room type bed size > ");
                    Integer bedSizeNumber = Integer.parseInt(sc.nextLine());
                    BedSize[] bedSizes = {BedSize.KING, BedSize.QUEEN, BedSize.FULL, BedSize.TWIN};
                    BedSize bedSize = bedSizes[bedSizeNumber - 1];

                    System.out.print("Enter new room type capacity > ");
                    Long capacity = Long.parseLong(sc.nextLine());

                    System.out.print("Enter new room type amenities > ");
                    String amenities = sc.nextLine().trim();

                    System.out.println("**** Room Type");
                    viewAllRoomType();
                    System.out.print("Next Room Type Id (type 0 if none) > ");

                    Long nextRoomTypeId = Long.parseLong(sc.nextLine());
                    RoomType rt;

                    try {
                        if (nextRoomTypeId == 0) {
                            rt = null;
                        } else {
                            rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(nextRoomTypeId);
                        }
                        Long roomTypeId = this.roomManagementSessionBeanRemote.createNewRoomType(rt, name, description, roomSize, bedSize, capacity, amenities);
                        System.out.println("You created a new room type with Room Type ID: " + roomTypeId);

                    } catch (RoomTypeNotFoundException e) {
                        System.out.println("Room Type Not Found !");
                        break;
                    }
                } else if (response == 2) {
                    System.out.print("Enter Room Type ID > ");
                    Long roomTypeId = Long.parseLong(sc.nextLine());
                    RoomType rt;
                    try {
                        rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(roomTypeId);
                    } catch (RoomTypeNotFoundException e) {
                        System.out.println("Room Type Not Found !");
                        break;
                    }

                    System.out.printf("%14s%40s%25s%25s%25s\n", "Room Type ID", "Room Type Name", "Room Quantity", "Status", "Next Room Type");
                    System.out.printf("%14s%40s%25s%25s%25s\n", rt.getRoomTypeId(), rt.getName(), rt.getQuantityAvailable().toString(), rt.getStatus().toString(), rt.getNextRoomType() == null ? null : rt.getNextRoomType().getName());
                    System.out.println("=====================================");
                    System.out.println("1. Update Room Type");
                    System.out.println("2. Delete Room Type");
                    System.out.println("3. Exit\n");
                    System.out.print(" > ");
                    Integer rtDetailsChoice = 0;
                    while (rtDetailsChoice < 1 || rtDetailsChoice > 3) {
                         rtDetailsChoice = Integer.parseInt(sc.nextLine());
                        if (rtDetailsChoice == 1) {
                            updateRoomType(rt);
                        } else if (rtDetailsChoice == 2) {
                            deleteRoomType(rt);
                        } else if (rtDetailsChoice == 3) {
                            break;
                        } else {
                            System.out.println("Invalid option, please try again!\n");
                        }
                    }

                } else if (response == 3) {
                    viewAllRoomType();
                } else if (response == 4) {
                    System.out.print("Enter room floor number > ");
                    Long floorNumber = Long.parseLong(sc.nextLine());
                    System.out.print("Enter room number > ");
                    Long roomNumber = Long.parseLong(sc.nextLine());

                    viewAllRoomType();
                    System.out.print("Enter room's Room Type ID > ");
                    RoomType rt;
                    try {
                        rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(Long.parseLong(sc.nextLine()));
                    } catch (RoomTypeNotFoundException e) {
                        System.out.println("Room Type Not Found !");
                        break;
                    }

                    System.out.println("1. Available");
                    System.out.println("2. Unavailable");
                    System.out.println("3. Disabled");
                    System.out.print("Enter new room status > ");

                    RoomStatus[] roomStatuses = {RoomStatus.AVAILABLE, RoomStatus.UNAVAILABLE, RoomStatus.DISABLE};
                    Integer roomStatusIndex = Integer.parseInt(sc.nextLine());
                    RoomStatus roomStatus = roomStatuses[roomStatusIndex - 1];

                    RoomId roomId = this.roomManagementSessionBeanRemote.createNewRoom(floorNumber, roomNumber, rt, roomStatus);
                    System.out.println("You created a new room  with Room ID: " + roomId.toString());

                } else if (response == 5) {
                    System.out.print("Enter room floor number > ");
                    Long floorNumber = Long.parseLong(sc.nextLine());
                    System.out.print("Enter room number > ");
                    Long roomNumber = Long.parseLong(sc.nextLine());
                    System.out.print("Enter room's Room Type ID > ");
                    RoomType rt;
                    try {
                        rt = this.roomManagementSessionBeanRemote.retrieveRoomTypeByRoomTypeId(Long.parseLong(sc.nextLine()));
                    } catch (RoomTypeNotFoundException e) {
                        System.out.println("Room Type Not Found !");
                        break;
                    }

                    System.out.println("1. Available");
                    System.out.println("2. Unavailable");
                    System.out.print("Enter new room status > ");
                    RoomStatus[] roomStatuses = {RoomStatus.AVAILABLE, RoomStatus.UNAVAILABLE};
                    Integer roomStatusIndex = Integer.parseInt(sc.nextLine());
                    RoomStatus roomStatus = roomStatuses[roomStatusIndex - 1];

                    Room room = new Room(floorNumber, roomNumber, rt, roomStatus);
                    try {
                        this.roomManagementSessionBeanRemote.updateRoom(room);
                    } catch (RoomNotFoundException | UpdateRoomException e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    System.out.println("You updated a room with Room ID: " + room.getRoomId().toString());

                } else if (response == 6) {
                    System.out.print("Enter new room floor number > ");
                    Long floorNumber = Long.parseLong(sc.nextLine());
                    System.out.print("Enter new room number > ");
                    Long roomNumber = Long.parseLong(sc.nextLine());

                    RoomId roomId = new RoomId(floorNumber, roomNumber);
                    try {
                        this.roomManagementSessionBeanRemote.deleteRoom(roomId);
                    } catch (RoomNotFoundException e) {
                        System.out.println(e.getMessage());
                    }

                } else if (response == 7) {
                    List<Room> rooms = this.roomManagementSessionBeanRemote.retrieveAllRooms();

                    System.out.println("*** HoRS Management Client :: Operation Manager Operation :: View List of Rooms ***\n");
                    System.out.printf("%14s%32s%32s%20s\n", "Room ID", "Room Status", "Room Type", "Room is Used");

                    rooms.forEach(r -> {
                        System.out.printf("%14s%32s%32s%20s\n", r.getRoomId().toString(), r.getStatus().toString(), r.getRoomType().getName(), r.isUsed().toString());
                    });

                } else if (response == 8) {
                    List<ExceptionReport> ers = this.exceptionReportManagementSessionBeanRemote.getAllReports();

                    System.out.println("*** HoRS Management Client :: Operation Manager Operation :: View List of Exception Reports ***\n");
                    System.out.printf("14s%40s%25s%25s\n", "Report ID", "Report Status", "Report Reservation", "Report Allocation");

                    for (ExceptionReport er : ers) {
                        System.out.printf("14s%40s%25s%25s\n", er.getReportId().toString(), er.getStatus().name(), er.getReservation(), er.getAllocation());
                    }
                } else if (response == 9) {
                    allocatingBotSessionBean.manualAllocate();
                } else if (response == 10) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 10) {
                break;
            }
        }

    }

    public void viewAllRoomType() {
        List<RoomType> roomTypes = this.roomManagementSessionBeanRemote.retrieveAllRoomTypes();

        System.out.println("*** HoRS Management Client :: Operation Manager Operation :: View List of Room Types ***\n");
        System.out.printf("%14s%40s%25s%25s%25s\n", "Room Type ID", "Room Type Name", "Room Type Quantity", "Status", "Next Room Type");

        roomTypes.forEach(rt -> {
            System.out.printf("%14s%40s%25s%25s%25s\n", rt.getRoomTypeId(), rt.getName(), rt.getQuantityAvailable().toString(), rt.getStatus(), rt.getNextRoomType() == null ? null : rt.getNextRoomType().getName());
        });
    }

    public void updateRoomType(RoomType roomType) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new room type name (Leave Blank If No Change)> ");
        String name = sc.nextLine().trim();
        if (name.length() > 0) {
            roomType.setName(name);
        }

        System.out.print("Enter new room type description (Leave Blank If No Change)> ");
        String description = sc.nextLine().trim();
        if (description.length() > 0) {
            roomType.setDescription(description);
        }

        System.out.print("Enter new room type room size (Type 0 If No Change)> ");
        BigDecimal roomSize = new BigDecimal(sc.nextLine().trim());
        if (!roomSize.equals(0)) {
            roomType.setRoomSize(roomSize);
        }

        System.out.println("1. King");
        System.out.println("2. Queen");
        System.out.println("3. Full");
        System.out.println("4. Twin");
        System.out.print("Enter new room type bed size (Type 0 If No Change)> ");
        Integer bedSizeNumber = Integer.parseInt(sc.nextLine());
        if (bedSizeNumber != 0) {
            BedSize[] bedSizes = {BedSize.KING, BedSize.QUEEN, BedSize.FULL, BedSize.TWIN};
            BedSize bedSize = bedSizes[bedSizeNumber - 1];
            roomType.setBedsize(bedSize);
        }

        System.out.print("Enter new room type capacity (Type 0 If No Change)> ");
        Long capacity = Long.parseLong(sc.nextLine());
        if (capacity != 0) {
            roomType.setCapacity(capacity);
        }

        System.out.print("Enter new room type amenities (Leave Blank If No Change)> ");
        String amenities = sc.nextLine().trim();
        if (amenities.length() > 0) {
            roomType.setAmenities(amenities);
        }

        try {
            this.roomManagementSessionBeanRemote.updateRoomType(roomType);
        } catch (RoomTypeNotFoundException | UpdateRoomTypeException e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteRoomType(RoomType roomType) {
        try {
            this.roomManagementSessionBeanRemote.deleteRoomType(roomType);
        } catch (DeleteRoomTypeException e) {
            System.out.println(e.getMessage());
        }
    }
}
