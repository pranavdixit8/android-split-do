package com.example.pranav.splitdo;

public class UserObject {

    private String name;
    private String email;
    private String mUid;

    public UserObject(){}

    public UserObject(String name, String email, String mUid){
        this.name = name;
        this.email = email;
        this.mUid = mUid;
    }

    public String getUid() {
        return mUid;
    }



    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
