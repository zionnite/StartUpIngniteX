package xyz.esuku.startupingnitex.Model;

public class GroupPostModel {

    String post_id;
    String post_content;
    String post_time;
    String post_by;
    String post_like_counter;
    String post_like_checker;
    String is_text;
    String post_content_image;
    String poster_img;


    public GroupPostModel(String post_id, String post_content, String post_time, String post_by, String post_like_counter, String post_like_checker, String is_text, String post_content_image,String poster_img) {
        this.post_id = post_id;
        this.post_content = post_content;
        this.post_time = post_time;
        this.post_by = post_by;
        this.post_like_counter = post_like_counter;
        this.post_like_checker = post_like_checker;
        this.is_text = is_text;
        this.post_content_image = post_content_image;
        this.poster_img = poster_img;
    }

    public GroupPostModel() {
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_by() {
        return post_by;
    }

    public void setPost_by(String post_by) {
        this.post_by = post_by;
    }

    public String getPost_like_counter() {
        return post_like_counter;
    }

    public void setPost_like_counter(String post_like_counter) {
        this.post_like_counter = post_like_counter;
    }

    public String getPost_like_checker() {
        return post_like_checker;
    }

    public void setPost_like_checker(String post_like_checker) {
        this.post_like_checker = post_like_checker;
    }

    public String getIs_text() {
        return is_text;
    }

    public void setIs_text(String is_text) {
        this.is_text = is_text;
    }

    public String getPost_content_image() {
        return post_content_image;
    }

    public void setPost_content_image(String post_content_image) {
        this.post_content_image = post_content_image;
    }

    public String getPoster_img() {
        return poster_img;
    }

    public void setPoster_img(String poster_img) {
        this.poster_img = poster_img;
    }
}
