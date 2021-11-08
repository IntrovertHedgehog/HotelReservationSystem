/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.entity.AccountManagementSessionBeanRemote;
import entity.user.Employee;
import entity.user.Partner;
import enumeration.EmployeeType;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author brianlim
 */
public class SystemAdministratorClient {

    private AccountManagementSessionBeanRemote accountManagementSessionBeanRemote;
    private Employee currentEmployee;

    public SystemAdministratorClient() {

    }

    public SystemAdministratorClient(AccountManagementSessionBeanRemote accountManagementSessionBeanRemote, Employee employee) {
        this.accountManagementSessionBeanRemote = accountManagementSessionBeanRemote;
        this.currentEmployee = employee;
    }

    public void menuSysAdminOperations() {
        Scanner sc = new Scanner(System.in);
        Integer response;

        while (true) {
            System.out.println("****** Hotel Reservation Management Client ******");
            System.out.println("You are logged in as a " + this.currentEmployee.getEmployeeType().toString());
            System.out.println("1. Create New Employee");
            System.out.println("2. View All Employee");
            System.out.println("3. Create New Partner");
            System.out.println("4. View All Partner");
            System.out.println("5. Exit \n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print(" > ");
                response = Integer.parseInt(sc.nextLine());
                

                if (response == 1) {
                    System.out.print("Enter new employee name > ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter new employee username > ");
                    String username = sc.nextLine().trim();
                    System.out.print("Enter new employee password > ");
                    String password = sc.nextLine().trim();

                    int role = 0;
                    
                    while (role < 1 || role > 4) {
                        System.out.println("****** Employee Access Right");
                        System.out.println("1. System Administrator");
                        System.out.println("2. Operation Manager");
                        System.out.println("3. Sales Manager");
                        System.out.println("4. Guest Relation Officer");

                        System.out.print("Enter new employee type > ");
                        role = Integer.parseInt(sc.nextLine());
                    }
                    
                    EmployeeType employeeType = null;
                    switch (role) {
                        case 1:
                            employeeType = EmployeeType.SYSTEM_ADMINISTRATOR;
                            break;
                        case 2:
                            employeeType = EmployeeType.OPERATIONS_MANAGER;
                            break;
                        case 3:
                            employeeType = EmployeeType.SALES_MANAGER;
                            break;
                        case 4:
                            employeeType = EmployeeType.GUEST_RELATION_OFFICER;
                            break;
                        default:
                            break;
                    }

                    Employee employee = new Employee(name, username, password, employeeType);
                    Long employeeId = this.accountManagementSessionBeanRemote.createEmployee(employee);
                    System.out.println("You created an employee with Employee ID: " + employeeId);

                } else if (response == 2) {
                    List<Employee> employees = this.accountManagementSessionBeanRemote.viewAllEmployees();
                    
                    System.out.println("*** HoRS Management Client :: System Administration Operation :: View List of Employees ***\n");
                    System.out.printf("%20s%20s%30s\n", "Employee ID", "Employee Name", "Employee Type");
                    
                    for (Employee e : employees) {
                        System.out.printf("%20s%20s%30s\n", e.getEmployeeId(), e.getName(), e.getEmployeeType().toString());
                    }
                    
                } else if (response == 3) {
                    System.out.print("Enter new partner name > ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter new partner username > ");
                    String username = sc.nextLine().trim();
                    System.out.print("Enter new partner password > ");
                    String password = sc.nextLine().trim();
                    
                    Partner partner = new Partner(name, username, password);
                    Long partnerId = this.accountManagementSessionBeanRemote.createPartner(partner);
                    System.out.println("You created a new partner with Partner ID: " + partnerId);

                } else if (response == 4) {
                    List<Partner> partners = this.accountManagementSessionBeanRemote.viewAllPartners();
                    System.out.println("*** HoRS Management Client :: System Administration Operation :: View List of Partners ***\n");
                    System.out.printf("%20s%20s\n", "Partner ID", "Partner Name");
                    
                    partners.forEach(p -> {
                        System.out.printf("%20s%20s\n", p.getPartnerId(), p.getName());
                    });
                    
                } else if (response == 5) {
                    break;

                } else {
                    System.out.println("Invalid option, please try again!\n");
                    
                }
            }
            if (response == 5) {
                break;
            }
        }
    }
}
