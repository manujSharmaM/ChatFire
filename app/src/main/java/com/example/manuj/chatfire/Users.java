package com.example.manuj.chatfire;

public class Users {

    private int  usersImageResource;

    private String usersName;

    public Users(String usersName,int usersImageResource){
        this.usersName=usersName;
        this.usersImageResource=usersImageResource;

    }



    public String getUsersName() {
        return usersName;
    }

    public int getUsersImageResource() {
        return usersImageResource;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public void setUsersImageResource(int usersImageResource) {
        this.usersImageResource = usersImageResource;
    }
}
