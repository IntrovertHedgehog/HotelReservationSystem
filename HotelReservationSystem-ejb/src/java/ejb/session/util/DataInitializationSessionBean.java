/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.util;

import ejb.session.entity.AccountManagementSessionBeanLocal;
import ejb.session.entity.RoomManagementSessionBeanLocal;
import entity.business.Rate;
import entity.business.Room;
import entity.business.RoomType;
import entity.user.Employee;
import entity.user.Partner;
import enumeration.BedSize;
import enumeration.EmployeeType;
import enumeration.RateType;
import enumeration.RoomStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Winter
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

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
            accountManagementSessionBean.createEmployee(new Employee("admin", "admin", "password", EmployeeType.SYSTEM_ADMINISTRATOR));
        }
        
        List<RoomType> rt = (List<RoomType>) em.createQuery("SELECT rt FROM RoomType rt")
                .setMaxResults(1)
                .getResultList();
        if (rt.isEmpty()) {
            roomManagementSessionBean.createNewRoomType("Deluxe", "very luxury", new BigDecimal("40.0"), BedSize.KING, 2l, "everything you want");
            roomManagementSessionBean.createNewRoomType("Family", "big",new BigDecimal("30.0"), BedSize.QUEEN, 3l, "TV and PS");
            roomManagementSessionBean.createNewRoomType("Trip", "Convenient", new BigDecimal("30.0"), BedSize.TWIN, 2l, "good for short trip");
        }
        
        List<Room> r = (List<Room>) em.createQuery("SELECT r FROM Room r")
                .setMaxResults(1)
                .getResultList();
        if (r.isEmpty()) {
            roomManagementSessionBean.createNewRoom(1l , 1l, em.find(RoomType.class, 1l), RoomStatus.AVAILABLE);
            roomManagementSessionBean.createNewRoom(2l , 2l, em.find(RoomType.class, 2l), RoomStatus.AVAILABLE);
            roomManagementSessionBean.createNewRoom(2l , 4l, em.find(RoomType.class, 2l), RoomStatus.AVAILABLE);
            roomManagementSessionBean.createNewRoom(3l , 3l, em.find(RoomType.class, 3l), RoomStatus.AVAILABLE);
        }
        
        List<Rate> rate = (List<Rate>) em.createQuery("SELECT r FROM Rate r")
                .setMaxResults(1)
                .getResultList();
        if (rate.isEmpty()) {
            roomManagementSessionBean.createRate(new Rate("expensive as fuck", em.find(RoomType.class, 1l), RateType.PEAK, new BigDecimal("30.45"), LocalDate.parse("2021-12-10"), LocalDate.parse("2021-12-31")));
            roomManagementSessionBean.createRate(new Rate("familial", em.find(RoomType.class, 2l), RateType.NORMAL, new BigDecimal("20"), LocalDate.parse("2021-12-10"), LocalDate.parse("2021-12-31")));
            roomManagementSessionBean.createRate(new Rate("cheap ass", em.find(RoomType.class, 3l), RateType.PROMOTION, new BigDecimal("15"), LocalDate.parse("2021-12-10"), LocalDate.parse("2021-12-31")));
        }
        
        List<Partner> p = em.createQuery("SELECT p FROM Partner p")
                .setMaxResults(1)
                .getResultList();
        if (p.isEmpty()) {
            accountManagementSessionBean.createPartner(new Partner("Hexico", "hexico", "password"));
            accountManagementSessionBean.createPartner(new Partner("Metro", "metro", "password"));
        }
    }
}
