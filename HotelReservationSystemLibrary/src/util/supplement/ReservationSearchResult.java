/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.supplement;

import entity.business.RoomType;
import enumeration.ClientType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Winter
 */
@XmlRootElement(name="ReservationSearchResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservationSearchResult implements Serializable {

    private RoomType roomType;
    private Long quantity;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate checkInDate;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate checkOutDate;
    private BigDecimal prevailRate;
    private ClientType clientType;

    public ReservationSearchResult() {
    }

    public ReservationSearchResult(RoomType roomType, Long quantity, LocalDate checkInDate, LocalDate checkOutDate, ClientType clientType) {
        this.roomType = roomType;
        this.quantity = quantity;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.clientType = clientType;
        if (clientType == ClientType.WALKIN) {
            this.prevailRate = roomType.calculateWalkInRate(checkInDate, checkOutDate);
        } else {
            this.prevailRate = roomType.calculateOnlineRate(checkInDate, checkOutDate);
        }
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDate getCheckInDate() {
        return this.checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return this.checkOutDate;
    }

    public BigDecimal getPrevailRate() {
        return prevailRate;
    }

    public ClientType getClientType() {
        return clientType;
    }
    
    
    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Long quantity) {
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(LocalDate checkInDate) {
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(LocalDate checkOutDate) {
    }

    /**
     * @param prevailRate the prevailRate to set
     */
    public void setPrevailRate(BigDecimal prevailRate) {
    }

    /**
     * @param clientType the clientType to set
     */
    public void setClientType(ClientType clientType) {
    }
}
