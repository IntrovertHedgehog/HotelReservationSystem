/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Reservation;
import javax.ejb.Local;

/**
 *
 * @author Winter
 */
@Local
public interface AllocationManagementSessionBeanLocal {
    public Long createAllocation(Reservation reservation);
}
