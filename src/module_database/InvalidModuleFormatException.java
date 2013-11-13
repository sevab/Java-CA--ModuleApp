/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package module_database;

/**
 *
 * @author sevabaskin
 */
public class InvalidModuleFormatException extends Exception {
	public InvalidModuleFormatException() {
        super();
    }
    public InvalidModuleFormatException(String message) {
        super(message);
    }
}