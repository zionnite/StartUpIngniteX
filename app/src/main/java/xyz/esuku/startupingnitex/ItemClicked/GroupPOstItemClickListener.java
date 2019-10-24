package xyz.esuku.startupingnitex.ItemClicked;

import xyz.esuku.startupingnitex.Model.GroupPostModel;

public interface GroupPOstItemClickListener {
    void onclick(GroupPostModel model);
    void onLikeToggle(String post_id,int selected_position);
}
