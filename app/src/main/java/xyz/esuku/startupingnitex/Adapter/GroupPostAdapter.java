package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.esuku.startupingnitex.ItemClicked.GroupPOstItemClickListener;
import xyz.esuku.startupingnitex.Model.GroupPostModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class GroupPostAdapter extends RecyclerView.Adapter<GroupPostAdapter.MyViewHolder> {

    Context context;
    List<GroupPostModel> groupPostModelList;
    GroupPOstItemClickListener groupPOstItemClickListener;

    public GroupPostAdapter(Context context, List<GroupPostModel> groupPostModelList, GroupPOstItemClickListener groupPOstItemClickListener) {
        this.context = context;
        this.groupPostModelList = groupPostModelList;
        this.groupPOstItemClickListener = groupPOstItemClickListener;
    }

    @NonNull
    @Override
    public GroupPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.group_post_list,parent,false);
        return new GroupPostAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupPostAdapter.MyViewHolder holder, int position) {

        String content_checker      = groupPostModelList.get(position).getIs_text();
        int counter      = Integer.parseInt(groupPostModelList.get(position).getPost_like_counter());

        holder.post_author.setText(groupPostModelList.get(position).getPost_by());
        holder.post_time_ago.setText(groupPostModelList.get(position).getPost_time());
        holder.post_content_text.setText(groupPostModelList.get(position).getPost_content());

        if(counter > 0) {
            holder.counter.setVisibility(View.VISIBLE);
            holder.counter.setText(groupPostModelList.get(position).getPost_like_counter() + " Like");
        }else{
            holder.counter.setVisibility(View.GONE);
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.content_loader_placeholder)
                .error(R.drawable.content_loader_placeholder);

        Glide.with(context).load(groupPostModelList.get(position).getPoster_img()).apply(options).into(holder.post_user_image);

        if(content_checker.equals("true")){
            holder.post_content_text.setText(groupPostModelList.get(position).getPost_content());
        }
        else if(content_checker.equals("false")){
            //show image
            holder.post_content_text.setText(groupPostModelList.get(position).getPost_content());

            holder.post_content_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(groupPostModelList.get(position).getPost_content_image()).apply(options).into(holder.post_content_image);

        }

        holder.post_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_id      = groupPostModelList.get(position).getPost_id();
                groupPOstItemClickListener.onLikeToggle(post_id,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupPostModelList.size();
    }

    public void upldate_item(int selected_position, String checker, String counter) {

        GroupPostModel model = groupPostModelList.get(selected_position); //getting object from the list

        model.setPost_like_checker(checker);
        model.setPost_like_counter(counter);

        notifyItemChanged(selected_position, model);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView post_user_image;
        TextView post_author;
        TextView post_time_ago;
        TextView post_content_text;
        ImageView post_content_image;
        ImageView post_like;
        TextView counter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            post_user_image         = itemView.findViewById(R.id.post_user_image);
            post_author         = itemView.findViewById(R.id.post_author);
            post_time_ago         = itemView.findViewById(R.id.post_time_ago);
            post_content_text         = itemView.findViewById(R.id.post_content_text);
            post_content_image         = itemView.findViewById(R.id.post_content_image);
            post_like         = itemView.findViewById(R.id.post_like);
            counter         = itemView.findViewById(R.id.counter);
        }
    }
}
