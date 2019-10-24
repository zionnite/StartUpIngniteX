package xyz.esuku.startupingnitex.ItemClicked;

import xyz.esuku.startupingnitex.Model.EventModel;

public interface EventItemClickListener {
    void OnItemClicked(EventModel eventModel);
    void OnBook(EventModel eventModel, int select_item);
    void OnUnbook(EventModel eventModel);
}
