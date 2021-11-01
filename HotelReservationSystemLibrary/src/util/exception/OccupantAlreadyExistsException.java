package util.exception;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Winter
 */
public class OccupantAlreadyExistsException extends Exception {

    public OccupantAlreadyExistsException() {
    }

    public OccupantAlreadyExistsException(String string) {
        super(string);
    }
    
}
