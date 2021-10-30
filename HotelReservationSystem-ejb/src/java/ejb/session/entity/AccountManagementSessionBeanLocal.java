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
import javax.ejb.Local;

/**
 *
 * @author Winter
 */
@Local
public interface AccountManagementSessionBeanLocal {

    public List<Partner> viewAllPartners();

    public Long createPartner(Partner partner);

    public List<Employee> viewAllEmployees();

    public Long createEmployee(Employee employee);

    public Employee loginEmployee(String username, String password);
    
    public Partner loginPartner(String username, String password);
    
    public Guest loginGuest(String username, String password);
    
}
