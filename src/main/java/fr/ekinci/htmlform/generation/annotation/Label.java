package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Advancement : 100%
 * @see : https://developer.mozilla.org/fr/docs/Web/HTML/Element/label
 * @author Gokan EKINCI
 */
@Target(ElementType.FIELD)
public @interface Label {
    // Mandatory
    String label();    
    // String f0r(); // useless because implicit in @Input / @Select / 
    
    // HTML5
    String form() default NotUsed.STRING;
}
