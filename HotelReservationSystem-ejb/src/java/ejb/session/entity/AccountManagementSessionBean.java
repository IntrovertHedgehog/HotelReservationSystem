/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.user.Employee;
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
public class AccountManagementSessionBean implements AccountManagementSessionBeanRemote {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    
    
    public Employee employeeLogin(String username, String password) {
        Employee employee = (Employee) em.createQuery("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
        
        return employee;
    }

    public Long employeeCreate(Employee employee) {
        em.persist(employee);
        em.flush();
        return employee.getEmployeeId();
    }
    
    public List<Employee> employeeView() {
        return em.createQuery("SELECT e FROM Employee e")
                .getResultList();
    }
    
    public Long partnerCreate(Partner partner) {
        em.persist(partner);
        em.flush();
        
        return partner.getPartnerId();
    }
    
    public List<Partner> partnerView() {
        return em.createQuery("SELECT p FROM Partner p")
                .getResultList();
    }
}
