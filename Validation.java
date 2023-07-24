package assignment4;

import javax.swing.JTextField;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    private List<JTextField> textFields;
    private String ID;
    private static final int[] MAX_LENGTHS = {9, 15, 15, 1, 20, 20, 2, 10};
    private static final int[] REQUIRED_FIELD_INDICES = {0, 1, 2, 7};
    private static final String[] FIELDS= {"ID","Last name","First name","mi","Adress","City","State","Telephone"};

    public Validation(List<JTextField> textFields) {
        this.textFields = textFields;
    }
    //validate each field not exceed the maximum length
    public List<Integer> validateLength() {
    	List<Integer> errorlist= new ArrayList<>();
    	for (int i = 0; i < textFields.size(); i++) {
            String fieldText = textFields.get(i).getText().trim();
            if (fieldText.length() > MAX_LENGTHS[i]) {
                errorlist.add(i);
            }
        }
        return errorlist;
    }
    //validate required field not empty
    public List<Integer> validateRequiredFields() {
    	List<Integer> errorlist= new ArrayList<>();
        for (int index : REQUIRED_FIELD_INDICES) {
            String fieldText = textFields.get(index).getText().trim();
            if (fieldText.isEmpty()) {
            	errorlist.add(index);
            }
        }
        return errorlist;
    }
    
    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public boolean exceedsMaxLength(String str, int maxLength) {
        return str.length() > maxLength;
    }
    
    public boolean isIdNumeric(String id) {
        
        try {
            Integer.parseInt(id);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    //validate ID(not empty,not exceed max length,be numeric)
    public String getId(String IdRaw) throws ValidationException {
        
        String text=null;
        
        if (isEmpty(IdRaw)) {
        	throw new ValidationException("ID can not be null.");
        }else if (exceedsMaxLength(IdRaw, 9)) {
        	throw new ValidationException("ID must be 9 characters or less, please try again");
        }else if (!isIdNumeric(IdRaw)) {
            throw new ValidationException("ID must be a number. Please try again");
         }else{
        String id = String.format("%09d", Integer.parseInt(IdRaw));
        return id;}
        }
    //define validationexception
    public class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    public void validate() throws ValidationException {
        //print out all the fields exceed max length
    	List<Integer> lenErrors = validateLength();
        String text="";
        String text1="";
        if (!lenErrors.isEmpty()) {
        	for(int i:lenErrors)
        	{
        		text+=FIELDS[i]+",";
        		text1+=MAX_LENGTHS[i]+",";
        	}
            throw new ValidationException(text+"exceeds max length "+text1+". Please enter again.");
        }
        //print out all the required fields that are empty
        List<Integer> requireErrors = validateRequiredFields();
        String text2="";
        
        if (!requireErrors.isEmpty()) {
        	for(int i:requireErrors) {
        		text2+=FIELDS[i]+",";
        	}
            throw new ValidationException(text2+" can not be empty. Please try again.");
        }
    }

}
