/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.business;

import enumeration.RateType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author Winter
 */
@Entity
public class Rate implements Serializable, Comparable<Rate> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    @Size(min = 1, max = 132)
    @Column(nullable = false, length = 32)
    private String rateName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RateType rateType;
    @Digits(integer = 38, fraction = 2)
    @Min(0)
    @Column(columnDefinition = "DECIMAL(38, 2) NOT NULL")
    private BigDecimal ratePerNight;
    @Column(columnDefinition = "DATE NOT NULL DEFAULT '0000-01-01'")
    private LocalDate periodStart = LocalDate.parse("0000-01-01");
    @Column(columnDefinition = "DATE NOT NULL DEFAULT '9999-12-31'")
    private LocalDate periodEnd  = LocalDate.parse("9999-12-31");
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;

    public Rate() {
    }

    public Rate(String rateName, RoomType roomType, RateType rateType, BigDecimal ratePerNight, LocalDate periodStart, LocalDate periodEnd) {
        this.rateName = rateName;
        this.roomType = roomType;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight.setScale(2, RoundingMode.FLOOR);
        if (rateType == RateType.PROMOTION || rateType == RateType.PEAK) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
        }
    }

    public Rate(String rateName, RoomType roomType, RateType rateType, BigDecimal ratePerNight) {
        this.rateName = rateName;
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.roomType = roomType;
    }
    
    public Long getRateId() {
        return rateId;
    }

    /**
     * @return the rateName
     */
    public String getRateName() {
        return rateName;
    }

    /**
     * @param rateName the rateName to set
     */
    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateId != null ? rateId.hashCode() : 0);
        return hash;
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
     * @return the rateType
     */
    public RateType getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the ratePerNight
     */
    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the periodStart
     */
    public LocalDate getPeriodStart() {
        return periodStart;
    }

    /**
     * @param periodStart the periodStart to set
     */
    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    /**
     * @return the periodEnd
     */
    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    /**
     * @param periodEnd the periodEnd to set
     */
    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }
    
    public Boolean isApplicableFor(LocalDate date) {
        return !date.isBefore(periodStart) && date.isBefore(periodEnd);
    }

    public Boolean isApplicableFor(LocalDate begin, LocalDate end) {
        return (!begin.isBefore(periodStart) && begin.isBefore(periodEnd)) || (!periodStart.isBefore(begin) && periodStart.isBefore(end)) ;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the RateId fields are not set
        if (!(object instanceof Rate)) {
            return false;
        }
        Rate other = (Rate) object;
        if ((this.rateId == null && other.rateId != null) || (this.rateId != null && !this.rateId.equals(other.rateId))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Rate o) {
        if (this.rateType.compareTo(o.getRateType()) != 0) {
            return this.rateType.compareTo(o.getRateType());
        } else if (this.ratePerNight.compareTo(o.getRatePerNight()) != 0){
            return this.ratePerNight.compareTo(o.getRatePerNight());
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Rate | id: %d | name: %s | type: %s | ... | room type: %d", rateId, rateName, rateType.name(), roomType.getRoomTypeId());
    }
    
}
