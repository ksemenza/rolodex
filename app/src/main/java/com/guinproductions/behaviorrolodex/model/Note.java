package com.guinproductions.behaviorrolodex.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guinp on 10/20/2017.
 */

public class Note {


    public String uid;
    public String title;
    public String body;
    public String date;
    public String time;

    public Note(){

    }

    public Note(String uid, String title, String body, String date, String time){
        this.uid = uid;
        this.title = title;
        this.body = body;
        this.date = date;
        this.time = time;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> noteMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("body", body);
        result.put("date", date);
        result.put("time", time);

        return result;
    }

}
