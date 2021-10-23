/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import entity.business.Rate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Winter
 */
@Stateless
public class RoomManagementSessionBean implements RoomManagementSessionBeanRemote, RoomManagementSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-PU")
    private EntityManager em;
    
    public Long rateCreate(Rate rate) {
        em.persist(rate);
        em.flush();
        
        return rate.getRateId();
    }
    
    public Rate rateDetails(Long id) {
        return em.find(Rate.class, id);
    }
    
    public Rate rateDetails(String name) {
        return (Rate) em.createQuery("SELECT r FROM Rate r WHERE r.name = :name")
                .setParameter("name", name)
                .getSingleResult();
    }
    
    public Boolean rateUpdate(Rate rate) {
        Rate oldRate = em.find(Rate.class, rate.getRateId());
        
        if (oldRate == null) {
            return false;
        }
        
        if (!oldRate.getRoomType().equals(rate.getRoomType())) {
            oldRate.getRoomType().removeRate(rate);
            rate.getRoomType().addRate(rate);
        }
        
        em.merge(rate);
        
        return true;
    }
    
    public Boolean rateDelete(Long id) {
        Rate rate = em.find(Rate.class, id);
        
        if (rate == null) {
            return false;
        }
        
        rate.getRoomType().removeRate(rate);
        em.remove(rate);
        return true;
    }
    
    public List<Rate> rateViewAll() {
        return em.createQuery("SELECT r FROM Rate r")
                .getResultList();
    }
}
