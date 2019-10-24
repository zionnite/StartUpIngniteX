package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class ForumDetailModel {

    @SerializedName("a_id")
    public int a_id;

    @SerializedName("question_id")
    public int question_id;

    @SerializedName("a_user_image")
    public String user_image;

    @SerializedName("a_user_name")
    public String user_name;

    @SerializedName("a_city")
    public String city;

    @SerializedName("a_time")
    public String time_ago;

    @SerializedName("a_content")
    public String content;

    @SerializedName("counter")
    public String counter;

    @SerializedName("is_text")
    String isText;

    @SerializedName("a_content_img")
    public String a_content_img;

    public ForumDetailModel(int a_id,int question_id,String user_image, String user_name, String city, String time_ago, String content,String counter, String isText, String a_content_img) {
        this.a_id = a_id;
        this.question_id = question_id;
        this.user_image = user_image;
        this.city = city;
        this.time_ago = time_ago;
        this.content = content;
        this.counter = counter;
        this.isText = isText;
        this.a_content_img = a_content_img;
    }

    public ForumDetailModel() {
    }

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
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

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getIsText() {
        return isText;
    }

    public void setIsText(String isText) {
        this.isText = isText;
    }

    public String getA_content_img() {
        return a_content_img;
    }

    public void setA_content_img(String a_content_img) {
        this.a_content_img = a_content_img;
    }
}
