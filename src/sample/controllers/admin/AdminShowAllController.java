package sample.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.Product;
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseProducts;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminShowAllController implements Initializable {


    @FXML
    private Label noSelectionErrorLabel;
    @FXML
    private Label successLabel;
    @FXML
    private TableView<Product> myAdsTable;
    @FXML
    private TableColumn<Product,String> productNameColumn;
    @FXML
    private TableColumn<Product,String> categoryColumn;
    @FXML
    private TableColumn<Product,Double> priceColumn;
    @FXML
    private TableColumn<Product,String> descriptionColumn;
    @FXML
    private TableColumn<Product,Double> uploaderColumn;
    @FXML
    private TableColumn<Product,String> dateColumn;
    @FXML
    private TableColumn<Product,Double> uploaderMailColumn;
    private static List<Product> allProducts;
    public static void openStage(List<Product> products){
        allProducts = products;
        SceneController.openStage(FXMLFiles.ADMIN_SHOW_ALL_ACTIVE);
    }
    public void deleteButtonOnAction(){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseProducts.deleteFromDb(selectedProduct.getId());
            noSelectionErrorLabel.setText(LabelConstants.BLANK);
            //space for favourite
            DatabaseFavouriteProducts.deactivatedFavRead();
            DatabaseFavouriteProducts.deactivatedDelete(selectedProduct);
            DatabaseFavouriteProducts.deactivatedFavRead();
        }else{
            noSelectionErrorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Product> productsList = FXCollections.observableList(allProducts); //FXCollections.observableList(CollectionController.allProductsList);//we want them all
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploader"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        uploaderMailColumn.setCellValueFactory(new PropertyValueFactory<>("uploaderMail"));
        myAdsTable.setItems(productsList);
    }

}
