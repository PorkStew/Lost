package com.example.lost;

public class users {
    private String firstName;
    private String surname;
    private String username;
    private String email;
    private String transportType;
    private String fuelType;
    private String password;
    private String metricSystem;
    private static String ID;

    public users(){

    }

    public users(String firstName, String surname,String username, String email, String fuelType,String transportType ,String password, String metricSystem) {
        this.metricSystem = metricSystem;
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.fuelType = fuelType;
        this.transportType = transportType;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getMetricSystem() {
        return metricSystem;
    }

    public void setMetricSystem(String metricSystem) {
        this.metricSystem = metricSystem;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
