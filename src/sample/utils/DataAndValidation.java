package sample.utils;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.entities.Product;
import sample.utils.constants.ConstantNumbers;
import sample.utils.constants.LabelConstants;
import sample.utils.constants.RegexConstants;
import sample.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DataAndValidation {
    public static boolean isBlankPassUser(TextField username, PasswordField password, Label message){
        boolean blank = false;
        if(username.getText().isBlank() && password.getText().isBlank()){
            message.setText(LabelConstants.PASS_AND_USERNAME_EMPTY);
            blank = true;
        }else if(username.getText().isBlank()){
            message.setText(LabelConstants.USERNAME_EMPTY);
            blank = true;
        }else if(password.getText().isBlank()){
            message.setText(LabelConstants.PASS_EMPTY);
            blank = true;
        }
        return blank;
    }
    public static boolean signupFieldsValidation(Label messageLabelUsername, Label messageLabelEmail,
                                                 Label messageLabelPassword, Label flNameLabel, String username, String password, String email, String firstName, String lastName){
        boolean assemble = true;
        boolean nameExists = true;
        for(User user: CollectionController.userList){
            if(user.getUsername().equals(username)){
                assemble = false;
                nameExists = false;
                break;
            }
        }
        if( lastName.length() < ConstantNumbers.MIN_LEN_F_L_NAME || firstName.length() < ConstantNumbers.MIN_LEN_F_L_NAME) {
            flNameLabel.setText(LabelConstants.EMPTY_F_L_NAMES);
            assemble = false;
        }else{
            flNameLabel.setText(LabelConstants.BLANK);
        }
        if (!RegexValidation.fieldIsValid(email, RegexConstants.EMAIL_REGEX)) {
            messageLabelEmail.setText(LabelConstants.NOT_VALID_EMAIL);
            assemble = false;
        }else{
            messageLabelEmail.setText(LabelConstants.BLANK);
        }
        if (!RegexValidation.fieldIsValid(username, RegexConstants.USERNAME_REGEX)) {
            messageLabelUsername.setText(LabelConstants.NOT_VALID_USERNAME);
            assemble = false;
        }else{
            if(nameExists){
                messageLabelUsername.setText(LabelConstants.BLANK);
            }else{
                messageLabelUsername.setText(LabelConstants.USERNAME_ALREADY_EXISTS);
            }
        }
        if (!RegexValidation.fieldIsValid(password, RegexConstants.PASS_REGEX)) {
            messageLabelPassword.setText(LabelConstants.PASSWORD_LEN);
            assemble = false;
        }else{
            messageLabelPassword.setText(LabelConstants.BLANK);
        }

        return assemble;
    }
    private static int getStringToIntDate(String data){
        String[] extractYears = data.split("\\s+");
        String[] extractNumbers = extractYears[0].split("\\.");
        int sum = 0;
        for (String extractNumber : extractNumbers) {
            sum += Integer.parseInt(extractNumber);
        }
        return sum;
    }
    public static List<Product> getSortedByDate(List<Product> inputList, TextField start, TextField end, Label startLabel, Label endLabel){
        List<Product> extractionList = new ArrayList<>();
        boolean wrongDate = false;
        if(start.getText().isBlank() && end.getText().isBlank()){
            extractionList = inputList;
        }else if(!start.getText().isBlank() && !end.getText().isBlank()){
            boolean checkStart;
            boolean checkEnd ;
            if(RegexValidation.fieldIsValid(start.getText(), RegexConstants.DATE_REGEX)){
                checkStart = true;
                startLabel.setText(LabelConstants.BLANK);
            }else{
                checkStart = false;
                wrongDate = true;
                startLabel.setText(LabelConstants.WRONG_DATE);
            }
            if(RegexValidation.fieldIsValid(end.getText(), RegexConstants.DATE_REGEX)){
                checkEnd = true;
                endLabel.setText(LabelConstants.BLANK);
            }else{
                wrongDate = true;
                checkEnd= false;
                endLabel.setText(LabelConstants.WRONG_DATE);
            }
            if(checkStart && checkEnd){
                for(Product product: inputList){
                    if(getStringToIntDate(product.getDate()) >= getStringToIntDate(start.getText()) && getStringToIntDate(product.getDate()) <= getStringToIntDate(end.getText())) {
                        extractionList.add(product);
                    }
                }
            }
        } else if(start.getText().isBlank()){
            if(RegexValidation.fieldIsValid(end.getText(), RegexConstants.DATE_REGEX)){
                for(Product product: inputList){
                    if(getStringToIntDate(product.getDate()) <= getStringToIntDate(end.getText())) {
                        extractionList.add(product);
                    }
                }
                endLabel.setText(LabelConstants.BLANK);
            }else{
                wrongDate = true;
                endLabel.setText(LabelConstants.WRONG_DATE);
            }
        }else if(end.getText().isBlank()){
            if(RegexValidation.fieldIsValid(start.getText(), RegexConstants.DATE_REGEX)){
                for(Product product: inputList){
                    if(getStringToIntDate(product.getDate()) >= getStringToIntDate(start.getText())) {
                        extractionList.add(product);
                    }
                }
                startLabel.setText(LabelConstants.BLANK);
            }else{
                wrongDate = true;
                startLabel.setText(LabelConstants.WRONG_DATE);
            }
        }
        if(!wrongDate) {
            startLabel.setText(LabelConstants.BLANK);
            endLabel.setText(LabelConstants.BLANK);
            return extractionList;
        }
        return null;

    }
}
