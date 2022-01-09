package sample.controllers.chat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.entities.Message;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.ConstantNumbers;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.constants.RegexConstants;
import sample.utils.database.DatabaseMessages;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ChatFavouritesController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private Label sendingLabel;
    @FXML
    private TextArea textArea;
    private static Product selectedProduct;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendingLabel.setText(LabelConstants.SENDING_MESSAGE_TO + selectedProduct.getUploader());
    }
    public static void openStage(Product inputProduct){
        selectedProduct = inputProduct;
        SceneController.openStage(FXMLFiles.CHAT_FAVOURITES);
    }
    public void sendButtonOnAction(){
        if(!textArea.getText().isBlank() && textArea.getText().length() <= ConstantNumbers.MAX_TEXT_LEN ){
            String currentTime = new SimpleDateFormat(RegexConstants.DATE_PATTERN).format(Calendar.getInstance().getTime());
            Message message = new Message(CollectionController.currentUser.getUsername(), selectedProduct.getUploader(), textArea.getText(), currentTime);
            DatabaseMessages.writeToDB(message);
            errorLabel.setText(LabelConstants.BLANK);
            textArea.clear();
        }else{
            if(textArea.getText().length() > ConstantNumbers.MAX_TEXT_LEN){
                errorLabel.setText(LabelConstants.TEXT_LIMIT);
            }else {
                errorLabel.setText(LabelConstants.NO_INPUT);
            }
        }

    }
}
