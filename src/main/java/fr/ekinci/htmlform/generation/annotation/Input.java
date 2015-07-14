package fr.ekinci.htmlform.generation.annotation;

import java.lang.annotation.Repeatable;


/**
 * Advancement : X %
 * see : https://developer.mozilla.org/fr/docs/Web/HTML/Element/input 
 * @author Gokan EKINCI
 */
/*
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
*/
@Repeatable(Inputs.class) 
public @interface Input {
    // Mandatory
    FormElement formElement();
    String  id();
    String  name();
    String  type();
           
    String  accept()             default NotUsed.STRING;
    boolean checked()            default NotUsed.BOOL;
    boolean disabled()           default NotUsed.BOOL;
    boolean readonly()           default NotUsed.BOOL;
    int     size()               default NotUsed.INT;
    String  src()                default NotUsed.STRING;  
    String  value()              default NotUsed.STRING;   
    
    // HTML 5
    String  autocomplete()       default NotUsed.STRING; // Possible values are "on" & "off" @see Autocomplete
    boolean autofocus()          default NotUsed.BOOL;
    boolean autosave()           default NotUsed.BOOL;
    String  form()               default NotUsed.STRING;
    String  formaction()         default NotUsed.STRING;
    String  formenctype()        default NotUsed.STRING; // Default value is @see Enctype.APPLICATION
    String  formmethod()         default NotUsed.STRING; // @see Method
    boolean formnovalidate()     default NotUsed.BOOL;
    String  formtarget()         default NotUsed.STRING; // @see Target
    int     height()             default NotUsed.INT;
    String  inputmode()          default NotUsed.STRING; 
    String  list()               default NotUsed.STRING;
    int     max()                default NotUsed.INT;
    int     maxlength()          default NotUsed.INT;
    int     min()                default NotUsed.INT;
    boolean multiple()           default NotUsed.BOOL;
    String  pattern()            default NotUsed.STRING;
    String  placeholder()        default NotUsed.STRING;
    boolean required()           default NotUsed.BOOL;
    String  selectionDirection() default NotUsed.STRING; // @see SelectionDirection
    String  spellcheck()         default NotUsed.STRING; // @see Spellcheck
    String  step()               default NotUsed.STRING;

    
    // Deprecated
    // String accessKey() default NotUsed.STRING;

}
