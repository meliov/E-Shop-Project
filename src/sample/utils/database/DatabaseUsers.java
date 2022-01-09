package sample.utils.database;

import sample.entities.Product;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;
import sample.entities.User;
import sample.utils.CollectionController;

import java.io.IOException;
import java.sql.*;

public class DatabaseUsers {
    public static void write(String firstName, String lastName,String username, String password, String email, String role){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.INSERT_INTO_USERS);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, role);
            preparedStatement.executeUpdate();
            connection.close();
            CollectionController.userList.add(new User(firstName,lastName,username,password,email,role));
        } catch (SQLException e) {
            e.getCause();
        }
    }
    public static void readFromDB(){
        CollectionController.userList.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_USERS);
            while(queryResult.next()){
                User user = new User(queryResult.getString(ConstantWords.FIRST_NAME), queryResult.getString(ConstantWords.LAST_NAME),
                        queryResult.getString(ConstantWords.USERNAME), queryResult.getString(ConstantWords.PASSWORD), queryResult.getString(ConstantWords.EMAIL),
                        queryResult.getString(ConstantWords.ROLE));
                CollectionController.userList.add(user);
            }

            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void deleteFromDb(User user){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.DELETE_FROM_USERS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
            CollectionController.userList.removeIf(u -> u.getUsername().equals(user.getUsername()));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static void editEmail(String newEmail, String username){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_EMAIL_FROM_USERS);
            preparedStatement.setString(1, newEmail);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        DatabaseUsers.readFromDB();
    }
    public static void editPassword(String newPass, String username){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.UPDATE_PASSWORD_FROM_USERS);
            preparedStatement.setString(1, newPass);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        DatabaseUsers.readFromDB();
    }
}
