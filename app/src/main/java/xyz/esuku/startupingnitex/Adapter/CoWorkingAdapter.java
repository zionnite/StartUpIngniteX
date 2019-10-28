package xyz.esuku.startupingnitex.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import xyz.esuku.startupingnitex.ItemClicked.CoWorkingItemClickListener;
import xyz.esuku.startupingnitex.Model.CoWorkingModel;
import xyz.esuku.startupingnitex.R;

import java.util.List;

public class CoWorkingAdapter extends RecyclerView.Adapter<CoWorkingAdapter.MyViewHolder> {
    Context context;
    List<CoWorkingModel> workingModelList;
    CoWorkingItemClickListener workingItemClickListener;

    public CoWorkingAdapter(Context context, List<CoWorkingModel> workingModelList, CoWorkingItemClickListener workingItemClickListener) {
        this.context = context;
        this.workingModelList = workingModelList;
        this.workingItemClickListener = workingItemClickListener;
    }

    @NonNull
    @Override
    public CoWorkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.co_working_item,parent,false);
        return new CoWorkingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoWorkingAdapter.MyViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return workingModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView co_working_title,co_working_desc,co_working_address,co_working_phone,co_working_email,co_working_country_state_city,co_working_userprofile,
                co_working_user_name,co_working_full_name;

        AppCompatButton co_working_view_profile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            co_working_title    = itemView.findViewById(R.id.co_working_title);
            co_working_desc     = itemView.findViewById(R.id.co_working_desc);
            co_working_address     = itemView.findViewById(R.id.co_working_address);
            co_working_phone     = itemView.findViewById(R.id.co_working_phone);
            co_working_email     = itemView.findViewById(R.id.co_working_email);
            co_working_country_state_city     = itemView.findViewById(R.id.co_working_country_state_city);
            co_working_userprofile     = itemView.findViewById(R.id.co_working_userprofile);
            co_working_user_name     = itemView.findViewById(R.id.co_working_user_name);
            co_working_full_name     = itemView.findViewById(R.id.co_working_full_name);
            co_working_view_profile     = itemView.findViewById(R.id.co_working_view_profile);
        }
    }
}
