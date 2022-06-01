package com.example.fuse2;

public class UserDetails {

    protected String name,gender, surname,email, location, birthdate, bio;

    //default constructor
    public UserDetails(){

        this.name = "default";
        this.surname = "default";
        this.email = "deafault@t.com";
        this.birthdate = "00/00/00";
        this.location = "default";
        this.gender = "gender";
        this.bio = "bio";

    }

    //param constructor


    public UserDetails( String name, String surname,String gender, String email, String location, String birthdate, String bio) {

        this.gender = gender;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.location = location;
        this.birthdate = birthdate;
        this.bio = bio;
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
    public void setGender(String gender) { this.gender = gender; }

    public  void setBio(String bio) { this.bio = bio;}
    public String getBio(){ return bio;}

    @Override
    public String toString() {
        return "UserDetails{" +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", bio= '" + bio + '\'' +
                '}';
    }
}
