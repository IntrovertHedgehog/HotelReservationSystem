/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Allocation;
import entity.business.ExceptionReport;
import entity.business.Reservation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Winter
 */
@Local
public interface ExceptionReportManagementSessionBeanLocal {
    public List<ExceptionReport> getAllReports();
    public void createReports(Reservation reservation);
    public void createReports(Reservation reservation, Allocation allocation);
}
