package com.guinproductions.behaviorrolodex.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guinp on 10/23/2017.
 */

public class Behavior {

    public String behaviorid;
    public String description;

    public Behavior(){

    }

    public Behavior(String behaviorid, String description){
        this.behaviorid = behaviorid;
        this.description = description;
    }

    @Exclude
    public Map<String, Object> behaviorMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("behaviorid", behaviorid);
        result.put("description", description);

        return result;
    }

    public String getBehaviorid(){return behaviorid;}

    public String setBehaviorid(String behaviorid){this.behaviorid = behaviorid;
    return behaviorid;}

    public String getDescription(){return description;}

    public String setDescription(String description){this.description = description;
        return description;}

}
