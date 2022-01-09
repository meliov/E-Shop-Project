package sample.controllers.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.SceneController;
import sample.utils.constants.FXMLFiles;
import sample.utils.constants.LabelConstants;
import sample.utils.database.DatabaseFavouriteProducts;
import sample.utils.database.DatabaseProducts;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProductController implements Initializable {
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
    private Label successfulEditingLabel;
    public static Product selectedProduct = new Product();
    public static void openStage(Product product){
        selectedProduct = product;
        SceneController.openStage(FXMLFiles.EDIT_PRODUCT);
    }
    public void categoryOnAction(){
        category = categoryComboBox.getSelectionModel().getSelectedItem();
    }
    public void applyButtonOnAction(){
        UserController validate = new UserController();
        if(validate.uploadValidation(productNameTextField, nameLabelError, category, categoryLabelError,
                priceTextField, priceLabelError, descriptionTextArea, descriptionLabelError)){
            Product ifProductWasChanged = new Product(selectedProduct);
            selectedProduct.setCategory(category);
            selectedProduct.setName(productNameTextField.getText());
            selectedProduct.setPrice(Double.parseDouble(priceTextField.getText()));
            selectedProduct.setDescription(descriptionTextArea.getText());
            DatabaseProducts.editProduct(selectedProduct);
            //Favourites
            if(!ifProductWasChanged.equals(selectedProduct)){
                DatabaseFavouriteProducts.readFavouriteProducts();
                DatabaseFavouriteProducts.deactivatedDelete(ifProductWasChanged);
                DatabaseFavouriteProducts.readFavouriteProducts();
            }
            successfulEditingLabel.setText(LabelConstants.EDITED_SUCCESSFULLY);
            descriptionTextArea.clear();
            priceTextField.clear();
            productNameTextField.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category = selectedProduct.getCategory();
        productNameTextField.setText(selectedProduct.getName());
        priceTextField.setText(String.valueOf(selectedProduct.getPrice()));
        descriptionTextArea.setText(selectedProduct.getDescription());

        ObservableList<String> listComboBoxCategory =
                FXCollections.observableList(CollectionController.categoriesList);
        categoryComboBox.setItems(listComboBoxCategory);
        if(getCategoryIndex() != -1) {
            categoryComboBox.getSelectionModel().select(getCategoryIndex());//to set the category of the product
        }
    }
    private int getCategoryIndex(){
        int i = 0;
        for(String c:CollectionController.categoriesList){
            if(c.equals(category)){
                return i;
            }
            i++;
        }
        return -1;
    }

}
