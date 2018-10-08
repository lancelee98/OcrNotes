package com.smujsj16.ocr_notes.Entity;

public class User {
    private String user_id;
    private String phone_number;
    private String password;

    public User(String phone_number, String password)
    {
        this.password=password;
        this.phone_number=phone_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}


