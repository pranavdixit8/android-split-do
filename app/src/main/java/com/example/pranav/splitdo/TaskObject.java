package com.example.pranav.splitdo;

public class TaskObject {

    private String text;
    private String creation_time;
    private String taken_time;
    private String completion_time;
    private String creator;
    private String creatorID;
    private String completor;
    private String completorID;
    private String taker;
    private String takerID;

    private String groupID;
    private String location;
    private String status;

    public TaskObject(){}

    public TaskObject(String text,String creation_time, String taken_time, String completion_time, String creator, String creatorID,String taker, String takerID,String completor, String completorID, String groupID, String status,String location){
        this.text = text;
        this.creation_time = creation_time;
        this.taken_time = taken_time;
        this.completion_time =completion_time;
        this.creator = creator;
        this.creatorID = creatorID;
        this.taker = taker;
        this.takerID = takerID;
        this.completor = completor;
        this.completorID = completorID;
        this.groupID = groupID;
        this.status = status;
        this.location = location;

    }

    public String getText(){return text;}

    public String getCreation_time() {
        return creation_time;
    }

    public String getTaken_time() {
        return taken_time;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public String getCreator() {
        return creator;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public String getTaker() {
        return taker;
    }

    public String getTakerID() {
        return takerID;
    }

    public String getCompletor() {
        return completor;
    }

    public String getCompletorID() {
        return completorID;
    }


    public String getGroupID() {
        return groupID;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation(){return location;}

}
