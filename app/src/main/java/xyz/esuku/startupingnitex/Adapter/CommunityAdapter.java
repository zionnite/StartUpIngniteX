package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.esuku.startupingnitex.ItemClicked.CommunityItemClickListener;
import xyz.esuku.startupingnitex.Model.CommunityModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {
    Context context;
    List<CommunityModel> communityModelList;
    CommunityItemClickListener communityItemClickListener;

    public CommunityAdapter(Context context, List<CommunityModel> communityModelList, CommunityItemClickListener communityItemClickListener) {
        this.context = context;
        this.communityModelList = communityModelList;
        this.communityItemClickListener = communityItemClickListener;
    }

    @NonNull
    @Override
    public CommunityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_group_list,parent,false);
        return new CommunityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.MyViewHolder holder, int position) {

        CommunityModel  model   = communityModelList.get(position);
        String checker  = communityModelList.get(position).getChecker();
        holder.com_title.setText(communityModelList.get(position).getGroup_name());
        holder.com_author.setText(communityModelList.get(position).getCreated_by());
        holder.com_time_ago.setText(communityModelList.get(position).getDate_created());
        holder.com_content.setText(communityModelList.get(position).getGroup_desc());
        holder.counter.setText(communityModelList.get(position).getCounter());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.content_loader_placeholder)
                .error(R.drawable.content_loader_placeholder);

        Glide.with(context).load(communityModelList.get(position).getGroup_icon()).apply(options).into(holder.logo_image);

//        holder.community_unjoin_btn.setVisibility(View.GONE);
//        holder.community_join_btn.setVisibility(View.GONE);

        if(checker.equals("true")){
            holder.community_unjoin_btn.setVisibility(View.VISIBLE);
            holder.community_join_btn.setVisibility(View.GONE);
        }
        else if(checker.equals("false")){
            holder.community_join_btn.setVisibility(View.VISIBLE);
            holder.community_unjoin_btn.setVisibility(View.GONE);
        }

        holder.community_join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityItemClickListener.OnJoinToggle(model,position);
            }
        });

        holder.community_unjoin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communityItemClickListener.OnJoinToggle(model,position);
            }
        });

        holder.group_CardView.setOnClickListener(v ->{
            communityItemClickListener.OnClick(model);
        });

    }

    @Override
    public int getItemCount() {
        return communityModelList.size();
    }

    public void update_item(int position, String group_id, String counter, String checker) {
        CommunityModel model = communityModelList.get(position); //getting object from the list
        model.setCounter(counter);
        model.setChecker(checker);
        notifyItemChanged(position, model);

        Log.e("checker",model.getChecker());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView logo_image;
        TextView com_title,com_author,com_time_ago,com_content,counter;
        AppCompatButton community_join_btn,community_unjoin_btn;
        CardView group_CardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            logo_image  = itemView.findViewById(R.id.logo_image);
            com_title  = itemView.findViewById(R.id.com_title);
            com_author  = itemView.findViewById(R.id.com_author);
            com_time_ago  = itemView.findViewById(R.id.com_time_ago);
            com_content  = itemView.findViewById(R.id.com_content);
            community_join_btn  = itemView.findViewById(R.id.community_join_btn);
            community_unjoin_btn  = itemView.findViewById(R.id.community_unjoin_btn);
            counter  = itemView.findViewById(R.id.counter);
            group_CardView  = itemView.findViewById(R.id.group_CardView);
        }
    }
}
