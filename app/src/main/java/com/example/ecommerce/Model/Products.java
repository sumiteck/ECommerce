package com.example.ecommerce.Model;

public class Products {

    public String categoryName,date,description,image,pname,price,time;

    public Products(){

    }

    public Products(String categoryName, String date, String description, String image, String pname, String price, String time) {
        this.categoryName = categoryName;
        this.date = date;
        this.description = description;
        this.image = image;
        this.pname = pname;
        this.price = price;
        this.time = time;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
