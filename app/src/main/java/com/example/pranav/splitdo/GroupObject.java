package com.example.pranav.splitdo;

public class GroupObject {

    private String text;
    private String creator;
    private String creatorID;
    private String groupID;
    private String tasksList;
    private String usersList;

    public GroupObject(){}

    public GroupObject(String text, String creator, String creatorID,String groupID, String tasks, String users){
        this.text = text;
        this.creator = creator;
        this.creatorID = creatorID;
        this.groupID = groupID;
        this.tasksList = tasks;
        this.usersList = users;

    }

    public String getText(){return text;}

    public String getCreator() {
        return creator;
    }

    public String getCreatorID() {
        return creatorID;
    }


    public String getGroupID() {
        return groupID;
    }

    public String getTasks() {
        return tasksList;
    }

    public String getUsers() {
        return usersList;
    }



}
