package com.example.lost;

public class users {
    private String firstName;
    private String surname;
    private String ID;

    public users(){

    }

    public users(String ID,String firstName, String surname) {
        this.ID = ID;
        this.firstName = firstName;
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
