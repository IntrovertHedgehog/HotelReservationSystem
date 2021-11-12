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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UsedUsernameException;

/**
 *
 * @author Winter
 */
@Stateless
public class AccountManagementSessionBean implements AccountManagementSessionBeanLocal, AccountManagementSessionBeanRemote {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public Employee loginEmployee(String username, String password) throws InvalidLoginCredentialsException {
        System.out.println("Attempt to login with credentials: " + username + " | " + password);
        try {
            Employee employee = (Employee) em.createQuery("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            
            return employee;
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialsException("Invalid Login Credential! \n");
        }
    }

    @Override
    public Long createEmployee(Employee employee) throws UsedUsernameException {
        if (!em.createQuery("SELECT e FROM Employee e WHERE e.username = :usr")
                .setParameter("usr", employee.getUsername())
                .getResultList().isEmpty()) {
            throw new UsedUsernameException();
        }
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
    public Long createPartner(Partner partner) throws UsedUsernameException {
        if (!em.createQuery("SELECT e FROM Partner e WHERE e.username = :usr")
                .setParameter("usr", partner.getUsername())
                .getResultList().isEmpty()) {
            throw new UsedUsernameException();
        }
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
    public Partner loginPartner(String username, String password) throws InvalidLoginCredentialsException{
        try {
            Partner partner = (Partner) em.createQuery("SELECT p FROM Partner p WHERE p.username = :username AND p.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();

            partner.getPartnerReservations();
            return partner;
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialsException("Invalid Login Credential! \n");
        }
    }
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialsException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :inUsername");
        query.setParameter("inUsername", username);
        try {
            Guest guest = (Guest) query.getSingleResult();
            
            if (guest.getPassword().equals(password)) {
                guest.getReservations().size();
                guest.getAllocations().size();
                return guest;
            } else {
                throw new InvalidLoginCredentialsException("Invalid Login Credential! \n");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialsException("Invalid Login Credential! \n");
        }
    }

    @Override
    public Guest registerAsGuest(String username, String password, String passport, String name) throws InvalidLoginCredentialsException {
        //Occupant occupant = em.find(Occupant.class, passport);
        Guest newGuest = new Guest(username, password, passport, name);
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            Guest oldGuest = (Guest) query.getSingleResult();
            if (oldGuest != null) {
                throw new InvalidLoginCredentialsException("The username is taken!");
            }
        } catch (NoResultException ex) {
            em.persist(newGuest);
            em.flush();
        }
        return newGuest;
    }
    
 
}
