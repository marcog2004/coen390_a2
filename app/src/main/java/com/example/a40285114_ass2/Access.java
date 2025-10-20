package com.example.a40285114_ass2;

public class Access {

    private final long accessId;
    private final int profileId;
    private final String type;
    private final String timestamp;

    public Access(long accessId, int profileId, String type, String timestamp){
        this.accessId = accessId;
        this.profileId = profileId;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getType(){
        return type;
    }

    public String getTimestamp(){
        return timestamp;
    }

}
