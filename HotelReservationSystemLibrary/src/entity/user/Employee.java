/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.user;

import enumeration.EmployeeType;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Winter
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(length = 32)
    private String name;
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private EmployeeType employeeType;
    
    public Employee() {
    }

    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username, String password) {
        if (this.password.equals(password)) {
            this.username = username;
        }
    }

    public void setPassword(String newPassword, String oldPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
        }
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee | id = " + employeeId + " | name = " + name;
    }
    
}
