package fr.ekinci.htmlform;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import fr.ekinci.htmlform.annotation.DateFormat;
import fr.ekinci.htmlform.generation.annotation.Input;
import fr.ekinci.htmlform.generation.annotation.Select;
import fr.ekinci.htmlform.generation.annotation.Textarea;


/**
 * Class for form validation
 * 
Supported field types are :
String
Date (Mandatory : has to be annotated with @DateFormat)
short  / Short
int    / Integer
long   / Long
float  / Float
double / Double
BigInteger
BigDecimal
List<T> where T is one of the previous type (Mandatory : has to be instanciated, example : new ArrayList<>() ).

 * @author Gokan EKINCI
 */
public class Form {
    public  final static String CSRF_TOKEN_NAME = "_csrf";
    private final static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    
    /**
     * Form validation method
     * 
     * @param formClass the return type of object 
     * @param request
     * @param submittedFormName
     * 
     * @return null if it's not submitted, new T() if it's correctly submitted, FormException/ConstraintViolationException otherwise
     * 
     * @throws FormException
     * @throws ConstraintViolationsException 
     */
    public static <T> T validate(
        Class<T> formClass,
        HttpServletRequest request, 
        String submittedFormName, 
        String requestMethod
    ) throws FormException, ConstraintViolationException { 
        
        // Control 1 : If wanted form is not sended return null
        Map<String, String[]> parameterMap = request.getParameterMap();        
        if(!parameterMap.containsKey(submittedFormName)){
            return null;
        }
        
        
        // Control 2 : If requestMethod is not precised (null), go to the next check
        if(requestMethod != null){
            // Check if sended REQUEST_METHOD corresponds to wanted REQUEST_METHOD
            if(!request.getMethod().equalsIgnoreCase(requestMethod)){
                throw new FormException("Wrong REQUEST_METHOD : Sended method does not correspond to expected method");
            }
        }
        
        
        // Control 3 : Test user csrf token
        if(!checkCsrfToken(userCsrfToken(request), parameterMap)){
            throw new FormException("Wrong CSRF_TOKEN : Sended token does not correspond to expected token");
        }
        
        
        // Initialize FieldNameFinder
        FieldNameFinder fnf = (formClass.isAnnotationPresent(fr.ekinci.htmlform.generation.annotation.Form.class)) ?          
            fieldNameFinderForAnnotatedForm()
            : new FieldNameFinder(){
                @Override
                public String getFieldName(Field field) {
                    return field.getName();
                }             
            };
        
            
        // All controls are passed, now fill the form instance        
        T formInstance = generateFormInstance(formClass, parameterMap, fnf);
        
        
        // Validate form instance
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(formInstance);
        if(constraintViolations.size() > 0){
            throw new ConstraintViolationException(constraintViolations);
        }
        
        return formInstance;        
    }
    
    
    /**
     * See {@link #validate(Class<T>, HttpServletRequest, String, String)}
     */
    public static <T> T validate(
        Class<T> formClass,
        HttpServletRequest request, 
        String submittedFormName
    ) throws FormException, ConstraintViolationException {
        return validate(formClass, request, submittedFormName, null);
    }
    
    
    private static FieldNameFinder fieldNameFinderForAnnotatedForm(){
        return new FieldNameFinder(){
            @Override
            public String getFieldName(Field field) {
                String fieldName = null;
                for(Annotation annotation : field.getAnnotations()){
                    if(annotation instanceof Input || annotation instanceof Select || annotation instanceof Textarea){
                        Class<? extends Annotation> annotationClass = annotation.getClass();
                        try {
                            Method m = annotationClass.getMethod("name");
                            fieldName = (String) m.invoke(annotation);
                        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            throw new RuntimeException("An internal error happened during retrieving field annotation name()", e);
                        }
                        break;
                    }
                }
                
                if(fieldName == null){
                    throw new RuntimeException("Annotation was not found on field, if you use @Form, add annotation to your fields");
                }

                return fieldName;
            }
        };
    }
    
    
    public static String userCsrfToken(HttpServletRequest request){
        HttpSession session = request.getSession();
        String userCsrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
        // if user token does not exist, generate it
        if(userCsrfToken == null){
            userCsrfToken = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_NAME, userCsrfToken);
        }
        return userCsrfToken;
    }
    
    
    public static boolean checkCsrfToken(String userCsrfToken, Map<String, String[]> parameterMap){
        String[] csrfTokenTab = parameterMap.get(CSRF_TOKEN_NAME);                      
        return csrfTokenTab != null && csrfTokenTab[0].equals(userCsrfToken);
    }
    
    
    // TODO
    public static String generateForm(Object instance, HttpServletRequest request){
        Class<? extends Object> classForm = instance.getClass();
        return null;
    }
    

    /**
     * Generate a form object from parameterMap
     * 
     * @param formClass
     * @param parameterMap
     * @param fnf
     * @throws FormException
     */
    public static <T> T generateFormInstance(
        Class<T> formClass,
        Map<String, String[]> parameterMap,
        FieldNameFinder fnf
    ) throws FormException {
        
        // Instanciation of form
        T formInstance = null;
        try {
            formInstance = formClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("An internal error happened during reflection instanciation of form object", e);
        }
        
        Field[] formFields = formClass.getDeclaredFields();
        for(Field formField : formFields){
            formField.setAccessible(true);
            // values may be null if it's not sended
            String[] values = parameterMap.get(fnf.getFieldName(formField));
            if(values != null){
                fillField(formInstance, formField, values);
            }
        }
        
        return formInstance;
    }
    
    
    /**
     * Filling a field from values
     * 
     * @param formInstance
     * @param field
     * @param values
     * @throws FormException
     */
    public static <T> void fillField(T formInstance, Field field, String[] values) throws FormException {
        Class<?> fieldClass = field.getType();

        try {
            if(fieldClass == List.class){               
                // @see http://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> parameterClass = (Class<?>) listType.getActualTypeArguments()[0];
                
                // @see http://stackoverflow.com/questions/14306166/java-generic-with-arraylist-extends-a-add-element
                List listObject = (List) field.get(formInstance);
                for(String value : values){
                    listObject.add(stringToObject(parameterClass, value, field));
                }
    
            } else {   
                field.set(formInstance, stringToObject(fieldClass, values[0], field));           
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("An internal error happened during setting form field", e);
        }
    }
    
    
    /**
     * Transforms string to Object
     * 
     * @param clazz Class of field
     * @param value Sended string value
     * @param field The field itself for obtaining annotations when its necessary
     * @return
     * @throws FormException
     */
    public static Object stringToObject(
        Class<?> clazz, 
        String value,
        Field field
    ) throws FormException {
        try { 
            // String
            if(clazz == String.class){
                return value;
            } 
            
            // Integer
            else if(clazz == int.class || clazz == Integer.class){
                String cleanedValue = value.trim();
                return Integer.parseInt(cleanedValue);
            } 
            
            // Double
            else if(clazz == double.class || clazz == Double.class){
                String cleanedValue = value.trim().replace(',', '.');
                return Double.parseDouble(cleanedValue);
            } 
            
            // Date
            else if(clazz == Date.class){
                String dateFormat = field.getAnnotation(DateFormat.class).value();
                String cleanedValue = value.trim();
                return new SimpleDateFormat(dateFormat).parse(cleanedValue);
            }
            
            // Float
            else if(clazz == float.class || clazz == Float.class){
                String cleanedValue = value.trim().replace(',', '.');
                return Float.parseFloat(cleanedValue);
            } 
            
            // Long
            else if(clazz == long.class || clazz == Long.class){
                String cleanedValue = value.trim();
                return Long.parseLong(cleanedValue);
            } 
            
            // Short
            else if(clazz == short.class || clazz == Short.class){
                String cleanedValue = value.trim();
                return Short.parseShort(cleanedValue);
            } 
            
            // BigDecimal
            else if(clazz == BigDecimal.class){
                String cleanedValue = value.trim().replace(',', '.');
                return new BigDecimal(cleanedValue);
            } 
            
            // BigInteger
            else if(clazz == BigInteger.class){
                String cleanedValue = value.trim();
                return new BigInteger(cleanedValue);
            } 
            
            // Unknown
            else {
                throw new RuntimeException(clazz.getName() + " is not supported field type");
            }
        } catch(NumberFormatException | ParseException e){
            throw new FormException("An error happened when trying to fill form field", e);
        } 
    }
    
    public interface FieldNameFinder {
        String getFieldName(Field field);
    }
}
