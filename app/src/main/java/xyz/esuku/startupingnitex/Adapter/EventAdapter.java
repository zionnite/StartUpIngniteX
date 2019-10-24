package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.esuku.startupingnitex.ItemClicked.EventItemClickListener;
import xyz.esuku.startupingnitex.Model.EventModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    Context context;
    List<EventModel> eventModelList;
    EventItemClickListener eventItemClickListener;
    String event_checker;
    boolean myChecker;

    public EventAdapter(Context context, List<EventModel> eventModelList, EventItemClickListener eventItemClickListener) {
        this.context = context;
        this.eventModelList = eventModelList;
        this.eventItemClickListener = eventItemClickListener;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_list,parent,false);
        return new EventAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, int position) {

        EventModel  model   = eventModelList.get(position);


        event_checker = eventModelList.get(position).getEvent_checker();
        holder.event_title.setText(eventModelList.get(position).getEvent_title());
        holder.event_date.setText(eventModelList.get(position).getEvent_date());
        holder.event_time.setText(eventModelList.get(position).getEvent_time());
        holder.event_desc.setText(eventModelList.get(position).getEvent_desc());
        holder.event_counter.setText(eventModelList.get(position).getEvent_counter()+" Booking");
        holder.event_title.setText(eventModelList.get(position).getEvent_title());
        //holder.book_event.setText(eventModelList.get(position).getEvent_checker());

        Log.e("event_checker", String.valueOf(event_checker)+" -"+ eventModelList.get(position).getId());
//        if(event_checker.equals("booked")){
//            myChecker = true;
//        }
//        else if(event_checker.equals("unbooked")/* Book Now*/){
//            myChecker = false;
//        }

        Log.e("Adapter", "Called");





        holder.event_like.setOnClickListener(v -> {
            eventItemClickListener.OnBook(model, position);
        });


    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }



    public void update_item(int position, String event_checker, String event_counter) {

        EventModel model = eventModelList.get(position); //getting object from the list
        model.setEvent_checker(event_checker);
        model.setEvent_counter(event_counter);
        notifyItemChanged(position, model);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView event_like;
        TextView event_title,event_date,event_time,event_desc,event_counter,book_event,unbook_event;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            event_title     = itemView.findViewById(R.id.event_title);
            event_date     = itemView.findViewById(R.id.event_date);
            event_time     = itemView.findViewById(R.id.event_time);
            event_desc     = itemView.findViewById(R.id.event_desc);
            event_counter     = itemView.findViewById(R.id.event_counter);
            book_event     = itemView.findViewById(R.id.book_event);

            event_like     = itemView.findViewById(R.id.event_like);



            event_like.setOnClickListener(v -> {
                //Toast.makeText(context,"Like clicked",Toast.LENGTH_LONG).show();
            });
        }
    }
}
