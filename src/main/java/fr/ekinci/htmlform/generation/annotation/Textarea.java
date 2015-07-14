package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 * Advancement : 100% 
 * see : https://developer.mozilla.org/fr/docs/Web/HTML/Element/Textarea
 * @author Gokan EKINCI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Textarea {
    // Mandatory
    FormElement formElement();
    String  id();    
    String  name();
    
    int     cols()               default 20; // HTML5 default value is 20
    boolean disabled()           default NotUsed.BOOL;
    boolean readonly()           default NotUsed.BOOL;
    int     rows()               default 2;
    int     selectionEnd()       default NotUsed.INT;
    int     selectionStart()     default NotUsed.INT;
    
    // HTML 5
    String  autocomplete()       default NotUsed.STRING; // Possible values are "on" & "off" @see Autocomplete
    boolean autofocus()          default NotUsed.BOOL;
    String  form()               default NotUsed.STRING;
    int     maxlength()          default NotUsed.INT;    // Internal default value, which means "not used"
    int     minlength()          default NotUsed.INT;    // Internal default value, which means "not used"
    String  placeholder()        default NotUsed.STRING;
    boolean required()           default NotUsed.BOOL;
    String  selectionDirection() default NotUsed.STRING; // @see SelectionDirection
    String  spellcheck()         default NotUsed.STRING; // @see Spellcheck
    String  wrap()               default NotUsed.STRING; // @see Wrap 
}
