package xyz.esuku.startupingnitex.Model;

public class GrantModel {

    String grant_id;
    String grant_name;
    String grant_desc;
    String grant_img;

    public GrantModel(String grant_id, String grant_name, String grant_desc, String grant_img) {
        this.grant_id = grant_id;
        this.grant_name = grant_name;
        this.grant_desc = grant_desc;
        this.grant_img = grant_img;
    }

    public String getGrant_id() {
        return grant_id;
    }

    public void setGrant_id(String grant_id) {
        this.grant_id = grant_id;
    }

    public String getGrant_name() {
        return grant_name;
    }

    public void setGrant_name(String grant_name) {
        this.grant_name = grant_name;
    }

    public String getGrant_desc() {
        return grant_desc;
    }

    public void setGrant_desc(String grant_desc) {
        this.grant_desc = grant_desc;
    }

    public String getGrant_img() {
        return grant_img;
    }

    public void setGrant_img(String grant_img) {
        this.grant_img = grant_img;
    }
}
