package sample.utils.constants;

public class DataBaseConstants {
    public static final String SELECT_FROM_USERS = "select * from users";
    public static final String INSERT_INTO_USERS = "insert into users( firstname, lastname, username, password, email, role) values(?, ?, ?, ?, ?, ?)";
    public static final String DELETE_FROM_USERS = "delete from users where username = ?";
    public static final String SELECT_FROM_CATEGORIES = "select * from categories";
    public static final String INSERT_INTO_CATEGORIES = "insert into categories(category) values(?)";
    public static final String SELECT_FROM_PRODUCTS = "select * from products";
    public static final String INSERT_INTO_PRODUCTS = "insert into products( category, price, name, description, uploader, date) values(?, ?, ?, ?, ?, ?)";
    public static final String DELETE_FROM_PRODUCTS = "DELETE FROM products WHERE id = ?";
    public static final String SELECT_FROM_DEACTIVATED_PRODUCTS = "select * from deactivated_products";
    public static final String INSERT_INTO_DEACTIVATED_PRODUCTS = "insert into deactivated_products( category, price, name, description, uploader, date, deactivation) values(?, ?, ?, ?, ?, ?, ?)";
    public static final String DELETE_FROM_DEACTIVATED_PRODUCTS = "DELETE FROM deactivated_products WHERE id = ?";
    public static final String UPDATE_PRODUCTS = "update products set category = ?, price = ?, name = ?, description = ? where id = ?";
    public static final String INSERT_INTO_FAVOURITE_PRODUCTS = "insert into favourite_products( category, price, name, description, uploader, date, owner) values(?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_FROM_FAVOURITE_PRODUCTS = "select * from favourite_products";
    public static final String DELETE_FROM_FAVOURITE_PRODUCTS = "DELETE FROM favourite_products WHERE id = ?";
    public static final String INSERT_INTO_DEACTIVATED_FAVOURITE_PRODUCTS = "insert into deactivated_favourite_products( category, price, name, description, uploader, date, owner) values(?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_FROM_DEACTIVATED_FAVOURITE_PRODUCTS = "select * from deactivated_favourite_products";
    public static final String DELETE_FROM_DEACTIVATED_FAVOURITE_PRODUCTS = "DELETE FROM deactivated_favourite_products WHERE description = ?";
    public static final String DELETE_FROM_ACTIVE_FAVOURITE_PRODUCTS_By_DESCRIPTION = "DELETE FROM favourite_products WHERE description = ?";
    public static final String UPDATE_CATEGORY = "update categories set category = ? where category = ?";
    public static final String UPDATE_CATEGORY_IN_PRODUCTS = "update products set category = ? where category = ?";
    public static final String UPDATE_CATEGORY_IN_FAVOURITE_PRODUCTS = "update favourite_products set category = ? where category = ?";
    public static final String UPDATE_CATEGORY_IN_DEACTIVATED_PRODUCTS = "update deactivated_products set category = ? where category = ?";
    public static final String UPDATE_CATEGORY_IN_DEACTIVATED_FAVOURITE_PRODUCTS = "update deactivated_favourite_products set category = ? where category = ?";
    public static final String UPDATE_EMAIL_FROM_USERS = "update users set email = ? where username = ?";
    public static final String UPDATE_PASSWORD_FROM_USERS = "update users set password = ? where username = ?";
    public static final String INSERT_INTO_MESSAGES = "insert into messages( from_user, to_user, content, sending_date) values(?, ?, ?, ?)";
    public static final String SELECT_FROM_MESSAGES = "select * from messages";
}
