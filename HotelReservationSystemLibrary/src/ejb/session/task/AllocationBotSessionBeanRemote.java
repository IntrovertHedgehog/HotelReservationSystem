/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.task;

import java.time.LocalDate;
import javax.ejb.Remote;

/**
 *
 * @author Winter
 */
@Remote
public interface AllocationBotSessionBeanRemote {
    public void manualAllocate();
}
