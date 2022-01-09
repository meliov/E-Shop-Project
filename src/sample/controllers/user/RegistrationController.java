package sample.controllers.user;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseUsers;
import sample.utils.DataAndValidation;
import sample.utils.SceneController;

public class RegistrationController {


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

    public void signupButtonOnAction(){
        boolean validation =  DataAndValidation.signupFieldsValidation(messageLabelUsername,messageLabelEmail,messageLabelPassword, flNameLabel,
              usernameField.getText(), passwordField.getText(), emailField.getText(), firstNameField.getText(),
              lastNameField.getText());
      if(validation){
          DatabaseUsers.write(firstNameField.getText(), lastNameField.getText(),
                  usernameField.getText(), passwordField.getText(),emailField.getText(), ConstantWords.USER);
          signedLabel.setText(LabelConstants.SIGNED_UP);
          usernameField.clear();
          passwordField.clear();
          lastNameField.clear();
          firstNameField.clear();
          emailField.clear();

      }
    }

    public void goBackButtonOnAction(ActionEvent event){
        SceneController.changeScene(event, FXMLFiles.HELLO_PANEL);
    }
}
