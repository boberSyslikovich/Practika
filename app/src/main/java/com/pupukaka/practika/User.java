package com.pupukaka.practika;

public class User {

    private String userId;
    private String name;
    private String email;
    private int mode;
    private int workplaceNumber;

    public User() {
    }

    public User(String userId, String name, String email, int mode, int workplaceNumber) {
        this.userId=userId;
        this.name = name;
        this.email = email;
        this.mode = mode;
        this.workplaceNumber = workplaceNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getWorkplaceNumber() {
        return workplaceNumber;
    }

    public void setWorkplaceNumber(int workplaceNumber) {
        this.workplaceNumber = workplaceNumber;
    }
}
