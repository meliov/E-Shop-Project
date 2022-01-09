package sample.controllers.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.Category;
import sample.entities.Product;
import sample.entities.User;
import sample.utils.CollectionController;
import sample.utils.DataAndValidation;
import sample.utils.SceneController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.*;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Label successfullCategoryLabel;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label noSelectedAdminLabel;
    @FXML
    private Label startDateErrorLabel;
    @FXML
    private Label endDateErrorLabel;
    @FXML
    private Label noSelectedCategoryErrorLabel;
    @FXML
    private Label wrongCategoryLabel;
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField endDateTextField;
    @FXML
    private TextField newCategoryTextField;
    @FXML
    private TableView<User> adminsTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableView<Category> categoriesTable;
    @FXML
    private TableColumn<Category, String> categoryColumn;

    @FXML
    private Accordion adminsAccordion;
    @FXML
    private TitledPane myProfilePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //admins
        DatabaseUsers.readFromDB();
        List<User> extractAllAdminsButCurrent = new ArrayList<>();
        for(User u: CollectionController.userList){
            if(u.getRole().equals(ConstantWords.ADMIN) && !u.getUsername().equals(CollectionController.currentUser.getUsername())){
                extractAllAdminsButCurrent.add(u);
            }
        }
        if(!extractAllAdminsButCurrent.isEmpty()) {
            ObservableList<User> usersList = FXCollections.observableList(extractAllAdminsButCurrent);
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            adminsTable.setItems(usersList);
        }
        if(adminsAccordion != null) {
            adminsAccordion.setExpandedPane(myProfilePane);
        }
        if(greetingLabel != null && emailLabel != null) {
            greetingLabel.setText("Hello " + CollectionController.currentUser.getUsername());
            emailLabel.setText(CollectionController.currentUser.getEmail());
        }
        //categories
        DatabaseCategories.readFromDb();
        if(!CollectionController.categoriesListForAdmins.isEmpty()){
            ObservableList<Category> categoriesList = FXCollections.observableList(CollectionController.categoriesListForAdmins);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            categoriesTable.setItems(categoriesList);
        }
    }

    public void exitButtonOnAction(ActionEvent event){
        SceneController.changeScene(event, FXMLFiles.HELLO_PANEL);
    }
    public void showDeactivatedButtonOnAction() {
        DatabaseProducts.deactivatedRead();
        List<Product> extractionList = DataAndValidation.getSortedByDate(CollectionController.deactivatedProductList, startDateTextField, endDateTextField, startDateErrorLabel, endDateErrorLabel);
        if(extractionList != null) {
            AdminDeactivatedProductsController.openStage(extractionList);
        }
    }
    public void showAllButtonOnAction(){
        DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
        List<Product> extractionList = DataAndValidation.getSortedByDate(CollectionController.allProductsList, startDateTextField, endDateTextField, startDateErrorLabel, endDateErrorLabel);
        if(extractionList != null) {
            AdminShowAllController.openStage(extractionList);
        }
    }
    public void refreshButtonOnAction(ActionEvent event){
        SceneController.changeScene(event, FXMLFiles.ADMIN_PANEL);
    }
    public void deleteAdminButtonOnAction(){
        User selectedAdmin = adminsTable.getSelectionModel().getSelectedItem();
        if(selectedAdmin != null) {
            int selectedRow = adminsTable.getSelectionModel().getSelectedIndex();
            adminsTable.getSelectionModel().clearSelection(selectedRow);
            adminsTable.getItems().remove(selectedRow);
            DatabaseUsers.deleteFromDb(selectedAdmin);
            DatabaseUsers.readFromDB();
            noSelectedAdminLabel.setText(LabelConstants.BLANK);
        }else{
            noSelectedAdminLabel.setText(LabelConstants.NO_SELECTED_ADMIN);
        }
    }
    public void addNewAdminButtonOnAction(){
        AdminRegistrationController.openStage();
    }

    public void editAdminButtonOnAction(){
        EditAdminsController.openStage();//this is used only for admins
    }
    public void editCategoryButtonOnAction(){
        Category category = categoriesTable.getSelectionModel().getSelectedItem();
        if(category != null){
            AdminEditCategoryController.openStage(category);
            noSelectedCategoryErrorLabel.setText(LabelConstants.BLANK);
        }else{
            noSelectedCategoryErrorLabel.setText(LabelConstants.NO_SELECTED_CATEGORY);
        }
    }
    public void addCategoryOnAction(){
        if(!newCategoryTextField.getText().isBlank()) {
            Category currentCategory = new Category(newCategoryTextField.getText());
            if (CollectionController.categoriesListForAdmins.contains(currentCategory)) {
                wrongCategoryLabel.setText(LabelConstants.CATEGORY_ALREADY_EXISTS);
                successfullCategoryLabel.setText(LabelConstants.BLANK);
            } else {
                DatabaseCategories.writeToDB(currentCategory.getCategory());
                successfullCategoryLabel.setText(LabelConstants.DONE);
                wrongCategoryLabel.setText(LabelConstants.BLANK);
            }
        }else{
            wrongCategoryLabel.setText(LabelConstants.NO_INPUT);
            successfullCategoryLabel.setText(LabelConstants.BLANK);
        }
    }
}
