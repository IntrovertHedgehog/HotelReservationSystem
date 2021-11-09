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
                accountManagementSessionBean.createEmployee(new Employee("admin", "admin", "password", EmployeeType.SYSTEM_ADMINISTRATOR));
                accountManagementSessionBean.createEmployee(new Employee("operator", "operator", "password", EmployeeType.OPERATIONS_MANAGER));
                accountManagementSessionBean.createEmployee(new Employee("seller", "seller", "password", EmployeeType.SALES_MANAGER));
                accountManagementSessionBean.createEmployee(new Employee("officer", "officer", "password", EmployeeType.GUEST_RELATION_OFFICER));
            } catch (UsedUsernameException ex) {
                System.out.println("Username taken!");
            }
        }
        
        List<RoomType> rt = (List<RoomType>) em.createQuery("SELECT rt FROM RoomType rt")
                .setMaxResults(1)
                .getResultList();
        if (rt.isEmpty()) {
            roomManagementSessionBean.createNewRoomType("Deluxe", "very luxury", new BigDecimal("40.0"), BedSize.KING, 2l, "everything you want", RoomTypeConfig.DELUXE_ROOM);
            roomManagementSessionBean.createNewRoomType("Family", "big",new BigDecimal("30.0"), BedSize.QUEEN, 3l, "TV and PS", RoomTypeConfig.FAMILY_ROOM);
            roomManagementSessionBean.createNewRoomType("Trip", "Convenient", new BigDecimal("30.0"), BedSize.TWIN, 2l, "good for short trip", RoomTypeConfig.PREMIER_ROOM);
        }
        
        List<RoomType> roomTypelist = new ArrayList<>();
        roomTypelist.add(em.find(RoomType.class, 1l));
        roomTypelist.add(em.find(RoomType.class, 2l));
        roomTypelist.add(em.find(RoomType.class, 3l));
        
        List<Room> r = (List<Room>) em.createQuery("SELECT r FROM Room r")
                .setMaxResults(1)
                .getResultList();
        if (r.isEmpty()) {
            for (long i = 1l ; i < 4l; i++) {
                RoomType roTy = roomTypelist.get((int) i-1);
                roomManagementSessionBean.createNewRoom(i , 1l, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(i , 2l, roTy, RoomStatus.AVAILABLE);
                roomManagementSessionBean.createNewRoom(i , 3l, roTy, RoomStatus.AVAILABLE);
            }
        }
        
        List<Rate> rate = (List<Rate>) em.createQuery("SELECT r FROM Rate r")
                .setMaxResults(1)
                .getResultList();
        if (rate.isEmpty()) {
            for (RoomType roTy : roomTypelist) {
                roomManagementSessionBean.createRate(new Rate("walk-in rate", roTy, RateType.PUBLISHED, new BigDecimal("20"), LocalDate.parse("2021-12-10"), LocalDate.parse("2021-12-31")));
                roomManagementSessionBean.createRate(new Rate("online rate", roTy, RateType.NORMAL, new BigDecimal("30"), LocalDate.parse("2021-12-01"), LocalDate.parse("2021-12-10")));
                roomManagementSessionBean.createRate(new Rate("peak price", roTy, RateType.PEAK, new BigDecimal("40"), LocalDate.parse("2021-12-20"), LocalDate.parse("2021-12-30")));
                roomManagementSessionBean.createRate(new Rate("cheap ass", roTy, RateType.PROMOTION, new BigDecimal("10"), LocalDate.parse("2021-12-12"), LocalDate.parse("2021-12-31")));
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
        
        List<Reservation> reservations = em.createQuery("SELECT r FROM Reservation r")
                .getResultList();
        if (reservations.isEmpty()) {
            try {
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(0), LocalDate.parse("2021-12-01"), LocalDate.parse("2021-12-03"), em.find(Guest.class, "PPT1101"));
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(1), LocalDate.parse("2021-12-02"), LocalDate.parse("2021-12-05"), em.find(Guest.class, "PPT1102"));
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(2), LocalDate.parse("2021-12-03"), LocalDate.parse("2021-12-06"), em.find(Guest.class, "PPT1103"));
                reservationManagementSessionBean.createOnlineReservation(roomTypelist.get(2), LocalDate.parse("2021-12-04"), LocalDate.parse("2021-12-07"), em.find(Guest.class, "PPT1101"));
            } catch (NoMoreRoomException ex) {
                System.out.println(ex.getMessage());
                
            }
        }
    }
}
