/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.user.Employee;
import entity.user.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Winter
 */
@Local
public interface AccountManagementSessionBeanLocal {

    public Employee employeeLogin(String username, String password);

    public Long employeeCreate(Employee employee);

    public List<Employee> employeeView();

    public List<Partner> partnerView();

    public Long partnerCreate(Partner partner);
    
}
