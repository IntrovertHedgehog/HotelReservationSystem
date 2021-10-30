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
import util.exception.InvalidLoginCredentialsException;

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

    public Employee LoginEmployee(String username, String password);

    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialsException;

    public String registerAsGuest(String username, String password, String passport, String name) throws InvalidLoginCredentialsException;
    
}
