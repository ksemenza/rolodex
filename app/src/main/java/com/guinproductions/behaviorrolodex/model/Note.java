package com.guinproductions.behaviorrolodex.model;

import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guinp on 10/20/2017.
 */

public class Note {


    public String noteid;
    public String title;
    public String content;
    public String date;
    public String time;

    public Note(String title, String content, String date, String time){

    }

    public Note(String noteid, String title, String content, String date, String time){
        this.noteid = noteid;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> noteMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("noteid", noteid);
        result.put("title", title);
        result.put("content", content);
        result.put("date", date);
        result.put("time", time);

        return result;
    }

    public String getNoteid(){return noteid;}

    public String setNoteid(String noteid){this.noteid = noteid;
        return noteid;
    }

    public String getTitle(){return title;}

    public String setTitle(String title){this.title = title;
        return title;
    }

    public String getContent(){return content;}

    public String setContent(String content){this.content = content;
        return content;
    }


    public String getDate() {
        final Calendar c = Calendar.getInstance();

        return(new StringBuilder()
                .append(c.get(Calendar.MONTH) + 1).append("/")
                .append(c.get(Calendar.DAY_OF_MONTH)).append("/")
                .append(c.get(Calendar.YEAR)).append(" ")).toString();
}


    public String setDate(String date){this.date = date;
        return date;
    }

    public String getTime() {
        final Calendar c = Calendar.getInstance();

        return(new StringBuilder()
                .append(c.get(Calendar.HOUR_OF_DAY)).append(":")
                .append(c.get(Calendar.MINUTE)).append(":")
                .append(c.get(Calendar.SECOND)).append(" ")).toString();

    }

    public String setTime(String time){this.time = time;
        return time;
    }

}
