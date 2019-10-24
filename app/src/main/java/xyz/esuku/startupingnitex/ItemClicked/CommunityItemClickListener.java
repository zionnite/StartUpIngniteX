package xyz.esuku.startupingnitex.ItemClicked;

import xyz.esuku.startupingnitex.Model.CommunityModel;

public interface CommunityItemClickListener {

    void OnJoinToggle(CommunityModel model, int position);
    void OnClick(CommunityModel model);
}
