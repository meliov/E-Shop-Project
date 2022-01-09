package sample.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.utils.CollectionController;
import sample.utils.RegexValidation;
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.constants.RegexConstants;
import sample.utils.database.DatabaseUsers;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAdminsController implements Initializable {//tr si napraa za currentUser da se zapisva, tr stane s update
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label successEmailLabel;
    @FXML
    private Label successPasswordLabel;
    public static void openStage(){
        SceneController.openStage(FXMLFiles.EDIT_ADMINS);
    }
    public void applyEmailButtonOnAction(){
        if(!emailTextField.getText().isBlank() &&
                !emailTextField.getText().equals(CollectionController.currentUser.getEmail()) &&
                RegexValidation.fieldIsValid(emailTextField.getText(), RegexConstants.EMAIL_REGEX)){
            DatabaseUsers.editEmail(emailTextField.getText(), CollectionController.currentUser.getUsername());
            CollectionController.currentUser.setEmail(emailTextField.getText());
            successEmailLabel.setText(LabelConstants.DONE);
            emailErrorLabel.setText(LabelConstants.BLANK);
        }else{
            emailErrorLabel.setText(LabelConstants.NOT_VALID_EMAIL);
            successEmailLabel.setText(LabelConstants.BLANK);
        }
    }
    public void applyPasswordButtonOnAction(){
        if(!newPasswordField.getText().equals(CollectionController.currentUser.getPassword()) &&
                !newPasswordField.getText().equals(CollectionController.currentUser.getPassword()) &&
                RegexValidation.fieldIsValid(newPasswordField.getText(), RegexConstants.PASS_REGEX)){
            DatabaseUsers.editPassword(newPasswordField.getText(),CollectionController.currentUser.getUsername());
            CollectionController.currentUser.setPassword(newPasswordField.getText());
            successPasswordLabel.setText(LabelConstants.DONE);
            passwordErrorLabel.setText(LabelConstants.BLANK);
        }else{
            if(newPasswordField.getText().equals(CollectionController.currentUser.getPassword())) {
                passwordErrorLabel.setText(LabelConstants.PASSWORD_SAME);
            } else if(!RegexValidation.fieldIsValid(newPasswordField.getText(), RegexConstants.PASS_REGEX)){
                passwordErrorLabel.setText(LabelConstants.PASSWORD_LEN);
            }
            successPasswordLabel.setText(LabelConstants.BLANK);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailTextField.setText(CollectionController.currentUser.getEmail());
    }
}
