package com.example.a40285114_ass2;

public class Access {

    // variables

    private final long accessId;
    private final int profileId;
    private final String type;
    private final String timestamp;

    // constructor
    public Access(long accessId, int profileId, String type, String timestamp){
        this.accessId = accessId;
        this.profileId = profileId;
        this.type = type;
        this.timestamp = timestamp;
    }

    // getters and setters
    public String getType(){
        return type;
    }

    public String getTimestamp(){
        return timestamp;
    }

}
