/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.entity.AccountManagementSessionBeanLocal;
import ejb.session.entity.ReservationManagementSessionBeanLocal;
import ejb.session.task.PartnerClientSessionBeanLocal;
import entity.business.PartnerReservation;
import entity.user.Occupant;
import entity.user.Partner;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialsException;
import util.exception.NoMoreRoomException;
import util.exception.PartnerNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author brianlim
 */
@WebService(serviceName = "HolidayReservationSystemWebService")
@Stateless()
public class HolidayReservationSystemWebService {

    List<PartnerReservation> partnerReservations; 
    
    @EJB
    private ReservationManagementSessionBeanLocal reservationManagementSessionBeanLocal;

    @EJB
    private PartnerClientSessionBeanLocal partnerClientSessionBeanLocal;

    @EJB
    private AccountManagementSessionBeanLocal accountManagementSessionBeanLocal;

    @WebMethod(operationName = "loginPartner")
    public Partner loginPartner(@WebParam(name = "username") String username,
                                @WebParam(name = "password") String password) throws InvalidLoginCredentialsException {
        //need to detach???
        return accountManagementSessionBeanLocal.loginPartner(username, password);  
    }
    
    @WebMethod(operationName = "partnerSearchRoom")
    public List<ReservationSearchResult> partnerSearchRoom(@WebParam(name = "checkInDate") LocalDate checkInDate,
                                                           @WebParam(name = "checkOutDate") LocalDate checkOutDate) {
        //need to detach???
        return partnerClientSessionBeanLocal.partnerSearchRoom(checkInDate, checkOutDate);  
    }
    
    @WebMethod(operationName = "partnerReserve")
    public Long partnerReserve(@WebParam(name = "target") ReservationSearchResult target,
                                                           @WebParam(name = "partner") Partner partner,
                                                           @WebParam(name = "occupant") Occupant occupant) throws NoMoreRoomException {
        //need to detach???
        return partnerClientSessionBeanLocal.partnerReserve(target, partner, occupant);
    }
    
    @WebMethod(operationName = "viewPartnerReservationDetails")
    public PartnerReservation viewPartnerReservationDetails(@WebParam(name = "index") Integer index){
        //need to detach???
        return this.partnerReservations.get(index);
    }
    
    
    @WebMethod(operationName = "viewAllReservationsByPartner")
    public List<PartnerReservation> viewAllReservationByPartner(@WebParam(name = "partnerId") Long partnerId) throws PartnerNotFoundException {
            this.partnerReservations = reservationManagementSessionBeanLocal.viewAllReservationByPartner(partnerId);
            return this.partnerReservations;
    }
}
