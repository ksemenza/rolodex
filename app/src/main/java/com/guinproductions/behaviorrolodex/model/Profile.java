package com.guinproductions.behaviorrolodex.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guinp on 10/20/2017.
 */

public class Profile {
    public String uid;
    public String fname;
    public String lname;
    public String noted;
    public String imageview;

    public Profile(){

    }

    public Profile(String uid, String fname, String lname, String noted){
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.noted = noted;
        this.imageview = imageview;
    }



    // [START post_to_map]
    @Exclude
    public Map<String, Object> profileMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("fname", fname);
        result.put("lname", lname);
        result.put("noted", noted);
        result.put("imageview", imageview);

        return result;
    }

    public String getUid(){return uid;}

    public String setUid(String uid){this.uid = uid;
        return uid;
    }
}
