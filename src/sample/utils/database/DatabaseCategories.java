package sample.utils.database;

import sample.entities.Category;
import sample.utils.CollectionController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;

import java.sql.*;

public class DatabaseCategories {
    public static void readFromDb(){
        CollectionController.categoriesList.clear();
        CollectionController.categoriesListForAdmins.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_CATEGORIES);
            while(queryResult.next()){
                CollectionController.categoriesList.add(queryResult.getString(ConstantWords.CATEGORY));
                CollectionController.categoriesListForAdmins.add(new Category(queryResult.getString(ConstantWords.CATEGORY)));
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void writeToDB(String category){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.INSERT_INTO_CATEGORIES);
            preparedStatement.setString(1, category);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        DatabaseCategories.readFromDb();

    }
    public static void editDb(Category category, Category oldCategory){//tr da updeitna i sichki produkti
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_CATEGORY);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, oldCategory.getCategory());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_CATEGORY_IN_PRODUCTS);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, oldCategory.getCategory());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_CATEGORY_IN_FAVOURITE_PRODUCTS);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, oldCategory.getCategory());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_CATEGORY_IN_DEACTIVATED_PRODUCTS);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, oldCategory.getCategory());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_CATEGORY_IN_DEACTIVATED_FAVOURITE_PRODUCTS);
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.setString(2, oldCategory.getCategory());
            preparedStatement.executeUpdate();
            connection.close();
            DatabaseCategories.readFromDb();
            DatabaseProducts.readFromDB(DataBaseConstants.SELECT_FROM_PRODUCTS,CollectionController.allProductsList);
            DatabaseFavouriteProducts.readFavouriteProducts();
            DatabaseProducts.deactivatedRead();
            DatabaseFavouriteProducts.deactivatedFavRead();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        //se iska se razpravesh sus sichki drugi
    }
}
