/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.task.PartnerClientSessionBeanLocal;
import entity.business.PartnerReservation;
import entity.user.Partner;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import util.exception.InvalidLoginCredentialsException;
import util.exception.InvalidTemporalInputException;
import util.exception.NoMoreRoomException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotVisibleException;
import util.exception.RoomTypeNotFoundException;
import util.supplement.ReservationSearchResult;

/**
 *
 * @author Winter
 */
@WebService(serviceName = "PartnerReservationService")
@Stateless()
public class PartnerReservationService {

    @EJB
    private PartnerClientSessionBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "loginPartner")
    public Partner loginPartner(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialsException {
        return ejbRef.loginPartner(username, password);
    }

    @WebMethod(operationName = "partnerSearchRoom")
    public List<ReservationSearchResult> partnerSearchRoom(@WebParam(name = "checkInDate") LocalDate checkInDate, @WebParam(name = "checkOutDate") LocalDate checkOutDate) throws InvalidTemporalInputException {
        return ejbRef.partnerSearchRoom(checkInDate, checkOutDate);
    }

    @WebMethod(operationName = "partnerReserve")
    public Long partnerReserve(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "roomTypeId") Long roomTypeId, @WebParam(name = "passport") String passport, @WebParam(name = "name") String name, @WebParam(name = "CheckInDate") LocalDate CheckInDate, @WebParam(name = "checkOutDate") LocalDate checkOutDate) throws NoMoreRoomException, PartnerNotFoundException, RoomTypeNotFoundException {
        return ejbRef.partnerReserve(partnerId, roomTypeId, passport, name, CheckInDate, checkOutDate);
    }

    @WebMethod(operationName = "viewAllReservations")
    public List<PartnerReservation> viewAllReservations(@WebParam(name = "partnerId") Long partnerId) throws PartnerNotFoundException {
        return ejbRef.viewAllReservations(partnerId);
    }

    @WebMethod(operationName = "reservationDetails")
    public PartnerReservation reservationDetails(@WebParam(name = "partnerId") Long partnerId, @WebParam(name = "reservationId") Long reservationId) throws PartnerNotFoundException, ReservationNotVisibleException {
        return ejbRef.reservationDetails(partnerId, reservationId);
    }
    
}
