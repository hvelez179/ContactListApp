package com.example.android.contactregapp;


public class Contact {
    private long id;
    private String firstName, lastName, PicPath;

    public Contact() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String path) {
        this.PicPath = path;
    }


}