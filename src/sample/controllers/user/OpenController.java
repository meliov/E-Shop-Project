package sample.controllers.user;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.utils.constants.ConstantWords;
import sample.entities.User;
import sample.utils.constants.FXMLFiles;
import sample.utils.SceneController;
import sample.utils.CollectionController;
import sample.utils.DataAndValidation;

public class OpenController{

    @FXML
    private Label messageLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    public void loginButtonOnAction(ActionEvent event){//tr si naprava bazata danni da se vkliuchva sled kato startiram prilojenieto, a ne sled akto natisna login butono
           String role = ConstantWords.ROLE;
            boolean userExists = false;
            for(User user: CollectionController.userList){
                if((user.getUsername().equals(usernameField.getText()) && user.getPassword().equals(passwordField.getText()))){
                    userExists = true;
                    role = user.getRole();
                    CollectionController.currentUser = user;
                    break;
                }
            }
            //tuka message label e za podeshtane che tr se otvarqt razlichni vidove prozorci
            if(!DataAndValidation.isBlankPassUser(usernameField, passwordField, messageLabel)) {
                if(userExists) {
                    if (role.equals(ConstantWords.ADMIN)) {
                        //messageLabel.setText("admin is in");
                        SceneController.changeScene(event, FXMLFiles.ADMIN_PANEL);
                    } else if (role.equals(ConstantWords.USER)) {
                        SceneController.changeScene(event, FXMLFiles.USER_PANEL);
                    }
                }else {
                    messageLabel.setText("user does not exist!");
                }
            }
    }


    public void signupButtonOnAction(ActionEvent event){
      SceneController.changeScene(event,FXMLFiles.REGISTRATION_PANEL);//rugni v meino 1 signup buton i proai tam, ako ne stane ruk
    }

}