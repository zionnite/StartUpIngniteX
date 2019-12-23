package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.esuku.startupingnitex.ItemClicked.ForumDetailItemListener;
import xyz.esuku.startupingnitex.Model.ForumDetailModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class ForumDetailAdapter extends RecyclerView.Adapter<ForumDetailAdapter.MyViewHolder> {
    Context context;
    List<ForumDetailModel> forumDetailModelList;
    ForumDetailItemListener forumDetailItemListener;

    public ForumDetailAdapter(Context context, List<ForumDetailModel> forumDetailModelList, ForumDetailItemListener forumDetailItemListener) {
        this.context = context;
        this.forumDetailModelList = forumDetailModelList;
        this.forumDetailItemListener    = forumDetailItemListener;
    }


    @NonNull
    @Override
    public ForumDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forum_detail_list,parent,false);
        return new ForumDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumDetailAdapter.MyViewHolder holder, int position) {

        Glide.with(context).load(forumDetailModelList.get(position).getUser_image()).centerCrop().placeholder(R.drawable.start_up_logo).into(holder.ask_detail_user_image);
        holder.ask_detail_user_name.setText(forumDetailModelList.get(position).user_name);
        String user_city    = forumDetailModelList.get(position).getCity();
        if(user_city.equals("") || user_city ==null){
            holder.ask_detail_city.setVisibility(View.GONE);
        }else{
            holder.ask_detail_city.setText(forumDetailModelList.get(position).getCity());
        }
        holder.ask_detail_time_ago.setText(forumDetailModelList.get(position).getTime_ago());

        String content_checker  = forumDetailModelList.get(position).getIsText();

        if(content_checker.equals("true")){
            holder.ask_detail_content.setText(forumDetailModelList.get(position).getContent());
        }
        else if(content_checker.equals("false")){
            //show image
            holder.ask_detail_content.setText(forumDetailModelList.get(position).getContent());
            holder.ask_detail_content_image.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.content_loader_placeholder)
                    .error(R.drawable.content_loader_placeholder);
            Glide.with(context).load(forumDetailModelList.get(position).getA_content_img()).apply(options).into(holder.ask_detail_content_image);


            holder.ask_detail_user_image.setOnClickListener(v -> {


                forumDetailItemListener.viewUser(forumDetailModelList.get(position).getUser_name());
            });

        }

    }

    @Override
    public int getItemCount() {
        return forumDetailModelList.size();
    }

    public void update_item(int position, int ask_id, String ask_user_image, String ask_user_name, String ask_city, String ask_time, String ask_content, String isText, String counter) {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ask_detail_user_image;
        TextView ask_detail_user_name,ask_detail_city,ask_detail_time_ago,ask_detail_content;
        ImageView ask_detail_content_image;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ask_detail_user_image       = itemView.findViewById(R.id.ask_detail_user_image);
            ask_detail_user_name       = itemView.findViewById(R.id.ask_detail_user_name);
            ask_detail_city       = itemView.findViewById(R.id.ask_detail_city);
            ask_detail_time_ago       = itemView.findViewById(R.id.ask_detail_time_ago);
            ask_detail_content       = itemView.findViewById(R.id.ask_detail_content);
            ask_detail_content_image       = itemView.findViewById(R.id.ask_detail_content_image);
            ask_detail_content.setLinksClickable(true);
            ask_detail_content.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
