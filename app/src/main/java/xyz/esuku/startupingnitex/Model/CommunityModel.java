package xyz.esuku.startupingnitex.Model;

public class CommunityModel {
    public String group_id;
    public String group_icon;
    public String group_desc;
    public String group_name;
    public String created_by;
    public String date_created;
    public String checker;
    public String counter;

    public CommunityModel(String group_id, String group_icon, String group_desc, String group_name, String created_by, String date_created, String checker, String counter) {
        this.group_id = group_id;
        this.group_icon = group_icon;
        this.group_desc = group_desc;
        this.group_name = group_name;
        this.created_by = created_by;
        this.date_created = date_created;
        this.checker    = checker;
        this.counter    = counter;
    }



    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_icon() {
        return group_icon;
    }

    public void setGroup_icon(String group_icon) {
        this.group_icon = group_icon;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
