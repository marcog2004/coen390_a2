package com.example.a40285114_ass2;

public class Profile {
    private final int profileId;
    private String name;
    private final String surname;
    private final float gpa;
    private final String creationTime;

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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }


    public float getGpa(){
        return gpa;
    }


    public String getCreationTime(){
        return creationTime;
    }

}
