package assignment4;

import javax.swing.JTextField;
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

    public int validateLength() {
        for (int i = 0; i < textFields.size(); i++) {
            String fieldText = textFields.get(i).getText().trim();
            if (fieldText.length() > MAX_LENGTHS[i]) {
                return i;
            }
        }
        return 100;
    }

    public int validateRequiredFields() {
        for (int index : REQUIRED_FIELD_INDICES) {
            String fieldText = textFields.get(index).getText().trim();
            if (fieldText.isEmpty()) {
                return index;
            }
        }
        return 100;
    }
    
    public String returnValidateLength() {
    	int i=validateLength();
    	if (i==100) {
    		return null;}
    	else
    	{
    		String text=FIELDS[i]+" exceeds max length "+MAX_LENGTHS[i]+".Please try again.";
    		return text;
    	}
    	
    }
    
    public String returnValidateRequiredFields() {
    	int i=validateRequiredFields();
    	if (i==100) {
    		return null;}
    	else
    	{
    		String text=FIELDS[i]+" can not be empty.Please try again.";
    		return text;
    	}
    	
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

    public String getId(String IdRaw) {
        
        String text=null;
        
        if (isEmpty(IdRaw)) {
            text="ID can not be null, please try again";
            return text;
        }else if (exceedsMaxLength(IdRaw, 9)) {
        	text="ID must be 9 characters or less, please try again";
            return text;
        }else if (!isIdNumeric(IdRaw)) {
            text="ID must be a number. Please try again";
            return text;
         }else{
        String id = String.format("%09d", Integer.parseInt(IdRaw));
        return id;}
        }
}
