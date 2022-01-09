package sample.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.DataBaseConstants;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {
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
        SceneController.openStage(FXMLFiles.SHOW_ALL_PRODUCTS);
    }
    public void addToFavouritesButtonOnAction(){
        DatabaseFavouriteProducts.readFavouriteProducts();//because we need to have our map filled, for the if else statements down
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            if(selectedProduct.getUploader().equals(CollectionController.currentUser.getUsername())){
                noSelectionErrorLabel.setText(LabelConstants.CAN_NOT_ADD_OWN);
                successLabel.setText(LabelConstants.BLANK);
            }else if(CollectionController.favouriteProductsMap.containsKey(CollectionController.currentUser.getUsername())){
                if(CollectionController.favouriteProductsMap.get(CollectionController.currentUser.getUsername()).contains(selectedProduct)) {
                    noSelectionErrorLabel.setText(LabelConstants.PRODUCT_ALREADY_ADDED);
                    successLabel.setText(LabelConstants.BLANK);
                }else {
                    DatabaseFavouriteProducts.writeFavouriteProduct(selectedProduct, DataBaseConstants.INSERT_INTO_FAVOURITE_PRODUCTS, CollectionController.currentUser.getUsername());
                    noSelectionErrorLabel.setText(LabelConstants.BLANK);
                    successLabel.setText(LabelConstants.SUCCESSFULLY_ADDED);
                }
            }else {
                DatabaseFavouriteProducts.writeFavouriteProduct(selectedProduct, DataBaseConstants.INSERT_INTO_FAVOURITE_PRODUCTS, CollectionController.currentUser.getUsername());
                noSelectionErrorLabel.setText(LabelConstants.BLANK);
                successLabel.setText(LabelConstants.SUCCESSFULLY_ADDED);
            }
        }else {
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
