package xyz.esuku.startupingnitex.Model;

import com.google.gson.annotations.SerializedName;

public class BusinessListingModel {

    @SerializedName("bus_id")
    public int id;

    @SerializedName("bus_logo")
    public String logo;

    @SerializedName("bus_name")
    public String name;

    @SerializedName("bus_phone")
    public String phone;

    @SerializedName("bus_email")
    public String email;

    @SerializedName("bus_address")
    public String address;

    public BusinessListingModel(String logo, String name, String phone, String email, String address) {
        this.logo = logo;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
