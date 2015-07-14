package fr.ekinci.test;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import fr.ekinci.htmlform.Form;
import fr.ekinci.htmlform.FormException;
import fr.ekinci.htmlform.WrongCsrfTokenException;
import fr.ekinci.htmlform.annotation.DateFormat;
import fr.ekinci.htmlform.generation.annotation.Input;
import fr.ekinci.htmlform.generation.annotation.FormElement;
import fr.ekinci.htmlform.generation.annotation.Label;
import fr.ekinci.htmlform.generation.annotation.Textarea;
import fr.ekinci.htmlform.generation.constant.Type;


@fr.ekinci.htmlform.generation.annotation.Form
public class MyForm {  
    
    @Input(
        formElement = @FormElement(
           presentationOrder = 0, 
           label = @Label(label = "{label_name}")      
        ),
        id = "nom", name = "nom", type = Type.TEXT
    )
    private String nom;
    
    
    @Textarea(
        formElement = @FormElement(
            presentationOrder = 1, 
            label = @Label(label = "{label_text}")
        ),
        id = "texte", name = "texte"
    )
    private String texte;    
    

    @Input(
        formElement = @FormElement(
            presentationOrder = 2,
            label = @Label(label = "{label_sex}")
        ), 
        id = "sexe1", name = "sexe", value = "{male}", type = Type.RADIO
    )
    @Input(        
        formElement = @FormElement(
            presentationOrder = 3,
            label = @Label(label = "{label_sex}")
        ), 
        id = "sexe2", name = "sexe", value = "{female}", type = Type.RADIO
    )
    private String sexe;
    
    @DateFormat("dd/MM/yyyy")
    private Date dateDeNaissance;
    
    private Integer age;
    private Double salaire;
    
    
    public static void main(String[] args) {
        HttpServletRequest request = null;
        MyForm myForm = null;
        try {
            if((myForm = Form.validateForm(MyForm.class, request, "submittedFormName")) != null){
                
            }
        } catch (WrongCsrfTokenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FormException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConstraintViolationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
