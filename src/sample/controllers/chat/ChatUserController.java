package sample.controllers.chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controllers.chat.ChattingController;
import sample.entities.Message;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatUserController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private TableColumn<Message,String> allChatColumn;
    @FXML
    private TableView<Message> allChatTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Message> messagesWithUsers = FXCollections.observableList(extractOnlyOneFromUser());
        allChatColumn.setCellValueFactory(new PropertyValueFactory<>("from"));
        allChatTable.setItems(messagesWithUsers);
    }
    public static void openStage(){
        SceneController.openStage(FXMLFiles.CHAT_USER);
    }
    public void textToButtonOnAction(ActionEvent event){
        Message selectedUser = allChatTable.getSelectionModel().getSelectedItem();//a String with the field from was enough tho
        if(selectedUser != null){
            ChattingController.changeScene(event, selectedUser);
            errorLabel.setText(LabelConstants.BLANK);
        }else{
            errorLabel.setText(LabelConstants.NO_SELECTED_USER);
        }
    }
    private List<Message> messagesToThisUser(){
        List<Message> list = new ArrayList<>();
        for(Message m: CollectionController.messageList){
            if (m.getTo().equals(CollectionController.currentUser.getUsername())){
                list.add(m);
            }
        }
        return list;
    }
    private List<Message> extractOnlyOneFromUser(){
        List<Message> allForThisUser = messagesToThisUser();
        List<String> compareNames = new ArrayList<>();
        for(Message m: allForThisUser){
            if(!compareNames.contains(m.getFrom())){
                compareNames.add(m.getFrom());
            }
        }
        List<Message> extracted = new ArrayList<>();
        for(String s: compareNames){
            for(Message m: allForThisUser){
                if(m.getFrom().equals(s)){
                    extracted.add(m);
                    break;
                }
            }
        }
        return extracted;
    }
}
