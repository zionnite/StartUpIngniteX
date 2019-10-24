package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.esuku.startupingnitex.ItemClicked.ForumItemClickListener;
import xyz.esuku.startupingnitex.Model.ForumModel;
import xyz.esuku.startupingnitex.R;

import java.util.ArrayList;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder> {
    Context context;
    List<ForumModel> forumModelList;
    ForumItemClickListener forumItemClickListener;

    boolean checker;

    public ForumAdapter(Context context, List<ForumModel> forumModelList, ForumItemClickListener forumItemClickListener) {
        this.context = context;
        this.forumModelList = forumModelList;
        this.forumItemClickListener = forumItemClickListener;
    }

    @NonNull
    @Override
    public ForumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_list,parent,false);
        return new ForumAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumAdapter.MyViewHolder holder, int position) {

        checker = Boolean.parseBoolean(forumModelList.get(position).getQuestion_status());
        ForumModel forumModel   = forumModelList.get(position);

        holder.ask_content.setText(forumModelList.get(position).getContent());
        Glide.with(context).load(forumModelList.get(position).getUser_image()).centerCrop().placeholder(R.drawable.start_up_logo).into(holder.ask_user_image);
        String user_city    = forumModelList.get(position).getCity();
        String city =null;
        if(user_city.equals("")){
            holder.ask_user_city.setVisibility(View.GONE);
        }else{
            holder.ask_user_city.setText(forumModelList.get(position).getCity());
        }

        holder.ask_user_time.setText(forumModelList.get(position).getTime_ago());
        holder.ask_user_name.setText(forumModelList.get(position).getUser_name());
        holder.ask_counter.setText(forumModelList.get(position).getCounter()+ " Participating");

        int counter      = Integer.parseInt(forumModelList.get(position).getCounter());
        if(counter > 0){
            holder.ask_counter.setText(forumModelList.get(position).getCounter()+ " Participating");
        }else{
            holder.ask_counter.setText("Participating");
        }
        Glide.with(context).load(forumModelList.get(position).getLast_commenter()).centerCrop().placeholder(R.drawable.start_up_logo).into(holder.ask_last_commenter);

        if(checker){
            holder.answer_status.setText("Answered");
        }else if(!checker){
            holder.answer_status.setText("Not Answered");
        }


        holder.ask_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumItemClickListener.viewUser(forumModelList.get(position).getUser_name());
            }
        });
        holder.particpate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumItemClickListener.OnParticipate(forumModel, position);
            }
        });

        holder.answer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumItemClickListener.OnAnswer(forumModel,position);
            }
        });

        holder.forum_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forumItemClickListener.OnClicked(forumModel,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return forumModelList.size();
    }

    public void update_item(int position,String ask_id,String user_image, String user_name, String city, String time_ago, String content, String last_commenter, String question_status, String counter) {

        Log.d("position -1", String.valueOf(position));
        Log.d("counter -1", String.valueOf(counter));
        Log.d("arraySize -1", String.valueOf(getItemCount()));
        ForumModel model = forumModelList.get(position); //getting object from the list
        model.setLast_commenter(last_commenter);
        model.setCounter(counter);
        notifyItemChanged(position, model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ask_user_image;
        TextView ask_user_name, ask_user_city,ask_user_time,ask_content,answer_status,ask_counter;
        CircleImageView ask_last_commenter;
        LinearLayout particpate_btn,answer_btn;
        CardView forum_activity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ask_user_image      = itemView.findViewById(R.id.ask_user_image);
            ask_user_name      = itemView.findViewById(R.id.ask_user_name);
            ask_user_city      = itemView.findViewById(R.id.ask_user_city);
            ask_user_time      = itemView.findViewById(R.id.ask_user_time);
            ask_content      = itemView.findViewById(R.id.ask_content);
            answer_status      = itemView.findViewById(R.id.answer_status);
            ask_last_commenter      = itemView.findViewById(R.id.ask_last_commenter);
            particpate_btn      = itemView.findViewById(R.id.particpate_btn);
            answer_btn      = itemView.findViewById(R.id.answer_btn);
            forum_activity      = itemView.findViewById(R.id.forum_activity);
            ask_counter      = itemView.findViewById(R.id.ask_counter);



        }
    }
}
