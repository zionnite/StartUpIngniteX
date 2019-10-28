package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class CoWorkingModel {
    @SerializedName("id")
    String id;

    @SerializedName("user_name")
    String user_name;

    @SerializedName("user_img")
    String user_img;

    @SerializedName("full_name")
    String full_name;

    @SerializedName("address")
    String address;

    @SerializedName("desc")
    String desc;

    @SerializedName("email")
    String email;

    @SerializedName("phone")
    String phone;

    @SerializedName("accelerator")
    String accelerator;

    @SerializedName("co_working_space")
    String co_working_space;

    @SerializedName("hub")
    String hub;

    @SerializedName("country")
    String country;

    @SerializedName("state")
    String state;

    @SerializedName("city")
    String city;

    public CoWorkingModel(String id, String user_name, String user_img, String full_name, String address, String desc, String email, String phone,
                          String accelerator, String co_working_space, String hub) {
        this.id = id;
        this.user_name = user_name;
        this.user_img = user_img;
        this.full_name = full_name;
        this.address = address;
        this.desc = desc;
        this.email = email;
        this.phone = phone;
        this.accelerator = accelerator;
        this.co_working_space = co_working_space;
        this.hub = hub;
    }

    public CoWorkingModel(String id, String user_name, String user_img, String full_name, String address, String desc, String email, String phone, String accelerator, String co_working_space, String hub, String country, String state, String city) {
        this.id = id;
        this.user_name = user_name;
        this.user_img = user_img;
        this.full_name = full_name;
        this.address = address;
        this.desc = desc;
        this.email = email;
        this.phone = phone;
        this.accelerator = accelerator;
        this.co_working_space = co_working_space;
        this.hub = hub;
        this.country = country;
        this.state = state;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }

    public String getCo_working_space() {
        return co_working_space;
    }

    public void setCo_working_space(String co_working_space) {
        this.co_working_space = co_working_space;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
