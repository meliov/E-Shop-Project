package sample.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controllers.chat.ChatFavouritesController;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.DataBaseConstants;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseProducts;

import java.net.URL;
import java.util.ResourceBundle;

public class FavouritesController implements Initializable {
    @FXML
    private Label errorLabel;

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
    public static void openStage(){
        SceneController.openStage(FXMLFiles.SHOW_FAVOURITES);
    }
    public void deleteButtonOnAction(){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            int selectedRow = myAdsTable.getSelectionModel().getSelectedIndex();
            myAdsTable.getSelectionModel().clearSelection(selectedRow);
            myAdsTable.getItems().remove(selectedRow);
            DatabaseFavouriteProducts.deleteFavouriteProduct(selectedProduct.getId());
            errorLabel.setText(LabelConstants.BLANK);
        }else {
            errorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }

    }
    public void sendMessageButtonOnAction(){
        Product selectedProduct = myAdsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null) {
            ChatFavouritesController.openStage(selectedProduct);
            errorLabel.setText(LabelConstants.BLANK);
        }else {
            errorLabel.setText(LabelConstants.NO_SELECTED_PRODUCT);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseFavouriteProducts.readFavouriteProducts();
        DatabaseProducts.deactivatedRead();
        DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
        if(CollectionController.favouriteProductsMap.containsKey(CollectionController.currentUser.getUsername())) {
            ObservableList<Product> productsList = FXCollections.observableList(CollectionController.favouriteProductsMap.get(CollectionController.currentUser.getUsername()));
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


}
