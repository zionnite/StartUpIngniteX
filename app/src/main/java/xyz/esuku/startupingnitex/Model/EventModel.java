package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class EventModel {
    @SerializedName("event_id")
    public int id;

    @SerializedName("event_title")
    public String event_title;

    @SerializedName("event_start_date")
    public String event_date;

    @SerializedName("event_start_time")
    public String event_time;

    @SerializedName("event_dec")
    public String event_desc;

    @SerializedName("event_counter")
    public String event_counter;

    public String getEvent_checker() {
        return event_checker;
    }

    public void setEvent_checker(String event_checker) {
        this.event_checker = event_checker;
    }

    @SerializedName("event_checker")
    public String event_checker;

    public EventModel(String event_title, String event_date, String event_time, String event_desc, String event_counter, String event_checker) {
        this.event_title = event_title;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_desc = event_desc;
        this.event_counter = event_counter;
        this.event_checker = event_checker;
    }

    public EventModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_counter() {
        return event_counter;
    }

    public void setEvent_counter(String event_counter) {
        this.event_counter = event_counter;
    }


}
