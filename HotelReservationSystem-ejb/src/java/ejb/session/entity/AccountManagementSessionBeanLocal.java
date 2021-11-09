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
import util.exception.UsedUsernameException;

/**
 *
 * @author Winter
 */
@Local
public interface AccountManagementSessionBeanLocal {

    public Long createEmployee(Employee employee) throws UsedUsernameException ;

    public Long createPartner(Partner partner) throws UsedUsernameException ;

    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialsException;

    public Guest registerAsGuest(String username, String password, String passport, String name) throws InvalidLoginCredentialsException;

    public List<Partner> viewAllPartners();

    public List<Employee> viewAllEmployees();

    public Employee loginEmployee(String username, String password) throws InvalidLoginCredentialsException;

    public Partner loginPartner(String username, String password) throws InvalidLoginCredentialsException;

}
