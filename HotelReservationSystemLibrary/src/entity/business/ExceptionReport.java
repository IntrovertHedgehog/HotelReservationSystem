/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import enumeration.ExceptionStatus;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author Winter
 */
@Entity
@Inheritance()
public class ExceptionReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    @Enumerated(EnumType.STRING)
    private ExceptionStatus status;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Reservation reservation;
    @OneToOne
    private Allocation allocation;
    

    public ExceptionReport() {
    }
    
    public ExceptionReport(Reservation reservation) {
        this.reservation = reservation;
        this.status = ExceptionStatus.PENDING;
    }

    public ExceptionReport(Reservation reservation, Allocation allocation) {
        this(reservation);
        this.allocation = allocation;
        this.status = ExceptionStatus.AUTOMATIC;
    }

    public ExceptionStatus getStatus() {
        return status;
    }

    public void setStatus(ExceptionStatus status) {
        this.status = status;
    }
    
    public Long getReportId() {
        return reportId;
    }

    /**
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @return the allocation
     */
    public Allocation getAllocation() {
        return allocation;
    }

    /**
     * @param allocation the allocation to set
     */
    public void setAllocation(Allocation allocation) {
        this.allocation = allocation;
        this.status = ExceptionStatus.RESOLVED;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportId fields are not set
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Exception Report | id= " + reportId + " | reservation id= " + getReservation().getReservationId() + " | allocation id= " + allocation.getAllocationId();
    }
    
}
