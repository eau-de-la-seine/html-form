package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * see : https://developer.mozilla.org/fr/docs/Web/HTML/Element/select
 * @author Gokan EKINCI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Select {

}
