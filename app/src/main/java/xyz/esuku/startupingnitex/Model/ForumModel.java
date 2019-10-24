package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class ForumModel {

    @SerializedName("ask_id")
    String id;

    @SerializedName("ask_user_image")
    public String user_image;

    @SerializedName("ask_user_name")
    public String user_name;

    @SerializedName("ask_city")
    public String city;

    @SerializedName("ask_time")
    public String time_ago;

    @SerializedName("ask_content")
    public String content;

    @SerializedName("last_paticipator")
    public String last_commenter;

    @SerializedName("question_status")
    public String question_status;

    @SerializedName("question_counter")
    String counter;

    public ForumModel(String ask_id,String user_image, String user_name, String city, String time_ago, String content, String last_commenter, String question_status, String counter) {
        this.id = ask_id;
        this.user_image = user_image;
        this.user_name = user_name;
        this.city = city;
        this.time_ago = time_ago;
        this.content = content;
        this.last_commenter = last_commenter;
        this.question_status = question_status;
        this.counter = counter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime_ago() {
        return time_ago;
    }

    public void setTime_ago(String time_ago) {
        this.time_ago = time_ago;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLast_commenter() {
        return last_commenter;
    }

    public void setLast_commenter(String last_commenter) {
        this.last_commenter = last_commenter;
    }

    public String getQuestion_status() {
        return question_status;
    }

    public void setQuestion_status(String question_status) {
        this.question_status = question_status;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
