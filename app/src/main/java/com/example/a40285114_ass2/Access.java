package com.example.a40285114_ass2;

public class Access {

    private long accessId;
    private int profileId;
    private String type;
    private String timestamp;

    public Access(long accessId, int profileId, String type, String timestamp){
        this.accessId = accessId;
        this.profileId = profileId;
        this.type = type;
        this.timestamp = timestamp;
    }

    public long getAccessId(){
        return accessId;
    }

    public void setAccessId(long accessId){
        this.accessId = accessId;
    }

    public int getProfileId(){
        return profileId;
    }

    public void setProfileId(int profileId){
        this.profileId = profileId;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}
