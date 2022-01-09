package sample.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.utils.DataAndValidation;
import sample.utils.SceneController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseUsers;

public class AdminRegistrationController {
    @FXML
    private Button signupButton;
    @FXML
    private Label messageLabelUsername;
    @FXML
    private Label signedLabel;
    @FXML
    private Label messageLabelEmail;
    @FXML
    private Label flNameLabel;
    @FXML
    private Label messageLabelPassword;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;

    public static void openStage(){
        SceneController.openStage(FXMLFiles.ADMIN_REG_PANEL);
    }
    public void signupButtonOnAction(){
        boolean validation =  DataAndValidation.signupFieldsValidation(messageLabelUsername,messageLabelEmail,messageLabelPassword, flNameLabel,
                usernameField.getText(), passwordField.getText(), emailField.getText(), firstNameField.getText(),
                lastNameField.getText());
        if(validation){
            DatabaseUsers.write(firstNameField.getText(), lastNameField.getText(),
                    usernameField.getText(), passwordField.getText(),emailField.getText(), ConstantWords.ADMIN);
            signedLabel.setText(LabelConstants.SIGNED_UP);
            usernameField.clear();
            passwordField.clear();
            lastNameField.clear();
            firstNameField.clear();
            emailField.clear();
        }
    }
}
