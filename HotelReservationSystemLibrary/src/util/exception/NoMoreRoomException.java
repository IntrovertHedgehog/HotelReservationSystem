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
public class NoMoreRoomException extends Exception {

    public NoMoreRoomException() {
    }

    public NoMoreRoomException(String string) {
        super(string);
    }
    
}
