/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumeration;

/**
 *
 * @author Winter
 */
public enum Status {
    AVAILABLE, // not-occupied, ready to use
    UNAVAILABLE, // occupied or in cleaning process (not allocatable)
    DISABLE // permanently disable or unavailable for other reasons (I think)
}
