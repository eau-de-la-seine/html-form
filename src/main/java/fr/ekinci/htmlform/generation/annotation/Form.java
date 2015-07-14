package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Advancement : 100%
 * see : https://developer.mozilla.org/fr/docs/Web/HTML/Element/form 
 * @author Gokan EKINCI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Form {
    String acceptCharset() default NotUsed.STRING;
    String action()        default NotUsed.STRING;
    String enctype()       default NotUsed.STRING; // Default value is @see Enctype.APPLICATION
    String id()            default NotUsed.STRING;
    String method()        default NotUsed.STRING; // Default value is @see Method.GET;
    String name()          default NotUsed.STRING;
    String target()        default NotUsed.STRING; // @see Target
    
    // HTML5
    String autocomplete()  default NotUsed.STRING; // Default value is Autocomplete.ON
    boolean novalidate()   default NotUsed.BOOL;
    
    // Deprecated
    // String accept()     default NotUsed.STRING;
}
