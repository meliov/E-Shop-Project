package sample.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controllers.chat.ChatUserController;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.DataAndValidation;
import sample.utils.constants.*;
import sample.utils.database.DatabaseCategories;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseMessages;
import sample.utils.database.DatabaseProducts;
import sample.utils.SceneController;

import java.net.URL;
import java.util.*;

public class UserController implements Initializable {

    //Upload fields
    @FXML
    private TextField productNameTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextArea descriptionTextArea;
    //Upload labels
    @FXML
    private Label nameLabelError;
    @FXML
    private Label categoryLabelError;
    @FXML
    private Label priceLabelError;
    @FXML
    private Label descriptionLabelError;
    private String category = null;
    @FXML
    private Label successfulUploadingLabel;

    //Upload ad
    public void uploadButtonOnAction() {
        if (uploadValidation(productNameTextField, nameLabelError, category, categoryLabelError,
                priceTextField, priceLabelError, descriptionTextArea, descriptionLabelError)) {
            DatabaseProducts.writeProductsToDB(category, Double.parseDouble(priceTextField.getText()),
                    productNameTextField.getText(), descriptionTextArea.getText());
            successfulUploadingLabel.setText(LabelConstants.UPLOADED_SUCCESSFULLY);
            descriptionTextArea.clear();
            priceTextField.clear();
            productNameTextField.clear();
        }
        DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
        DatabaseFavouriteProducts.readFavouriteProducts();
        DatabaseFavouriteProducts.deactivatedFavRead();
    }

    public void categoryOnAction() {
        category = categoryComboBox.getSelectionModel().getSelectedItem();
    }


    public boolean uploadValidation(TextField productNameTextField, Label nameLabelError, String category, Label categoryLabelError,
                                     TextField priceTextField, Label priceLabelError, TextArea descriptionTextArea, Label descriptionLabelError ) {
        boolean name, price, description, categoryBool;
        if (productNameTextField.getText().isBlank()) {
            nameLabelError.setText(LabelConstants.NO_NAME);
            name = false;
        } else {
            nameLabelError.setText(LabelConstants.BLANK);
            name = true;
        }
        if (category == null) {
            categoryLabelError.setText(LabelConstants.CATEGORY_MESSAGE);
            categoryBool = false;
        } else {
            categoryLabelError.setText(LabelConstants.BLANK);
            categoryBool = true;
        }
        if (priceTextField.getText().isBlank()) {
            priceLabelError.setText(LabelConstants.PRICE_MESSAGE);
            price = false;
        } else {
            double currPrice = -1;//if it is not a number
            try {
                currPrice = Double.parseDouble(priceTextField.getText());
            } catch (Exception ignored) {
            }
            if (currPrice < ConstantNumbers.MIN_PRICE) {
                priceLabelError.setText(LabelConstants.PRICE_MESSAGE);
                price = false;
            } else {
                priceLabelError.setText(LabelConstants.BLANK);
                price = true;
            }
        }
        if (descriptionTextArea.getText().isBlank()) {
            descriptionLabelError.setText(LabelConstants.DESCRIPTION_MESSAGE);
            description = false;
        } else {
            if (descriptionTextArea.getText().length() < ConstantNumbers.MIN_TEXT_LEN ||
                    descriptionTextArea.getText().length() > ConstantNumbers.MAX_TEXT_LEN) {
                descriptionLabelError.setText(LabelConstants.DESCRIPTION_MESSAGE);
                description = false;
            } else {
                descriptionLabelError.setText(LabelConstants.BLANK);
                description = true;
            }
        }
        return name && price && categoryBool && description;
    }

    //My profile
    @FXML
    private Button exitButton;
    @FXML
    private Label greetingLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TableView<Product> myAdsTable;
    @FXML
    private TableColumn<Product,String> nameColumn;
    @FXML
    private TableColumn<Product,String> categoryColumn;
    @FXML
    private TableColumn<Product,Double> priceColumn;
    @FXML
    private TableColumn<Product,String> descriptionColumn;
    @FXML
    private TableColumn<Product, String> dateColumn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TitledPane myProfilePane;
    @FXML
    private Accordion userAccordion;
    @FXML
    private Label noSelectedProductLabel;
    //i will skip adding the buttons because i will not use them
    @FXML
    private TextField startDateTextField;
    @FXML
    private TextField endDateTextField;
    @FXML
    private Label endDateErrorLabel;
    @FXML
    private Label startDateErrorLabel;


    public void exitButtonOnAction(ActionEvent event) {
        SceneController.changeScene(event, FXMLFiles.HELLO_PANEL);
    }
    public void chatButtonOnAction(){
        DatabaseMessages.readFromDB();
        ChatUserController.openStage();
    }
    public void favouritesButtonOnAction() {
        FavouritesController.openStage();
    }
    public void showAllButtonOnAction()  {
        DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
        List<Product> extractionList = DataAndValidation.getSortedByDate(CollectionController.allProductsList, startDateTextField, endDateTextField, startDateErrorLabel, endDateErrorLabel);
        if(extractionList != null) {
            ShowAllController.openStage(extractionList);
        }
    }
    public void editAdButtonOnAction(ActionEvent event){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            EditProductController.openStage(selectedProduct);
            noSelectedProductLabel.setText(LabelConstants.BLANK);
        }else{
            noSelectedProductLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }
    public void deactivateAdButtonOnAction(ActionEvent event){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseProducts.deactivatedWrite(selectedProduct.getId());
            noSelectedProductLabel.setText(LabelConstants.BLANK);
            //space for favourite
            DatabaseFavouriteProducts.deactivateFavWrite(selectedProduct);
            DatabaseFavouriteProducts.deactivatedFavRead();
        }else{
            noSelectedProductLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }

    }

    public void showDeactivatedButtonOnAction(ActionEvent event){
        DatabaseProducts.deactivatedRead();
        ShowDeactivatedController.openStage();
    }

    public void deleteAdButtonOnAction(ActionEvent event) {
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseProducts.deleteFromDb(selectedProduct.getId());
            noSelectedProductLabel.setText(LabelConstants.BLANK);
            //space for favourite
            DatabaseFavouriteProducts.deactivatedFavRead();
            DatabaseFavouriteProducts.deactivatedDelete(selectedProduct);
            DatabaseFavouriteProducts.deactivatedFavRead();
        }else{
            noSelectedProductLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }
    public void refreshButtonOnAction(ActionEvent event){//realno tr se izmisli po dobur nachin ama za sega stava :DD
        SceneController.changeScene(event,FXMLFiles.USER_PANEL);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Upload ad
        //categories comboBox
        DatabaseCategories.readFromDb();
        if(categoryComboBox != null) {
            ObservableList<String> listComboBoxCategory =
                    FXCollections.observableList(CollectionController.categoriesList);
            categoryComboBox.setItems(listComboBoxCategory);
        }
        //My profile
        //table
        ObservableList<Product> productsList =
                FXCollections.observableList(CollectionController.getUserProducts(CollectionController.allProductsList, CollectionController.currentUser.getUsername()));
        if(nameColumn != null && categoryColumn != null && priceColumn!= null && descriptionColumn != null && myAdsTable != null) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            myAdsTable.setItems(productsList);
        }
        //labels
        if(greetingLabel != null && emailLabel != null) {
            greetingLabel.setText("Hello " + CollectionController.currentUser.getUsername());
            emailLabel.setText(CollectionController.currentUser.getEmail());
        }
        //refresh button in my profile
        if(userAccordion != null) {
            userAccordion.setExpandedPane(myProfilePane);//zasega taka, nasekude che turish refresh butoni i che te vrashtaa v nachaloto
        }
    }
}

