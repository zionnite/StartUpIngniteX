package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        View view = LayoutInflater.from(context).inflate(R.layout.grant_list,parent,false);
        return new InternshipAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternshipAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return internshipModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
