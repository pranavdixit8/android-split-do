package com.example.pranav.splitdo;

public class TaskObject {

    private String text;
    private String taskId;
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

    public TaskObject() {
    }

    public TaskObject(String text, String taskId, String creation_time, String taken_time, String completion_time, String creator, String creatorID, String taker, String takerID, String completor, String completorID, String groupID, String status, String location) {
        this.text = text;
        this.taskId = taskId;
        this.creation_time = creation_time;
        this.taken_time = taken_time;
        this.completion_time = completion_time;
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

    public String getText() {
        return text;
    }

    public String getTaskId() {
        return taskId;
    }

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

    public String getLocation() {
        return location;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTakerID(String takerID) {
        this.takerID = takerID;
    }

    public void setTaker(String taker) {
        this.taker = taker;
    }

    public void setTaken_time(String taken_time) {
        this.taken_time = taken_time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public void setCompletorID(String completorID) {
        this.completorID = completorID;
    }

    public void setCompletor(String completor) {
        this.completor = completor;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }
}
