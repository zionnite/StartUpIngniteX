package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import xyz.esuku.startupingnitex.ItemClicked.GrantItemClickListener;
import xyz.esuku.startupingnitex.Model.GrantModel;
import xyz.esuku.startupingnitex.R;

import java.util.ArrayList;
import java.util.List;

public class GrantAdapter extends RecyclerView.Adapter<GrantAdapter.MyViewHolder> {
    Context context;
    List<GrantModel> grantModelList;
    GrantItemClickListener grantItemClickListener;

    public GrantAdapter(Context context, List<GrantModel> grantModelList, GrantItemClickListener grantItemClickListener) {
        this.context = context;
        this.grantModelList = grantModelList;
        this.grantItemClickListener = grantItemClickListener;
    }

    @NonNull
    @Override
    public GrantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grant_list,parent,false);
        return new GrantAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrantAdapter.MyViewHolder holder, int position) {
        GrantModel grantModel   = grantModelList.get(position);
        holder.grant_topic.setText(grantModelList.get(position).getGrant_name());
        holder.grant_content.setText(grantModelList.get(position).getGrant_desc());
        //Glide.with(context).load(grantModelList.get(position).getGrant_img()).centerCrop().placeholder(R.drawable.start_up_logo).into(holder.grant_image);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.start_up_logo)
                .error(R.drawable.start_up_logo);

        Glide.with(context).load(grantModelList.get(position).getGrant_img()).apply(options).into(holder.grant_image);

        holder.grant_read_more_btn.setOnClickListener(v -> {

            grantItemClickListener.onItemClicked(grantModel,position);
        });

    }

    @Override
    public int getItemCount() {
        return grantModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView grant_image;
        TextView grant_topic, grant_content;
        Button grant_read_more_btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            grant_topic         = itemView.findViewById(R.id.grant_topic);
            grant_content         = itemView.findViewById(R.id.grant_content);
            grant_image         = itemView.findViewById(R.id.grant_image);
            grant_read_more_btn         = itemView.findViewById(R.id.grant_read_more_btn);



        }
    }
}
