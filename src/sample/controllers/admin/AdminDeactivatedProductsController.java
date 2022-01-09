package sample.controllers.admin;

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
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseProducts;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDeactivatedProductsController implements Initializable {

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
    @FXML
    private TableColumn<Product,String> uploaderColumn;
    private static List<Product> allDeactivated;
    public static void openStage(List<Product> extractedList){
        allDeactivated = extractedList;
        SceneController.openStage(FXMLFiles.ADMIN_SHOW_ALL_DEACTIVATED);
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
            DatabaseProducts.deactivatedRead();
        }else{
            errorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Product> productsList =
                FXCollections.observableList(allDeactivated);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("Category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        deactivationDate.setCellValueFactory(new PropertyValueFactory<>("deactivationDate"));
        uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploader"));
        myAdsTable.setItems(productsList);
    }
}
