package com.example.fuse2;

public class UserDetails {

    protected String name,gender, surname,email, location, birthdate;

    //default constructor
    public UserDetails(){

        this.name = "default";
        this.surname = "default";
        this.email = "deafault@t.com";
        this.birthdate = "00/00/00";
        this.location = "default";
        this.gender = "gender";

    }

    //param constructor


    public UserDetails( String name, String surname,String gender, String email, String location, String birthdate) {

        this.gender = gender;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.location = location;
        this.birthdate = birthdate;
    }

    //getters and setters






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", birthdate='" + birthdate + '\'' +
                '}';
    }
}
