package xyz.esuku.startupingnitex.Model;

public class GrantCategoryModel {
    int grant_cat_id;
    String grant_cat_name;

    public GrantCategoryModel(int grant_cat_id, String grant_cat_name) {
        this.grant_cat_id = grant_cat_id;
        this.grant_cat_name = grant_cat_name;
    }

    public int getGrant_cat_id() {
        return grant_cat_id;
    }

    public void setGrant_cat_id(int grant_cat_id) {
        this.grant_cat_id = grant_cat_id;
    }

    public String getGrant_cat_name() {
        return grant_cat_name;
    }

    public void setGrant_cat_name(String grant_cat_name) {
        this.grant_cat_name = grant_cat_name;
    }
}
