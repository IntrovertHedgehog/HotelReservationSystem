/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Winter
 */
public class GuestNotFoundException extends Exception {

    public GuestNotFoundException() {
    }

    public GuestNotFoundException(String string) {
        super(string);
    }
    
}
