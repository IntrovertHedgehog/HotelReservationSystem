/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.entity;

import util.exception.OccupantAlreadyExistsException;
import entity.user.Occupant;
import javax.ejb.Remote;
import util.exception.OccupantNotFoundException;

/**
 *
 * @author Winter
 */
@Remote
public interface OccupantManagementSessionBeanRemote {
    public Occupant retrieveOccupant(String passport) throws OccupantNotFoundException;

    public Occupant createOccupant(Occupant occupant) throws OccupantAlreadyExistsException;
}
