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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.*;

/**
 *
 * @author Winter
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Size(min = 1, max = 32)
    @Column(length = 32)
    private String name;
    @Size(min = 1, max = 32)
    @Column(nullable = false, unique = true, length = 32)
    private String username;
    @Size(min = 1)
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeType employeeType;
    
    public Employee() {
    }

    public Employee(String name, String username, String password, EmployeeType employeeType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.employeeType = employeeType;
    }

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
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
    
    public EmployeeType getEmployeeType() {
        return this.employeeType;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getEmployeeId() != null ? getEmployeeId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.getEmployeeId() == null && other.getEmployeeId() != null) || (this.getEmployeeId() != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee | id = " + getEmployeeId() + " | name = " + name;
    }
    
}
