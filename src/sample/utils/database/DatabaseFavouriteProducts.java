package sample.utils.database;

import sample.entities.Product;
import sample.utils.CollectionController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseFavouriteProducts {
    public static void writeFavouriteProduct(Product currentProduct, String database, String owner){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(database);
            preparedStatement.setString(1, currentProduct.getCategory());
            preparedStatement.setDouble(2, currentProduct.getPrice());
            preparedStatement.setString(3, currentProduct.getName());
            preparedStatement.setString(4, currentProduct.getDescription());
            preparedStatement.setString(5, currentProduct.getUploader());
            preparedStatement.setString(6, currentProduct.getDate());
            preparedStatement.setString(7, owner);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void readFavouriteProducts(){
        CollectionController.favouriteProductsMap.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_FAVOURITE_PRODUCTS);
            while(queryResult.next()){
                Product product = new Product(queryResult.getInt(ConstantWords.ID),queryResult.getString(ConstantWords.CATEGORY), queryResult.getDouble(ConstantWords.PRICE),
                        queryResult.getString(ConstantWords.NAME),queryResult.getString(ConstantWords.DESCRIPTION), queryResult.getString(ConstantWords.UPLOADER),queryResult.getString(ConstantWords.DATE));
                String owner = queryResult.getString(ConstantWords.OWNER);
                if(CollectionController.favouriteProductsMap.containsKey(owner)){
                    CollectionController.favouriteProductsMap.get(owner).add(product);
                }else{
                    List<Product> tempList = new ArrayList<>();
                    tempList.add(product);
                    CollectionController.favouriteProductsMap.put(owner, tempList);
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void deleteFavouriteProduct(int id){//nekoi sa ti nujni i taka
        deleteFavouriteProduct(id,DataBaseConstants.DELETE_FROM_FAVOURITE_PRODUCTS,CollectionController.favouriteProductsMap, CollectionController.currentUser.getUsername());
    }
    public static void deleteFavouriteProduct(int id, String database, Map<String, List<Product>> mainMap,String user){//tva si raboti samo za otgore
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(database);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            if(mainMap.containsKey(user)) {
                for (Product product : mainMap.get(user)) {
                    if (product.getId() == id) {
                        mainMap.get(CollectionController.currentUser.getUsername()).remove(product);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void deactivateFavWrite(Product product){//delete from favourite, add to deactivated_favourite
        getMapsAndDbDone(product,CollectionController.favouriteProductsMap, CollectionController.deactivatedFavouriteProductsMap,DataBaseConstants.INSERT_INTO_DEACTIVATED_FAVOURITE_PRODUCTS);

    }
    public static void deactivatedFavRead(){
        CollectionController.deactivatedFavouriteProductsMap.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_DEACTIVATED_FAVOURITE_PRODUCTS);
            while(queryResult.next()){
                Product product = new Product(queryResult.getInt(ConstantWords.ID),queryResult.getString(ConstantWords.CATEGORY), queryResult.getDouble(ConstantWords.PRICE),
                        queryResult.getString(ConstantWords.NAME),queryResult.getString(ConstantWords.DESCRIPTION), queryResult.getString(ConstantWords.UPLOADER),queryResult.getString(ConstantWords.DATE));
                String owner = queryResult.getString(ConstantWords.OWNER);
                if(CollectionController.deactivatedFavouriteProductsMap.containsKey(owner)){
                    CollectionController.deactivatedFavouriteProductsMap.get(owner).add(product);
                }else{
                    List<Product> tempList = new ArrayList<>();
                    tempList.add(product);
                    CollectionController.deactivatedFavouriteProductsMap.put(owner, tempList);
                }
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void deactivatedActivate(Product product){//delete from deactivated_favourites,add to fa favourites
        getMapsAndDbDone(product, CollectionController.deactivatedFavouriteProductsMap,CollectionController.favouriteProductsMap,DataBaseConstants.INSERT_INTO_FAVOURITE_PRODUCTS);
        universalDeletionFromDeactivated(product, DataBaseConstants.DELETE_FROM_DEACTIVATED_FAVOURITE_PRODUCTS);
    }
    public static void deactivatedDelete(Product product){
        List<String> allUsersWhoAddedTheProduct = new ArrayList<>();
        for(Map.Entry<String, List<Product>> e: CollectionController.deactivatedFavouriteProductsMap.entrySet()){
            if(e.getValue().contains(product)){
                allUsersWhoAddedTheProduct.add(e.getKey());//we are making the check for users here
            }
        }
        for(String owner: allUsersWhoAddedTheProduct){
            CollectionController.deactivatedFavouriteProductsMap.get(owner).removeIf(p -> p.getDescription().equals(product.getDescription()));
        }
        universalDeletionFromDeactivated(product, DataBaseConstants.DELETE_FROM_DEACTIVATED_FAVOURITE_PRODUCTS);
        deleteFavouriteByDescription(product);
    }
   private static void deleteFavouriteByDescription(Product product){
        universalDeletionFromDeactivated(product,DataBaseConstants.DELETE_FROM_ACTIVE_FAVOURITE_PRODUCTS_By_DESCRIPTION);
        List<String> allUsersWhoAddedTheProduct = new ArrayList<>();
        for(Map.Entry<String, List<Product>> e: CollectionController.favouriteProductsMap.entrySet()){
            if(e.getValue().contains(product)){
                allUsersWhoAddedTheProduct.add(e.getKey());//we are making the check for users here
            }
        }
        for(String owner: allUsersWhoAddedTheProduct){
            CollectionController.favouriteProductsMap.get(owner).removeIf(p -> p.getDescription().equals(product.getDescription()));
        }
    }
    private static void getMapsAndDbDone(Product product, Map<String, List<Product>> mainMap, Map<String, List<Product>> secMap, String database ){//delete from favourite, add to deactivated_favourite
        List<String> allUsersWhoAddedTheProduct = new ArrayList<>();
        for(Map.Entry<String, List<Product>> e: mainMap.entrySet()){
            if(e.getValue().contains(product)){
                allUsersWhoAddedTheProduct.add(e.getKey());//we are making the check for users here
            }
        }
        for(String owner: allUsersWhoAddedTheProduct){
            for(Product p : mainMap.get(owner)){
                if(p.getDescription().equals(product.getDescription())){//because the id wont be the same
                    if(secMap.containsKey(owner)){
                        secMap.get(owner).add(p);
                    }else{
                        List<Product> tempList = new ArrayList<>();
                        tempList.add(p);
                        secMap.put(owner, tempList);
                    }
                    if(database.equals(DataBaseConstants.INSERT_INTO_DEACTIVATED_FAVOURITE_PRODUCTS)) {
                        writeFavouriteProduct(p, DataBaseConstants.INSERT_INTO_DEACTIVATED_FAVOURITE_PRODUCTS, owner);
                    }else{
                        writeFavouriteProduct(p, DataBaseConstants.INSERT_INTO_FAVOURITE_PRODUCTS, owner);
                    }
                }

            }
        }
        if(database.equals(DataBaseConstants.INSERT_INTO_DEACTIVATED_FAVOURITE_PRODUCTS)) {
            deleteFavouriteByDescription(product);
        }

    }

    private static void universalDeletionFromDeactivated(Product product, String database){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(database);
            preparedStatement.setString(1, product.getDescription());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
