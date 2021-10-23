/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keyclass;

import java.io.Serializable;

/**
 *
 * @author Winter
 */
public class RoomId implements Serializable {

    private final Long floorNumber;
    private final Long roomNumber;

    public RoomId(Long floorNumber, Long roomNumber) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
    }
    
    private String getId() {
        return String.format("%d-%d", floorNumber, roomNumber);
    }

    @Override
    public int hashCode() {
        String id = getId();
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomId)) {
            return false;
        }
        RoomId other = (RoomId) object;
        String id = getId();
        String otherId = other.getId();
        if ((id == null && otherId != null) || (id != null && !id.equals(otherId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Room ID: " + floorNumber + "-" + roomNumber;
    }
}
