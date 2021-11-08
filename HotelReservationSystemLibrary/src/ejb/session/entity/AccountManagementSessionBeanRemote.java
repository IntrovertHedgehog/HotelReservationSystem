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
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Winter
 */
@Remote
public interface AccountManagementSessionBeanRemote {
    public List<Partner> viewAllPartners();

    public Long createPartner(Partner partner);

    public List<Employee> viewAllEmployees();

    public Long createEmployee(Employee employee);

    public Employee loginEmployee(String username, String password) throws InvalidLoginCredentialsException;
    
    public Partner loginPartner(String username, String password) throws InvalidLoginCredentialsException;
    
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialsException;

    public void registerAsGuest(String username, String password, String passport, String name) throws InvalidLoginCredentialsException;
}
