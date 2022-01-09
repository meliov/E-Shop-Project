package sample.utils;

import sample.entities.Category;
import sample.entities.Message;
import sample.entities.Product;
import sample.entities.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CollectionController {
    public static final List<User> userList = new ArrayList<>();
    public static final List<String> categoriesList = new ArrayList<>();
    public static final List<Product> allProductsList = new ArrayList<>();
    public static User currentUser;
    public static final List<Product> deactivatedProductList = new ArrayList<>();
    public static final Map<String, List<Product>> favouriteProductsMap = new LinkedHashMap<>();
    public static final Map<String, List<Product>> deactivatedFavouriteProductsMap= new LinkedHashMap<>();
    public static final List<Category> categoriesListForAdmins = new ArrayList<>();
    public static final List<Message> messageList = new ArrayList<>();
    public static List<Product> getUserProducts(List<Product> products, String uploader){
        List<Product> list = new ArrayList<>();
        for (Product product:products ) {
            if(product.getUploader().equals(uploader)){
                list.add(product);
            }
        }
        return list;
    }
}
