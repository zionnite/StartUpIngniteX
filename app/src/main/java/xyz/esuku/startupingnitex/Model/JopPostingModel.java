package xyz.esuku.startupingnitex.Model;

public class JopPostingModel {

    String job_title;
    String job_desc;
    String job_company;
    String job_location;
    String job_remotely;
    String job_category;
    String job_experience;
    String job_compensation;

    public JopPostingModel(String job_title, String job_desc, String job_company, String job_location, String job_remotely, String job_category, String job_experience, String job_compensation) {
        this.job_title = job_title;
        this.job_desc = job_desc;
        this.job_company = job_company;
        this.job_location = job_location;
        this.job_remotely = job_remotely;
        this.job_category = job_category;
        this.job_experience = job_experience;
        this.job_compensation = job_compensation;
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

    public String getJob_remotely() {
        return job_remotely;
    }

    public void setJob_remotely(String job_remotely) {
        this.job_remotely = job_remotely;
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
}
