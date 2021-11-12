/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Allocation;
import entity.business.Reservation;
import javax.ejb.Local;

/**
 *
 * @author Winter
 */
@Local
public interface ExceptionReportManagementSessionBeanLocal {
    public void createReports(Reservation reservation);
    public void createReports(Reservation reservation, Allocation allocation);
}
