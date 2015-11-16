package fr.ekinci.test;
import static org.mockito.Mockito.*;

import org.junit.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import fr.ekinci.htmlform.Form;
import fr.ekinci.htmlform.FormException;
import fr.ekinci.htmlform.annotation.DateFormat;

public class FormTest {
    public final static String SUBMITTED_FORM_NAME = "my-form";
    public final static String METHOD = "POST";
    public final static String CSRF_TOKEN_NAME = "_csrf";
    public final static String CSRF_TOKEN_VALUE = "1234567890TheDarkKnight";
    
    @Test
    public void test() throws ParseException {  
        
        /* *** MOCK *** */
        HttpServletRequest mockedRequest = mock(HttpServletRequest.class);        
        when(mockedRequest.getParameterMap()).thenReturn(generateMapForFormClassTest()); 
        when(mockedRequest.getMethod()).thenReturn(METHOD);
        
        HttpSession mockedSession = mock(HttpSession.class);
        when(mockedSession.getAttribute(CSRF_TOKEN_NAME)).thenReturn(CSRF_TOKEN_VALUE);
        
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        /* *** MOCK *** */
        
        try {
            FormClassTest form = null;
            if((form = Form.validate(FormClassTest.class, mockedRequest, SUBMITTED_FORM_NAME, METHOD)) != null){
                Assert.assertEquals("field1 failed", form.getField1(), "This is a String");
                Assert.assertEquals("field2 failed", form.getField2(), new SimpleDateFormat("dd/MM/yyyy").parse("26/01/1992"));
                Assert.assertEquals("field3 failed", form.getField3(), 3);
                Assert.assertEquals("field4 failed", form.getField4(), new Short((short) 4));
                Assert.assertEquals("field5 failed", form.getField5(), 5);
                Assert.assertEquals("field6 failed", form.getField6(), new Integer(6));
                Assert.assertEquals("field7 failed", form.getField7(), 7);
                Assert.assertEquals("field8 failed", form.getField8(), new Long(8));
                Assert.assertEquals(9.0f, form.getField9(), 9.0f);
                Assert.assertEquals("field10 failed", form.getField10(), new Float(10.0f));
                Assert.assertEquals(11.0d, form.getField11(), 11.0d);
                Assert.assertEquals("field12 failed", form.getField12(), new Double(12.0d));
                Assert.assertEquals("field13 failed", form.getField13(), new BigInteger("13"));
                Assert.assertEquals("field14 failed", form.getField14(), new BigDecimal("14.14"));
                
                List<String> list15 = new ArrayList<>();
                list15.add("This is a String 1");
                list15.add("This is a String 2");
                list15.add("This is a String 3");
                Assert.assertEquals("field15 failed", form.getField15(), list15);
                
                List<Date> list16 = new ArrayList<>();
                list16.add(new SimpleDateFormat("dd/MM/yyyy").parse("26/01/1992"));
                list16.add(new SimpleDateFormat("dd/MM/yyyy").parse("27/01/1992"));
                list16.add(new SimpleDateFormat("dd/MM/yyyy").parse("28/01/1992"));
                Assert.assertEquals("field16 failed", form.getField16(), list16);
                
                List<Short> list17 = new ArrayList<>();
                list17.add(new Short((short) 17));
                list17.add(new Short((short) 18));
                list17.add(new Short((short) 19));
                Assert.assertEquals("list17 failed", form.getField17(), list17);
                
                List<Integer> list18 = new ArrayList<>();
                list18.add(new Integer(18));
                list18.add(new Integer(19));
                list18.add(new Integer(20));
                Assert.assertEquals("list18 failed", form.getField18(), list18);
                
                List<Long> list19 = new ArrayList<>();
                list19.add(new Long(19L));
                list19.add(new Long(20L));
                list19.add(new Long(21L));
                Assert.assertEquals("list19 failed", form.getField19(), list19);
                
                List<Float> list20 = new ArrayList<>();
                list20.add(new Float(20.0F));
                list20.add(new Float(21.0F));
                list20.add(new Float(22.0F));
                Assert.assertEquals("list20 failed", form.getField20(), list20);
                
                List<Double> list21 = new ArrayList<>();
                list21.add(new Double(21.0d));
                list21.add(new Double(22.0d));
                list21.add(new Double(23.0d));
                Assert.assertEquals("list21 failed", form.getField21(), list21);
                
                List<BigInteger> list22 = new ArrayList<>();
                list22.add(new BigInteger("22"));
                list22.add(new BigInteger("23"));
                list22.add(new BigInteger("24"));
                Assert.assertEquals("list22 failed", form.getField22(), list22);
                
                List<BigDecimal> list23 = new ArrayList<>();
                list23.add(new BigDecimal("23.0"));
                list23.add(new BigDecimal("24.0"));
                list23.add(new BigDecimal("25.0"));
                Assert.assertEquals("list23 failed", form.getField23(), list23);                
            }
        } catch (ConstraintViolationException | FormException e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, String[]> generateMapForFormClassTest(){
        Map<String, String[]> result = new HashMap<>();
        result.put(SUBMITTED_FORM_NAME, new String[]{}); // Submitted form name
        result.put(CSRF_TOKEN_NAME, new String[]{CSRF_TOKEN_VALUE}); // CSRF
        result.put("field1", new String[]{"This is a String"});
        result.put("field2", new String[]{"26/01/1992"});
        result.put("field3", new String[]{"3"});
        result.put("field4", new String[]{"4"});
        result.put("field5", new String[]{"5"});
        result.put("field6", new String[]{"6"});
        result.put("field7", new String[]{"7"});
        result.put("field8", new String[]{"8"});
        result.put("field9", new String[]{"9,0"});
        result.put("field10", new String[]{"10"});
        result.put("field11", new String[]{"11.0"});
        result.put("field12", new String[]{"12,0"});
        result.put("field13", new String[]{"13"});
        result.put("field14", new String[]{"14.14"});
        result.put("field15", new String[]{"This is a String 1", "This is a String 2", "This is a String 3"});
        result.put("field16", new String[]{"26/01/1992", "27/01/1992", "28/01/1992"});
        result.put("field17", new String[]{"17", "18", "19"});
        result.put("field18", new String[]{"18", "19", "20"});
        result.put("field19", new String[]{"19", "20", "21"});
        result.put("field20", new String[]{"20,0", "21,0", "22,0"});
        result.put("field21", new String[]{"21", "22", "23"});
        result.put("field22", new String[]{"22", "23", "24"});
        result.put("field23", new String[]{"23,0", "24,0", "25,0"});
        return result;
    }
}



