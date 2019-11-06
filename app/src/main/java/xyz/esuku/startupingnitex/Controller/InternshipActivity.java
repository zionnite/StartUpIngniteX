package xyz.esuku.startupingnitex.Controller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.InternshipAdapter;
import xyz.esuku.startupingnitex.ItemClicked.InternshipItemClickListener;
import xyz.esuku.startupingnitex.Model.InternshipModel;
import xyz.esuku.startupingnitex.Model.JopPostingModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternshipActivity extends AppCompatActivity implements InternshipItemClickListener {

    TextView goBack;
    ImageView back_btn;
    RecyclerView internship_recyclerview;
    InternshipAdapter internshipAdapter;
    List<InternshipModel> internshipModelList = new ArrayList<>();

    AppCompatEditText search_box;
    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    Boolean searching = false;

    private UserDb_Helper userDb_helper;
    String user_name;

    Dialog prefDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intenship);

        myProgressDialog    = new MyProgressDialog(InternshipActivity.this);

        userDb_helper   = new UserDb_Helper(InternshipActivity.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");
        internship_recyclerview        = findViewById(R.id.internship_Recyclerview);

        goBack       = findViewById(R.id.goBack);
        back_btn       = findViewById(R.id.back_btn);
        search_box       = findViewById(R.id.search_box);

        internshipAdapter = new InternshipAdapter(getApplicationContext(), internshipModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        internship_recyclerview.setHasFixedSize(true);
        internship_recyclerview.setLayoutManager(linearLayoutManager);
        internship_recyclerview.setAdapter(internshipAdapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InternshipActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        search_box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search_term  = search_box.getText().toString().trim();
                    Log.d("Bus", "Action Search" + search_box.getText().toString().trim());
                    performSearch(search_term);
                    return true;
                }
                return false;
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInternship();
            }
        });

        getInternship();
    }

    private void performSearch(String search_term) {
        search_box.clearFocus();
        search_box.setText("");
        searching   = true;

        if(searching){
            goBack.setVisibility(View.VISIBLE);

        }
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(search_box.getWindowToken(), 0);
        //...perform search


        String requestUrl = appLinks.post_interns_search;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("search_term", search_term);
        postMap.put("user_name", user_name);
        myProgressDialog.setMessage("Searching ...");

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();


                    Log.e("BusSA",response.toString());
                    try {

                        List<InternshipModel> listingModels = new ArrayList<>();
                        JSONArray detail = response.getJSONArray("detail");
                        for (int i=0; i < detail.length(); i++) {

                            JSONObject detailObj = detail.getJSONObject(i);

                            String user_id        = detailObj.getString("user_id");
                            String user_name        = detailObj.getString("user_name");
                            String full_name     = detailObj.getString("full_name");
                            String phone_no     = detailObj.getString("phone");
                            String email     = detailObj.getString("email");
                            String user_img     = detailObj.getString("user_img");
                            String interest     = detailObj.getString("interest");
                            String need_mentor     = detailObj.getString("need_mentor");
                            String city     = detailObj.getString("city");

                            listingModels.add(new InternshipModel(user_id,user_name,full_name,phone_no,email,user_img,interest,need_mentor,city));
                        }



                        internshipModelList.clear();
                        internshipModelList.addAll(listingModels);

                        // refreshing recycler view
                        internshipAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("BusSB",e.toString());
                        e.printStackTrace();
                    }


                }, error -> {

            Log.e("BusSC",error.toString());
            myProgressDialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    private void getInternship() {
        searching =false;
        if (!searching){
            goBack.setVisibility(View.GONE);
        }

        myProgressDialog.setMessage("Please wait ...");
        String requestUrl = appLinks.get_interns;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        myProgressDialog.dismiss();
                        Log.d("BusinessListing",response.toString());
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<InternshipModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<InternshipModel>>() {
                        }.getType());



                        internshipModelList.clear();
                        internshipModelList.addAll(list);

                        // refreshing recycler view
                        internshipAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myProgressDialog.dismiss();
                // error in getting
                Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
                Log.e("BusinessListing",error.toString());

            }
        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }
    @Override
    public void onClick(InternshipModel model) {

        String d_user_id      = model.getUser_id();
        String d_user_name      = model.getUser_name();
        String d_full_name      = model.getFull_name();
        String d_phone          = model.getPhone_no();
        String d_email          = model.getEmail();
        String d_interest       = model.getInterest();
        String d_need_mentor    = model.getNeed_mentor();
        String d_city           = model.getCity();
        String d_user_image     = model.getUser_img();


        prefDialog  = new Dialog(InternshipActivity.this);
        prefDialog.setContentView(R.layout.modal_view_interns_details);
        prefDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        prefDialog.setCancelable(true);
        prefDialog.show();

        TextView t_user_name      = prefDialog.findViewById(R.id.user_name);
        TextView t_full_name      = prefDialog.findViewById(R.id.full_name);
        TextView t_city           = prefDialog.findViewById(R.id.city);
        TextView t_interest       = prefDialog.findViewById(R.id.interest);
        TextView t_email          = prefDialog.findViewById(R.id.email);
        TextView t_phone          = prefDialog.findViewById(R.id.phone);
        CircleImageView t_user_image          = prefDialog.findViewById(R.id.user_image);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.start_up_logo)
                .error(R.drawable.start_up_logo);

        Glide.with(getApplicationContext()).load(d_user_image).apply(options).into(t_user_image);
        t_user_name.setText(d_user_name);
        t_full_name.setText(d_full_name);
        t_city.setText(d_city);
        t_phone.setText(d_phone);
        t_email.setText(d_email);
        t_interest.setText(d_interest);


    }
}
