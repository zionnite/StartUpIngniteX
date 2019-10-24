package xyz.esuku.startupingnitex.ItemClicked;

import xyz.esuku.startupingnitex.Model.ForumModel;

public interface ForumItemClickListener {
    void OnClicked(ForumModel forumModel, int position);
    void OnParticipate(ForumModel forumModel, int item_id);
    void OnAnswer(ForumModel forumModel, int position);
    void viewUser(String user_name);
}
