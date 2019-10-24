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
import xyz.esuku.startupingnitex.ItemClicked.BusinessListingListener;
import xyz.esuku.startupingnitex.Model.BusinessListingModel;
import xyz.esuku.startupingnitex.R;

import java.util.HashMap;
import java.util.List;

public class BusinessListingAdapter extends RecyclerView.Adapter<BusinessListingAdapter.MyViewHolder> {
    Context context;
    List<BusinessListingModel> businessListingModelList;
    BusinessListingListener businessListingListener;

    public BusinessListingAdapter(Context context, List<BusinessListingModel> businessListingModelList, BusinessListingListener businessListingListener) {
        this.context = context;
        this.businessListingModelList = businessListingModelList;
        this.businessListingListener = businessListingListener;
    }

    @NonNull
    @Override
    public BusinessListingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.business_list,parent,false);
        return new BusinessListingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessListingAdapter.MyViewHolder holder, int position) {


//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
//                .error(R.mipmap.ic_launcher_round);
//
//        Glide.with(context).load(user.get("user_image_name")).apply(options).into(nav_image_name);

        Glide.with(context).load(businessListingModelList.get(position).getLogo()).centerCrop().placeholder(R.drawable.start_up_logo).into(holder.bus_logo);
        //holder.bus_logo.setImageResource(Integer.parseInt(businessListingModelList.get(position).getLogo()));
        holder.bus_name.setText(businessListingModelList.get(position).getName());
        holder.bus_phone.setText(businessListingModelList.get(position).getPhone());
        holder.bus_email.setText(businessListingModelList.get(position).getEmail());
        holder.bus_address.setText(businessListingModelList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return businessListingModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView bus_logo;
        TextView bus_name,bus_phone,bus_email,bus_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bus_logo        = itemView.findViewById(R.id.bus_logo);
            bus_name        = itemView.findViewById(R.id.bus_name);
            bus_phone        = itemView.findViewById(R.id.bus_phone);
            bus_email        = itemView.findViewById(R.id.bus_email);
            bus_address        = itemView.findViewById(R.id.bus_address);
        }
    }
}
