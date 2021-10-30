/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.user.Employee;
import entity.user.Guest;
import entity.user.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Winter
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal, AccountManagementSessionBeanRemote {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    
    
    @Override
    public Employee loginEmployee(String username, String password) {
        Employee employee = (Employee) em.createQuery("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        
        return employee;
    }

    @Override
    public Long createEmployee(Employee employee) {
        em.persist(employee);
        em.flush();
        return employee.getEmployeeId();
    }
    
    @Override
    public List<Employee> viewAllEmployees() {
        return em.createQuery("SELECT e FROM Employee e")
                .getResultList();
    }
    
    @Override
    public Long createPartner(Partner partner) {
        em.persist(partner);
        em.flush();
        
        return partner.getPartnerId();
    }
    
    @Override
    public List<Partner> viewAllPartners() {
        return em.createQuery("SELECT p FROM Partner p")
                .getResultList();
    }

    @Override
    public Partner loginPartner(String username, String password) {
        Partner partner = (Partner) em.createQuery("SELECT p FROM Partner p WHERE p.username = :username AND p.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        
        return partner;
    }

    @Override
    public Guest loginGuest(String username, String password) {
        Guest guest = (Guest) em.createQuery("SELECT g FROM Guest g WHERE g.username = :username AND g.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        
        return guest;
    }
}
