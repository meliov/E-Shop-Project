package sample.utils.database;

import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;
import sample.utils.constants.RegexConstants;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DatabaseProducts {//parallel modifying the database along with the lists
    public static void readFromDB(String database, List<Product> list){
        list.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(database);
            while(queryResult.next()){
                Product product = new Product(queryResult.getInt(ConstantWords.ID),queryResult.getString(ConstantWords.CATEGORY), queryResult.getDouble(ConstantWords.PRICE),
                        queryResult.getString(ConstantWords.NAME),queryResult.getString(ConstantWords.DESCRIPTION), queryResult.getString(ConstantWords.UPLOADER),queryResult.getString(ConstantWords.DATE));
                list.add(product);
            }
           connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void writeProductsToDB(String category, double price, String name, String description){
        String timeOfUploading = new SimpleDateFormat(RegexConstants.DATE_PATTERN).format(Calendar.getInstance().getTime());//because we must write the current date
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.INSERT_INTO_PRODUCTS);
            preparedStatement.setString(1, category);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, description);
            preparedStatement.setString(5, CollectionController.currentUser.getUsername());
            preparedStatement.setString(6, timeOfUploading);
            preparedStatement.executeUpdate();
            connection.close();
            int id = 0;
            if(CollectionController.allProductsList.size() > 0) {
                id = CollectionController.allProductsList.get(CollectionController.allProductsList.size() - 1).getId() + 1;//previous id + 1
            }
            CollectionController.allProductsList.add(new Product(id,category, price, name, description, CollectionController.currentUser.getUsername(), timeOfUploading));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void deleteFromDb(int id){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.DELETE_FROM_PRODUCTS);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            for(Product product: CollectionController.allProductsList){
                if(product.getId() == id){
                    CollectionController.allProductsList.remove(product);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
    public static void deactivatedWrite(int id){//from products to deactivated_products
        String timeOfDeactivating = new SimpleDateFormat(RegexConstants.DATE_PATTERN).format(Calendar.getInstance().getTime());
        Product currentProduct = new Product();
        for(Product p: CollectionController.allProductsList){
            if(p.getId() == id){
                currentProduct = p;
                currentProduct.setDeactivationDate(timeOfDeactivating);
                CollectionController.deactivatedProductList.add(currentProduct);
                break;
            }
        }
        deleteFromDb(id);
       writeUniversal(currentProduct,DataBaseConstants.INSERT_INTO_DEACTIVATED_PRODUCTS);
    }

    public static void deactivatedRead(){
        CollectionController.deactivatedProductList.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_DEACTIVATED_PRODUCTS);
            while(queryResult.next()){
                Product product = new Product(queryResult.getInt(ConstantWords.ID),queryResult.getString(ConstantWords.CATEGORY), queryResult.getDouble(ConstantWords.PRICE),
                        queryResult.getString(ConstantWords.NAME),queryResult.getString(ConstantWords.DESCRIPTION), queryResult.getString(ConstantWords.UPLOADER), queryResult.getString(ConstantWords.DATE), queryResult.getString(ConstantWords.DEACTIVATION_DATE));
                CollectionController.deactivatedProductList.add(product);
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void deactivatedActivate(int id){// from deactivated_products to products
        Product currentProduct = new Product();
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.DELETE_FROM_DEACTIVATED_PRODUCTS);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            for(Product product: CollectionController.deactivatedProductList){
                if(product.getId() == id){
                    CollectionController.allProductsList.add(product);
                    currentProduct = product;
                    CollectionController.deactivatedProductList.remove(product);
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        writeUniversal(currentProduct, DataBaseConstants.INSERT_INTO_PRODUCTS);
    }
    public static void writeUniversal(Product currentProduct, String database){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(database);
            preparedStatement.setString(1, currentProduct.getCategory() );
            preparedStatement.setDouble(2, currentProduct.getPrice());
            preparedStatement.setString(3, currentProduct.getName());
            preparedStatement.setString(4, currentProduct.getDescription());
            preparedStatement.setString(5, currentProduct.getUploader());
            preparedStatement.setString(6, currentProduct.getDate());
            if(database.equals(DataBaseConstants.INSERT_INTO_DEACTIVATED_PRODUCTS)){
                preparedStatement.setString(7, currentProduct.getDeactivationDate());
            }
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void deactivatedDelete(int id){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.DELETE_FROM_DEACTIVATED_PRODUCTS);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            for(Product product: CollectionController.deactivatedProductList){
                if(product.getId() == id){
                    CollectionController.deactivatedProductList.remove(product);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void editProduct(Product currentProduct){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_PRODUCTS);
            preparedStatement.setString(1, currentProduct.getCategory());
            preparedStatement.setDouble(2, currentProduct.getPrice());
            preparedStatement.setString(3, currentProduct.getName());
            preparedStatement.setString(4, currentProduct.getDescription());
            preparedStatement.setInt(5, currentProduct.getId());
            preparedStatement.executeUpdate();
            connection.close();
            int i = 0;
            for(Product product: CollectionController.allProductsList){
                if(product.getId() == currentProduct.getId()){
                    CollectionController.allProductsList.set(i,product);
                    break;
                }
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
