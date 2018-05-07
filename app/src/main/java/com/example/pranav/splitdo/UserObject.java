package com.example.pranav.splitdo;

public class UserObject {

    private String name;
    private String email;
//    private ArrayList<TaskObject> tasks;

    public UserObject(){}

    public UserObject(String name, String email){
        this.name = name;
        this.email = email;
//        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
