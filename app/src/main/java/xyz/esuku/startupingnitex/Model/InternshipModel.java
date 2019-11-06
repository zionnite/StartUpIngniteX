package xyz.esuku.startupingnitex.Model;

public class InternshipModel {
    String id;
    String user_id;
    String user_name;
    String full_name;
    String phone_no;
    String email;
    String user_img;
    String interest;
    String need_mentor;
    String city;


    public InternshipModel(String user_id, String user_name, String full_name, String phone_no, String email, String user_img, String interest, String need_mentor, String city) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.full_name = full_name;
        this.phone_no = phone_no;
        this.email = email;
        this.user_img = user_img;
        this.interest = interest;
        this.need_mentor = need_mentor;
        this.city = city;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getNeed_mentor() {
        return need_mentor;
    }

    public void setNeed_mentor(String need_mentor) {
        this.need_mentor = need_mentor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
