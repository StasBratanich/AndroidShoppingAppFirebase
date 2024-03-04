package com.example.ShoppingApp.Data;

public class UsersData {

    public String email;
    public String password;
    public String id;
    public UsersData(){
    }
    public UsersData(String email, String password, String id) {
        this.email = email;
        this.password = password;
        this.id = id;
    }
    public UsersData(String email, String id) {
        this.email = email;
        this.id = id;
    }
    @Override
    public String toString() {
        return "Data{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
