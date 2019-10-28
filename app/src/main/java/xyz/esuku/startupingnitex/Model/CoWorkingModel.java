package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class CoWorkingModel {
    @SerializedName("id")
    String id;

    @SerializedName("user_name")
    String user_name;

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

    @SerializedName("category")
    String category;

    @SerializedName("country")
    String country;

    @SerializedName("state")
    String state;

    @SerializedName("city")
    String city;

    public CoWorkingModel(String id, String user_name, String full_name, String address, String desc, String email, String phone, String category) {
        this.id = id;
        this.full_name = full_name;
        this.user_name = user_name;
        this.address = address;
        this.desc = desc;
        this.email = email;
        this.phone = phone;
        this.category = category;
    }

    public CoWorkingModel(String id, String user_name, String full_name, String address, String desc, String email, String phone, String category, String country, String state, String city) {
        this.id = id;
        this.user_name = user_name;
        this.full_name = full_name;
        this.address = address;
        this.desc = desc;
        this.email = email;
        this.phone = phone;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
