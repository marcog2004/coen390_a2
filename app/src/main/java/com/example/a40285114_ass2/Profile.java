package com.example.a40285114_ass2;

public class Profile {
    private int profileId;
    private String name;
    private String surname;
    private float gpa;
    private String creationTime;

    public Profile(int profileId, String name, String surname, float gpa, String creationTime){
        this.profileId = profileId;
        this.name = name;
        this.surname = surname;
        this.gpa = gpa;
        this.creationTime = creationTime;
    }

    public int getProfileId(){
        return profileId;
    }

    public void setProfileId(int profileId){
        this.profileId = profileId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public float getGpa(){
        return gpa;
    }

    public void setGpa(float gpa){
        this.gpa = gpa;
    }

    public String getCreationTime(){
        return creationTime;
    }

    public void setCreationTime(String creationTime){
        this.creationTime = creationTime;
    }
}
