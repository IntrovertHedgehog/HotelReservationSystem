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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Winter
 */
@Stateless
public class ExceptionReportManagementSessionBean implements ExceptionReportManagementSessionBeanRemote, ExceptionReportManagementSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;

    @Override
    public List<ExceptionReport> getAllReports() {
        return em.createQuery("SELECT r FROM ExceptionReport r")
                .getResultList();
    }

    @Override
    public void createReports(Reservation reservation) {
        ExceptionReport exceptionReport = new ExceptionReport(reservation);
        em.persist(exceptionReport);
    }

    @Override
    public void createReports(Reservation reservation, Allocation allocation) {
        ExceptionReport exceptionReport = new ExceptionReport(reservation, allocation);
        em.persist(exceptionReport);
    }

    public void persist(Object object) {
        em.persist(object);
    }

}
