package xyz.esuku.startupingnitex.Model;

public class JopPostingModel {

    String id;
    String job_poster;
    String job_title;
    String job_desc;
    String job_company;
    String job_location;
    String job_remote;
    String job_category;
    String job_experience;
    String job_compensation;
    String time;
    String date;
    String job_status;


    public JopPostingModel(String id, String job_poster, String job_title, String job_desc, String job_company, String job_location, String job_remote, String job_category, String job_experience, String job_compensation, String time, String date, String job_status) {
        this.id = id;
        this.job_poster = job_poster;
        this.job_title = job_title;
        this.job_desc = job_desc;
        this.job_company = job_company;
        this.job_location = job_location;
        this.job_remote = job_remote;
        this.job_category = job_category;
        this.job_experience = job_experience;
        this.job_compensation = job_compensation;
        this.time = time;
        this.date = date;
        this.job_status = job_status;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_poster() {
        return job_poster;
    }

    public void setJob_poster(String job_poster) {
        this.job_poster = job_poster;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public String getJob_company() {
        return job_company;
    }

    public void setJob_company(String job_company) {
        this.job_company = job_company;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getJob_remote() {
        return job_remote;
    }

    public void setJob_remote(String job_remote) {
        this.job_remote = job_remote;
    }

    public String getJob_category() {
        return job_category;
    }

    public void setJob_category(String job_category) {
        this.job_category = job_category;
    }

    public String getJob_experience() {
        return job_experience;
    }

    public void setJob_experience(String job_experience) {
        this.job_experience = job_experience;
    }

    public String getJob_compensation() {
        return job_compensation;
    }

    public void setJob_compensation(String job_compensation) {
        this.job_compensation = job_compensation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }
}
