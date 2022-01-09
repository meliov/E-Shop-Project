package sample.utils.database;

import sample.entities.Message;
import sample.utils.CollectionController;
import sample.utils.constants.ConstantWords;
import sample.utils.constants.DataBaseConstants;

import java.sql.*;

public class DatabaseMessages {
    public static void writeToDB(Message message){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DataBaseConstants.INSERT_INTO_MESSAGES);
            preparedStatement.setString(1, message.getFrom());
            preparedStatement.setString(2, message.getTo());
            preparedStatement.setString(3, message.getContent());
            preparedStatement.setString(4, message.getDate());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        readFromDB();
    }
    public static void readFromDB(){
        CollectionController.messageList.clear();
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet queryResult = statement.executeQuery(DataBaseConstants.SELECT_FROM_MESSAGES);
            while(queryResult.next()){
                Message message = new Message(queryResult.getString(ConstantWords.FROM_USER),queryResult.getString(ConstantWords.TO_USER),queryResult.getString(ConstantWords.CONTENT), queryResult.getString(ConstantWords.SENDING_DATE));
                CollectionController.messageList.add(message);
            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
