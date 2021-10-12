/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author Winter
 */
@Entity
public class SystemAdministrator extends Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    public SystemAdministrator() {
        super();
    }

    public SystemAdministrator(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public String toString() {
        return "System Administrator -> " + super.toString();
    }
    
}
