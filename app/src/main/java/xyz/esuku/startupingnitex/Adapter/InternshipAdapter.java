package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.esuku.startupingnitex.ItemClicked.InternshipItemClickListener;
import xyz.esuku.startupingnitex.Model.InternshipModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.MyViewHolder> {
    Context context;
    List<InternshipModel> internshipModelList;
    InternshipItemClickListener internshipItemClickListener;

    public InternshipAdapter(Context context, List<InternshipModel> internshipModelList, InternshipItemClickListener internshipItemClickListener) {
        this.context = context;
        this.internshipModelList = internshipModelList;
        this.internshipItemClickListener = internshipItemClickListener;
    }

    @NonNull
    @Override
    public InternshipAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.internship_item_list,parent,false);
        return new InternshipAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternshipAdapter.MyViewHolder holder, int position) {

        InternshipModel model   = internshipModelList.get(position);
        holder.full_name.setText(internshipModelList.get(position).getFull_name());
        holder.user_name.setText(internshipModelList.get(position).getUser_name());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.start_up_logo)
                .error(R.drawable.start_up_logo);

        Glide.with(context).load(internshipModelList.get(position).getUser_img()).apply(options).into(holder.user_image);

        holder.view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internshipItemClickListener.onClick(model);
            }
        });



    }

    @Override
    public int getItemCount() {
        return internshipModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_image;
        TextView user_name;
        TextView full_name;
        AppCompatButton view_detail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_image  = itemView.findViewById(R.id.user_image);
            user_name  = itemView.findViewById(R.id.user_name);
            full_name  = itemView.findViewById(R.id.full_name);
            view_detail  = itemView.findViewById(R.id.view_detail);
        }
    }
}
