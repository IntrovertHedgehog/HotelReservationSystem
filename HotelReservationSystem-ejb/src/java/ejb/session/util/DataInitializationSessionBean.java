/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.util;

import ejb.session.entity.AccountManagementSessionBeanLocal;
import ejb.session.entity.ReservationManagementSessionBeanLocal;
import ejb.session.entity.RoomManagementSessionBeanLocal;
import entity.business.Rate;
import entity.business.Reservation;
import entity.business.Room;
import entity.business.RoomType;
import entity.user.Employee;
import entity.user.Guest;
import entity.user.Occupant;
import entity.user.Partner;
import enumeration.BedSize;
import enumeration.EmployeeType;
import enumeration.RateType;
import enumeration.RoomStatus;
import enumeration.RoomTypeConfig;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoMoreRoomException;
import util.exception.UsedUsernameException;

/**
 *
 * @author Winter
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBean;

    @EJB
    private RoomManagementSessionBeanLocal roomManagementSessionBean;

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBean;

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @PostConstruct
    public void initData() {
        List<Employee> emp = (List<Employee>) em.createQuery("SELECT e FROM Employee e")
                .setMaxResults(1)
                .getResultList();
        if (emp.isEmpty()) {
            try {
                accountManagementSessionBean.createEmployee(new Employee("System Administrator", "sysadmin", "password", EmployeeType.SYSTEM_ADMINISTRATOR));
                accountManagementSessionBean.createEmployee(new Employee("Operation Manager", "opmanager", "password", EmployeeType.OPERATIONS_MANAGER));
                accountManagementSessionBean.createEmployee(new Employee("Sales Manager", "salesmanager", "password", EmployeeType.SALES_MANAGER));
                accountManagementSessionBean.createEmployee(new Employee("Guest Relation Officer", "guestrelo", "password", EmployeeType.GUEST_RELATION_OFFICER));
            } catch (UsedUsernameException ex) {
                System.out.println("Username taken!");
            }
        }
        
        List<RoomType> rt = (List<RoomType>) em.createQuery("SELECT rt FROM RoomType rt")
                .setMaxResults(1)
                .getResultList();
        if (rt.isEmpty()) {
            roomManagementSessionBean.createNewRoomType("Deluxe Room", "It said deluxe but it's the least interesting", new BigDecimal("20.0"), BedSize.TWIN, 2l, "everything you want", RoomTypeConfig.DELUXE_ROOM);
            roomManagementSessionBean.createNewRoomType("Premier Room", "> Deluxe", new BigDecimal("30.0"), BedSize.FULL, 2l, "TV and PS", RoomTypeConfig.PREMIER_ROOM);
            roomManagementSessionBean.createNewRoomType("Family Room", "> Premier", new BigDecimal("40.0"), BedSize.QUEEN, 3l, "family dee dee", RoomTypeConfig.FAMILY_ROOM);
            roomManagementSessionBean.createNewRoomType("Junior Suite", "> Family", new BigDecimal("50.0"), BedSize.KING, 5l, "Business I think", RoomTypeConfig.JUNIOR_SUIT);
            roomManagementSessionBean.createNewRoomType("Grand Suite", "> Junior", new BigDecimal("60.0"), BedSize.KING, 6l, "A club can fit in this room", RoomTypeConfig.GRAND_SUIT);
        }
        
        List<RoomType> roomTypelist = new ArrayList<>();
        roomTypelist.add(em.find(RoomType.class, 1l));
        roomTypelist.add(em.find(RoomType.class, 2l));
        roomTypelist.add(em.find(RoomType.class, 3l));
        roomTypelist.add(em.find(RoomType.class, 4l));
        roomTypelist.add(em.find(RoomType.class, 5l));
        
        List<Room> r = (List<Room>) em.createQuery("SELECT r FROM Room r")
                .setMaxResults(1)
                .getResultList();
        if (r.isEmpty()) {
            for (long i = 1l ; i < 6l; i++) {
                RoomType roTy = roomTypelist.get((int) i-1);
                roomManagementSessionBean.createNewRoom(1l , i, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(2l , i, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(3l , i, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(4l , i, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(5l , i, roTy, RoomStatus.AVAILABLE);
            }
        }
        
        List<Rate> rate = (List<Rate>) em.createQuery("SELECT r FROM Rate r")
                .setMaxResults(1)
                .getResultList();
        if (rate.isEmpty()) {
            for (int i = 1; i < 6; i++) {
                RoomType roTy = roomTypelist.get(i - 1);
                roomManagementSessionBean.createRate(new Rate(roTy.getName() + "Published", roTy, RateType.PUBLISHED, new BigDecimal(100 * i), LocalDate.parse("2021-12-10"), LocalDate.parse("2021-12-31")));
                roomManagementSessionBean.createRate(new Rate(roTy.getName() + "Normal", roTy, RateType.NORMAL, new BigDecimal(50 * i), LocalDate.parse("2021-12-01"), LocalDate.parse("2021-12-10")));
            }
        }
        
        List<Guest> guests = em.createQuery("SELECT g FROM Guest g")
                .getResultList();
        if (guests.isEmpty()) {
            try {
                accountManagementSessionBean.registerAsGuest("guest1", "password", "PPT1101", "Gus Johnson");
                accountManagementSessionBean.registerAsGuest("guest2", "password", "PPT1102", "Derek Morrison");
                accountManagementSessionBean.registerAsGuest("guest3", "password", "PPT1103", "Richard NoName");
            } catch (InvalidLoginCredentialsException ex) {
                ex.printStackTrace();
            }
        }
        
        List<Partner> partners = em.createQuery("SELECT p FROM Partner p")
                .getResultList();
        if (partners.isEmpty()) {
            try {
                accountManagementSessionBean.createPartner(new Partner("Nvidia", "nvi", "password"));
                accountManagementSessionBean.createPartner(new Partner("AMD", "amd", "password"));
            } catch (UsedUsernameException ex) {
                System.out.println("Named used!");
            }
        }
        
        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r")
                .getResultList();
        if (reservations.isEmpty()) {
            try {
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(0), LocalDate.parse("2021-12-01"), LocalDate.parse("2021-12-03"), em.find(Guest.class, "PPT1101"));
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(1), LocalDate.parse("2021-12-02"), LocalDate.parse("2021-12-05"), em.find(Guest.class, "PPT1102"));
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(2), LocalDate.parse("2021-12-03"), LocalDate.parse("2021-12-06"), em.find(Guest.class, "PPT1103"));
                reservationManagementSessionBean.createWalkInReservation(roomTypelist.get(2), LocalDate.parse("2021-12-04"), LocalDate.parse("2021-12-07"), em.find(Occupant.class, "PPT1101"));
                reservationManagementSessionBean.createPartnerReservation(roomTypelist.get(1), LocalDate.parse("2021-12-06"), LocalDate.parse("2021-12-09"), em.find(Partner.class, 1l), new Occupant("PPT9128", "Jack Sparrow"));
                reservationManagementSessionBean.createPartnerReservation(roomTypelist.get(0), LocalDate.parse("2021-12-07"), LocalDate.parse("2021-12-08"), em.find(Partner.class, 2l), new Occupant("PPT4231", "Hanma Noname"));
            } catch (NoMoreRoomException ex) {
                System.out.println(ex.getMessage());
                
            }
        }
    }
}
