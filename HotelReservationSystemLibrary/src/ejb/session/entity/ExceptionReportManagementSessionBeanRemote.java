/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.ExceptionReport;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Winter
 */
@Remote
public interface ExceptionReportManagementSessionBeanRemote {
    public List<ExceptionReport> getAllReports();
}
