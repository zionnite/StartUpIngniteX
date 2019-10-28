package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.esuku.startupingnitex.ItemClicked.CoWorkingItemClickListener;
import xyz.esuku.startupingnitex.Model.CoWorkingModel;

import java.util.List;

public class CoWorkingAdapter extends RecyclerView.Adapter<CoWorkingAdapter.MyViewHolder> {
    Context context;
    List<CoWorkingModel> workingModelList;
    CoWorkingItemClickListener workingItemClickListener;
    @NonNull
    @Override
    public CoWorkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CoWorkingAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return workingModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
