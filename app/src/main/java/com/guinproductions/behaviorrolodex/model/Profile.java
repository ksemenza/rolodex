package com.guinproductions.behaviorrolodex.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guinp on 10/20/2017.
 */

public class Profile {
    public String profileid;
    public String fname;
    public String lname;
    public String noted;
    public String imageview;

    public Profile(){

    }

    public Profile(String profileid, String fname, String lname, String noted){
        this.profileid = profileid;
        this.fname = fname;
        this.lname = lname;
        this.noted = noted;
        this.imageview = imageview;
    }



    // [START post_to_map]
    @Exclude
    public Map<String, Object> profileMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("profileid", profileid);
        result.put("fname", fname);
        result.put("lname", lname);
        result.put("noted", noted);
        result.put("imageview", imageview);

        return result;
    }

    public String getProfileid(){return profileid;}

    public String setProfileid(String profileid){this.profileid = profileid;
        return profileid;
    }

    public String getFname(){return fname;}

    public String setFname(String fname){this.fname = fname;
        return fname;
    }
    public String getLname(){return lname;}

    public String setLname(String lname){this.lname = lname;
        return lname;
    }
    public String getNoted(){return noted;}

    public String setNoted(String noted){this.noted = noted;
        return noted;
    }

    public String getImageview(){return imageview;}

    public String setImageview(String imageview){this.imageview = imageview;
    return imageview;
    }
}
