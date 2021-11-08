/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.user.Employee;
import entity.user.Guest;
import entity.user.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Winter
 */
@Local
public interface AccountManagementSessionBeanLocal {
    public Long createEmployee(Employee employee);
    public Long createPartner(Partner partner);
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialsException;
    public Guest registerAsGuest(String username, String password, String passport, String name) throws InvalidLoginCredentialsException;    
}
