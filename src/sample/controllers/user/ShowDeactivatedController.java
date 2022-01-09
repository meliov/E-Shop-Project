package sample.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.constants.DataBaseConstants;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseProducts;
import sample.utils.SceneController;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowDeactivatedController implements Initializable {
    @FXML
    private Button activateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label errorLabel;
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
    private TableColumn<Product,String> deactivationDate;
    public static void openStage(){
        SceneController.openStage(FXMLFiles.SHOW_DEACTIVATED_PANEL);
    }
    public void activateButtonOnAction(){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseProducts.deactivatedActivate(selectedProduct.getId());
            DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
            errorLabel.setText(LabelConstants.BLANK);
            //space for favourite
            DatabaseFavouriteProducts.deactivatedFavRead();
            DatabaseFavouriteProducts.deactivatedActivate(selectedProduct);

        }else{
            errorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }

    }
    public void deleteButtonOnAction(){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseProducts.deactivatedDelete(selectedProduct.getId());
            errorLabel.setText(LabelConstants.BLANK);
            //space for favourite
            DatabaseFavouriteProducts.deactivatedFavRead();
            DatabaseFavouriteProducts.deactivatedDelete(selectedProduct);
        }else{
            errorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Product> productsList =
                FXCollections.observableList(CollectionController.getUserProducts(CollectionController.deactivatedProductList, CollectionController.currentUser.getUsername()));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
            deactivationDate.setCellValueFactory(new PropertyValueFactory<>("deactivationDate"));
            myAdsTable.setItems(productsList);
    }
}
