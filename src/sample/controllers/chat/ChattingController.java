package sample.controllers.chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.Message;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.ConstantNumbers;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.constants.RegexConstants;
import sample.utils.database.DatabaseMessages;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class ChattingController implements Initializable {

    @FXML
    private TableView<Message> messagesTable;
    @FXML
    private TableColumn<Message, String> dateColumn;
    @FXML
    private TableColumn<Message, String> fromColumn;
    @FXML
    private TableColumn<Message, String> toColumn;
    @FXML
    private TableColumn<Message, String> contentColumn;
    @FXML
    private TextArea textArea;
    @FXML
    private Label errorLabel;
    private static Message selectedMessage;
    public static void changeScene(ActionEvent event, Message message){
        selectedMessage = message;
        SceneController.changeScene(event, FXMLFiles.CHATTING);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Message> extractedMessages = FXCollections.observableList(listBetweenCurrentAndSelectedUsers());
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        messagesTable.setItems(extractedMessages);
    }
    public void sendButtonOnAction(ActionEvent event){
        if(!textArea.getText().isBlank() && textArea.getText().length() <= ConstantNumbers.MAX_TEXT_LEN ){
            String currentTime = new SimpleDateFormat(RegexConstants.DATE_PATTERN).format(Calendar.getInstance().getTime());
            Message message = new Message(CollectionController.currentUser.getUsername(), selectedMessage.getFrom(), textArea.getText(), currentTime);
            DatabaseMessages.writeToDB(message);
            errorLabel.setText(LabelConstants.BLANK);
            textArea.clear();
            SceneController.changeScene(event, FXMLFiles.CHATTING);
        }else{
            if(textArea.getText().length() > ConstantNumbers.MAX_TEXT_LEN){
                errorLabel.setText(LabelConstants.TEXT_LIMIT);
            }else {
                errorLabel.setText(LabelConstants.NO_INPUT);
            }
        }
    }
    public void previousPageButtonOnAction(ActionEvent event){
        SceneController.changeScene(event, FXMLFiles.CHAT_USER);
    }
    private List<Message> listBetweenCurrentAndSelectedUsers(){
        DatabaseMessages.readFromDB();
        List<Message> list = new ArrayList<>();
        for(Message m: CollectionController.messageList){
            if(m.getFrom().equals(selectedMessage.getFrom()) && m.getTo().equals(selectedMessage.getTo())
                    || m.getTo().equals(selectedMessage.getFrom()) && m.getFrom().equals(selectedMessage.getTo())){
                list.add(m);
            }
        }
        return list;
    }
}
