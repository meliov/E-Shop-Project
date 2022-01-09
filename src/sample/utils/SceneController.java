package sample.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    public static void changeScene(ActionEvent event, String fileName) {
        try {
             Stage stage;
             Scene scene;
             Parent root;
            root = FXMLLoader.load(SceneController.class.getResource(fileName));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void openStage( String fileName){
        try {
            Stage stage = new Stage();
            Scene scene;
            Parent root;
            root = FXMLLoader.load(SceneController.class.getResource(fileName));
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
