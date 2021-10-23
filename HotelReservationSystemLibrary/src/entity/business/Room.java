/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import enumeration.Status;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import keyclass.RoomId;

/**
 *
 * @author Winter
 */
@Entity
@IdClass(RoomId.class)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long floorNumber;
    @Id
    private Long roomNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public Room() {
    }

    public Room(Long floorNumber, Long roomNumber, RoomType roomType, Status status) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.status = status;
    }

    /**
     * @return the floorNumber
     */
    public Long getFloorNumber() {
        return floorNumber;
    }

    /**
     * @return the roomNumber
     */
    public Long getRoomNumber() {
        return roomNumber;
    }
    
    public RoomId getRoomId() {
        return new RoomId(floorNumber, roomNumber);
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    public void setAvailable() {
        this.status = Status.AVAILABLE;
    }

    public void setUnavailable() {
        this.status = Status.UNAVAILABLE;
    }

    public void setDisabled() {
        this.status = Status.DISABLE;
    }

    @Override
    public int hashCode() {
        return getRoomId().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return getRoomId().equals(object);
    }

    @Override
    public String toString() {
        return "entity.business.Room[ id=" + getRoomId() + " ]";
    }

}
