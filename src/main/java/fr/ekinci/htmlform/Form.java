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
Supported field types are :
String
Date
short  / Short
int    / Integer
long   / Long
float  / Float
double / Double
BigInteger
BigDecimal
List<T> where T is one of the previous type

 * @author Gokan EKINCI
 */
public class Form {
    public  final static String CSRF_TOKEN_NAME = "_csrf";
    private final static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();


    public static <T> T validateForm(
        Class<T> formClass,
        HttpServletRequest request, 
        String submittedFormName
    ) throws WrongCsrfTokenException, FormException, ConstraintViolationException{
        return validateForm(formClass, request, submittedFormName, null);
    }
    
    /**
     * 
     * @param formClass the return type of object 
     * @param request
     * @param submittedFormName
     * @return null if it's not submitted, new T() if it's correctly submitted, FormException otherwise
     * @throws WrongCsrfTokenException
     * @throws FormException
     * @throws ConstraintViolationsException 
     */
    public static <T> T validateForm(
        Class<T> formClass,
        HttpServletRequest request, 
        String submittedFormName, 
        String requestMethod
    ) throws WrongCsrfTokenException, FormException, ConstraintViolationException {      
        // If requestMethod is not precised (null), go to the next check
        // Check if sended REQUEST_METHOD corresponds to wanted REQUEST_METHOD
        if(requestMethod != null || !request.getMethod().equalsIgnoreCase(requestMethod)){
            return null;
        }
        
        // If wanted form is not sended return null
        Map<String, String[]> parameterMap = request.getParameterMap();        
        if(!parameterMap.containsKey(submittedFormName)){
            return null;
        }
        
        // Test user csrf token
        if(!checkCsrfToken(userCsrfToken(request), parameterMap)){
            throw new WrongCsrfTokenException();
        }
        
        // Instanciate of form
        T formInstance = null;
        try {
            formInstance = formClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("An internal error happened during reflection instanciation of form object", e);
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
                   
        
        // Fill form instance        
        fillFormInstance(formClass, formInstance, parameterMap, fnf);
        
        
        // Validate instance of form
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(formInstance);
        if(constraintViolations.size() > 0){
            throw new ConstraintViolationException(constraintViolations);
        }
        
        return formInstance;        
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
    

    public static <T> void fillFormInstance(
        Class<T> formClass,
        T formInstance,
        Map<String, String[]> parameterMap,
        FieldNameFinder fnf
    ) throws FormException {
        Field[] formFields = formClass.getDeclaredFields();
        for(Field formField : formFields){
            formField.setAccessible(true);          
            String[] values = parameterMap.get(fnf.getFieldName(formField));
            if(values != null){
                fillField(formInstance, formField, values);
            }
        }
    }
    
    
    public static <T> void fillField(T formInstance, Field field, String[] values) throws FormException {
        Class<?> fieldClass = field.getType();

        try {
            if(fieldClass == List.class){               
                // @see http://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> parameterClass = (Class<?>) listType.getActualTypeArguments()[0];
                
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
    
    
    public static Object stringToObject(
        Class<?> clazz, 
        String value,
        Field field
    ) throws FormException{
        try{ 
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
