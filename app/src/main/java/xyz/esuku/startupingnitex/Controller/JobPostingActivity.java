package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.JobPostingAdapter;
import xyz.esuku.startupingnitex.ItemClicked.JopPostingItemClickListener;
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

public class JobPostingActivity extends AppCompatActivity implements JopPostingItemClickListener {

    TextView goBack;
    ImageView back_btn,add_job;
    RecyclerView job_recyclerView;
    JobPostingAdapter jobPostingAdapter;
    List<JopPostingModel> jopPostingModelList = new ArrayList<>();

    AppCompatEditText search_box;
    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    Boolean searching = false;

    private UserDb_Helper userDb_helper;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_job);
        myProgressDialog    = new MyProgressDialog(JobPostingActivity.this);

        userDb_helper   = new UserDb_Helper(JobPostingActivity.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");
        job_recyclerView        = findViewById(R.id.jopPosting_Recyclerview);



        goBack       = findViewById(R.id.goBack);
        back_btn       = findViewById(R.id.back_btn);
        search_box       = findViewById(R.id.search_box);
        add_job       = findViewById(R.id.add_job);

        jobPostingAdapter = new JobPostingAdapter(getApplicationContext(), jopPostingModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        job_recyclerView.setHasFixedSize(true);
        job_recyclerView.setLayoutManager(linearLayoutManager);
        job_recyclerView.setAdapter(jobPostingAdapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobPostingActivity.this, MainActivity.class);
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
                getJobListing();
            }
        });

        add_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobPostingActivity.this, JobPostingAddJop.class);
                startActivity(intent);
            }
        });
        getJobListing();
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


        String requestUrl = appLinks.search_job_listing;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("search_term", search_term);
        postMap.put("user_name", user_name);
        myProgressDialog.setMessage("Searching ...");

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();


                    Log.e("BusSA",response.toString());
                    try {

                        List<JopPostingModel> listingModels = new ArrayList<>();
                        JSONArray detail = response.getJSONArray("detail");
                        for (int i=0; i < detail.length(); i++) {

                            JSONObject detailObj = detail.getJSONObject(i);

                            String job_id        = detailObj.getString("id");
                            String job_poster        = detailObj.getString("job_poster");
                            String job_title     = detailObj.getString("job_title");
                            String job_desc     = detailObj.getString("job_desc");
                            String job_company     = detailObj.getString("job_company");
                            String job_location     = detailObj.getString("job_location");
                            String job_remotely     = detailObj.getString("job_remote");
                            String job_category     = detailObj.getString("job_category");
                            String job_experience     = detailObj.getString("job_experience");
                            String job_compensation     = detailObj.getString("job_compensation");
                            String job_time     = detailObj.getString("time");
                            String job_date     = detailObj.getString("date");
                            String job_status     = detailObj.getString("job_status");

                            listingModels.add(new JopPostingModel(job_id,job_poster,job_title,job_desc,job_company,job_location,job_remotely,job_category,job_experience,
                                    job_compensation,job_time,job_date,job_status));
                        }



                        jopPostingModelList.clear();
                        jopPostingModelList.addAll(listingModels);

                        // refreshing recycler view
                        jobPostingAdapter.notifyDataSetChanged();

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

    private void getJobListing() {
        searching =false;
        if (!searching){
            goBack.setVisibility(View.GONE);
        }

        myProgressDialog.setMessage("Please wait ...");
        String requestUrl = appLinks.get_jop_listing+"/"+user_name;
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

                        List<JopPostingModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<JopPostingModel>>() {
                        }.getType());



                        jopPostingModelList.clear();
                        jopPostingModelList.addAll(list);

                        // refreshing recycler view
                        jobPostingAdapter.notifyDataSetChanged();

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
    public void onClick(JopPostingModel model) {

        Intent intent = new Intent(JobPostingActivity.this, JobPostingDetail.class);
        intent.putExtra("job_poster",model.getJob_poster());
        intent.putExtra("job_id",model.getId());
        intent.putExtra("job_title",model.getJob_title());
        intent.putExtra("job_comp",model.getJob_company());
        intent.putExtra("job_desc",model.getJob_desc());
        intent.putExtra("job_cat",model.getJob_category());
        intent.putExtra("job_compensate",model.getJob_compensation());
        intent.putExtra("job_exp",model.getJob_experience());
        intent.putExtra("job_remote",model.getJob_remote());
        intent.putExtra("job_location",model.getJob_location());
        intent.putExtra("job_status",model.getJob_status());
        startActivity(intent);
    }
}
