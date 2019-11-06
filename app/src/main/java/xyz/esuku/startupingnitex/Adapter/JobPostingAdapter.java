package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import xyz.esuku.startupingnitex.ItemClicked.JopPostingItemClickListener;
import xyz.esuku.startupingnitex.Model.JopPostingModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class JobPostingAdapter extends RecyclerView.Adapter<JobPostingAdapter.MyViewHolder> {
    Context context;
    List<JopPostingModel> jopPostingModelList;
    JopPostingItemClickListener jopPostingItemClickListener;

    public JobPostingAdapter(Context context, List<JopPostingModel> jopPostingModelList, JopPostingItemClickListener jopPostingItemClickListener) {
        this.context = context;
        this.jopPostingModelList = jopPostingModelList;
        this.jopPostingItemClickListener = jopPostingItemClickListener;
    }

    @NonNull
    @Override
    public JobPostingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jop_posting_items,parent,false);
        return new JobPostingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPostingAdapter.MyViewHolder holder, int position) {

        JopPostingModel model   = jopPostingModelList.get(position);
        String job_status   =model.getJob_status();
        holder.jop_posting_title.setText(jopPostingModelList.get(position).getJob_title());
        holder.jop_posting_desc.setText(jopPostingModelList.get(position).getJob_desc());

        if(job_status.equals("true")){
            holder.apply_btn.setBackgroundResource(R.color.colorGray);
        }
        holder.apply_btn.setOnClickListener(v -> jopPostingItemClickListener.onClick(model));
    }

    @Override
    public int getItemCount() {
        return jopPostingModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView jop_posting_title,jop_posting_desc;
        AppCompatButton apply_btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            jop_posting_title       = itemView.findViewById(R.id.jop_posting_title);
            jop_posting_desc       = itemView.findViewById(R.id.jop_posting_desc);
            apply_btn       = itemView.findViewById(R.id.apply_btn);
        }
    }
}
