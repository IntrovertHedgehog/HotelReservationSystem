/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import enumeration.BedSize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Winter
 */
@Entity
public class RoomType implements Serializable {

    /**
     * @return the rates
     */

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    @Column(length = 32, nullable = false)
    private String name;
    @Column(length = 512)
    private String description;
    @Column(nullable = false)
    private Double roomSize;
    @Column(nullable = false)
    private BedSize bedsize;
    @Column(nullable = false)
    private Long capacity;
    @Column(length = 64)
    private String amenities;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomType")
    private List<Rate> rates;

    
    public RoomType() {
        rates = new ArrayList<>();
    }

    public RoomType(String name, String description, Double size, BedSize bedsize, Long capacity, String amenities) {
        this();
        this.name = name;
        this.description = description;
        this.roomSize = size;
        this.bedsize = bedsize;
        this.capacity = capacity;
        this.amenities = amenities;
    }
    
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the roomSize
     */
    public Double getRoomSize() {
        return roomSize;
    }

    /**
     * @param roomSize the roomSize to set
     */
    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    /**
     * @return the bed roomSize
     */
    public BedSize getBedsize() {
        return bedsize;
    }

    /**
     * @param bedsize the bed roomSize to set
     */
    public void setBedsize(BedSize bedsize) {
        this.bedsize = bedsize;
    }

    /**
     * @return the capacity
     */
    public Long getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public String getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }
    
    public List<Rate> getRates() {
        return rates;
    }
    
    public Boolean addRate(Rate rate) {
        return rates.add(rate);
    }
    
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room Type " + name + ": " + description;
    }
    
}
