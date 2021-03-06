/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import enumeration.BedSize;
import enumeration.RateType;
import enumeration.RoomTypeStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.*;

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
    @OneToOne
    private RoomType nextRoomType;
    @Column(length = 32, nullable = false)
    private String name;
    @Column(length = 512)
    private String description;
    @Column(columnDefinition = "DECIMAL(38,2) NOT NULL")
    private BigDecimal roomSize;
    @Column(nullable = false)
    private BedSize bedsize;
    @Min(0)
    @Column(nullable = false)
    private Long capacity;
    @Size(max = 128)
    @Column(length = 128)
    private String amenities;
    @OneToMany(mappedBy = "roomType")
    private List<Rate> rates;
    @Column(nullable = false)
    private Long quantityAvailable;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomTypeStatus status;
    
    public RoomType() {
        rates = new ArrayList<>();
    }

    public RoomType(RoomType nextRoomType, String name, String description, BigDecimal size, BedSize bedsize, Long capacity, String amenities) {
        this();
        this.nextRoomType = nextRoomType;
        this.name = name;
        this.description = description;
        this.roomSize = size;
        this.bedsize = bedsize;
        this.capacity = capacity;
        this.amenities = amenities;
        this.quantityAvailable = 0l;
        this.status = RoomTypeStatus.UNUSED;
    }
    
    public void nullify() {
        this.rates = null;
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
     * @return the nextRoomType
     */
    public RoomType getNextRoomType() {
        return nextRoomType;
    }

    /**
     * @param nextRoomType the nextRoomType to set
     */
    public void setNextRoomType(RoomType nextRoomType) {
        this.nextRoomType = nextRoomType;
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
    public BigDecimal getRoomSize() {
        return roomSize;
    }

    /**
     * @param roomSize the roomSize to set
     */
    public void setRoomSize(BigDecimal roomSize) {
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
    
    public Boolean removeRate(Rate rate) {
        return rates.remove(rate);
    }
    
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    
    public Long getQuantityAvailable() {
        return quantityAvailable;
    }

    public void incrementQuantity() {
        quantityAvailable++;
    }
    
    public void decrementQuantity() {
        quantityAvailable--;
    }
    
    public RoomTypeStatus getStatus() {
        return status;
    }
    
    public void setUsed() {
        this.status = RoomTypeStatus.USED;
    }
    
    public void setDisable() {
        this.status = RoomTypeStatus.DISABLE;
    }
    
    public Boolean isUsed() {
        return this.status == RoomTypeStatus.USED;
    }
    
    public Boolean isDisable() {
        return this.status == RoomTypeStatus.DISABLE;
    }

    public BigDecimal calculateWalkInRate(LocalDate checkInDate, LocalDate checkOutDate) {
        Rate publishedrate = rates.stream()
                .filter(r -> r.getRateType() == RateType.PUBLISHED)
                .reduce((f, s) -> s)
                .orElse(null);
        
        return publishedrate.getRatePerNight().multiply(new BigDecimal(ChronoUnit.DAYS.between(checkInDate, checkOutDate)));
    }
    
    public BigDecimal calculateOnlineRate(LocalDate checkInDate, LocalDate checkOutDate) {
        List<Rate> rateList = rates.stream()
                .filter(r -> r.getRateType() != RateType.PUBLISHED && r.isApplicableFor(checkInDate, checkOutDate))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
       
        BigDecimal total = new BigDecimal("0").setScale(2);
        for (LocalDate date = checkInDate; date.isBefore(checkOutDate); date = date.plusDays(1)) {
            Rate rate = null;
            for (Rate r : rateList) {
                if (r.isApplicableFor(date)) {
                    if (rate == null || r.compareTo(rate) > 0) {
                        rate = r;
                    }
                }
            }
            
            total = total.add(rate.getRatePerNight());
        } 
        
        return total;
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
