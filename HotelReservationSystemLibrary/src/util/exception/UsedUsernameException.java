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
public class UsedUsernameException extends Exception {

    public UsedUsernameException() {
        super("This username has been taken!");
    }

}
