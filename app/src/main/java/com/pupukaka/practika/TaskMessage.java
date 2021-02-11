package com.pupukaka.practika;

public class TaskMessage {
    String imageUrl;
    String time;
    int executionStatus;
    String notes;
    String workplaceNumber;

    public TaskMessage() {
    }

    public TaskMessage(String imageUrl, String time, int executionStatus, String notes, String workplaceNumber) {
        this.imageUrl = imageUrl;
        this.time = time;
        this.executionStatus = executionStatus;
        this.notes = notes;
        this.workplaceNumber=workplaceNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public int getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(int executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getWorkplaceNumber() {
        return workplaceNumber;
    }

    public void setWorkplaceNumber(String workplaceNumber) {
        this.workplaceNumber = workplaceNumber;
    }
}
