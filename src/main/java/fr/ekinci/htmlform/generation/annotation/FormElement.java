package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 
 * @author Gokan EKINCI
 */
@Target(ElementType.FIELD)
public @interface FormElement {
    int   presentationOrder();
    Label label();
}
