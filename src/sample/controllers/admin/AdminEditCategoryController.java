package sample.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.entities.Category;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseCategories;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminEditCategoryController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private Label successLabel;
    @FXML
    private TextField categoryTextField;
    private static Category category = new Category();
    private static Category oldCategory = new Category() ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(category != null) {
            categoryTextField.setText(category.getCategory());
        }
    }

    public static void openStage(Category currentCategory){
        category = currentCategory;
        oldCategory = currentCategory;
        SceneController.openStage(FXMLFiles.ADMIN_EDIT_CATEGORY_PANEL);
    }

    public void doneButtonOnAction(){
        if(!categoryTextField.getText().equals(category.getCategory()) && !categoryTextField.getText().isBlank()) {
            Category currentCategory = new Category(categoryTextField.getText());
            DatabaseCategories.readFromDb();
            if (CollectionController.categoriesListForAdmins.contains(currentCategory)) {
                errorLabel.setText(LabelConstants.CATEGORY_ALREADY_EXISTS);
                successLabel.setText(LabelConstants.BLANK);
            } else {
                DatabaseCategories.editDb(currentCategory, oldCategory);
                successLabel.setText(LabelConstants.DONE);
                errorLabel.setText(LabelConstants.BLANK);
            }
        }else{
            errorLabel.setText(LabelConstants.NO_INPUT);
        }
    }
}
