package sample.entities;

import sample.utils.CollectionController;

import java.util.Objects;

public class Product  {
    private int id;
    private String category;
    private double price;
    private String name;
    private String description;
    private String uploader;
    private String date;
    private String deactivationDate;

    public String getUploaderMail() {//iska si napraish oshte 1 kolona v db za mail ili prosto go vzemi s imeto ot user
        return uploaderMail;
    }

    public void setUploaderMail(String uploaderMail) {
        this.uploaderMail = uploaderMail;
    }

    private String uploaderMail;

    public void setId(int id) {
        this.id = id;
    }

    public String getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(String deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploader() {
        return uploader;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Product(){

    }
    public Product(int id,String category, double price, String name, String description, String username, String date) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
        this.uploader = username;
        getTheMail(username);
        this.date = date;
    }
    public Product(int id,String category, double price, String name, String description, String username, String date, String deactivationDate) {//this will be needed only for the deactivation
        this.id = id;
        this.category = category;
        this.price = price;
        this.name = name;
        this.description = description;
        this.uploader = username;
        getTheMail(username);
        this.date = date;
        this.deactivationDate = deactivationDate;
    }
    public Product(Product product){
        this.id = product.getId();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.name = product.getName();
        this.description = product.getDescription();
        this.uploader = product.getUploader();
        this.date = product.getDate();
        getTheMail(product.getName());

    }
    private void getTheMail(String name){
        for(User user: CollectionController.userList){
            if(user.getUsername().equals(name)){
                this.uploaderMail = user.getEmail();
                break;
            }
        }
    }
    @Override
    public boolean equals(Object o) {//we do not need the deactivation date
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(category, product.category) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(uploader, product.uploader) && Objects.equals(date, product.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, price, name, description, uploader, date);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uploader='" + uploader + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
