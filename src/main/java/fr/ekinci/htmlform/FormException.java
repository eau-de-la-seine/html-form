package fr.ekinci.htmlform;

/**
 * 
 * @author Gokan EKINCI
 */
public class FormException extends Exception {
    public FormException(String message){
        super(message);
    }
    
    public FormException(String message, Throwable cause){
        super(message, cause);
    }
}

