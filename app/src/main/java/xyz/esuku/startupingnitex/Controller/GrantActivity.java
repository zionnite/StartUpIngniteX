package xyz.esuku.startupingnitex.Controller;

import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.GrantAdapter;
import xyz.esuku.startupingnitex.ItemClicked.GrantItemClickListener;
import xyz.esuku.startupingnitex.Model.GrantCategoryModel;
import xyz.esuku.startupingnitex.Model.GrantModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class GrantActivity extends AppCompatActivity implements GrantItemClickListener {

    NiceSpinner niceSpinner;
    List<String> dataset;

    ImageView back_btn,grant_image;
    TextView grant_topic,grant_content,selected_grant_title;

    RecyclerView grant_recyclerView;
    GrantAdapter grantAdapter;
    List<GrantModel> grantModelList = new ArrayList<>();

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    boolean grant_selector =false;

    List<GrantCategoryModel> grantCategoryModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant);

        back_btn    =findViewById(R.id.back_btn);
        grant_image    =findViewById(R.id.grant_image);
        grant_topic    =findViewById(R.id.grant_topic);
        grant_content    =findViewById(R.id.grant_content);
        selected_grant_title    =findViewById(R.id.selected_grant_title);
        grant_recyclerView    =findViewById(R.id.grant_recyclerView);


        myProgressDialog    = new MyProgressDialog(GrantActivity.this);
        userDbHelper = new UserDb_Helper(GrantActivity.this);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(GrantActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        niceSpinner = (NiceSpinner) findViewById(R.id.grant_spinner);
        //dataset = new LinkedList<>(Arrays.asList("Select Grant Type","One", "Two", "Three", "Four", "Five is greate than the selected fiveFive is greate than the selected fiveFive is greate than the selected five"));
        //niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            grant_selector  = true;

            String item = String.valueOf(parent.getItemAtPosition(position));
            //Toast.makeText(GrantActivity.this, item, Toast.LENGTH_SHORT).show();
            Log.d("Grant ID", String.valueOf(id)+" POSITION "+String.valueOf(position)+ "NAME "+item);

            if(grant_selector && position !=0){
                selected_grant_title.setText(item);
                getGrantByCategory(item);
            }
            if(position ==0){
                selected_grant_title.setText(item);
                grant_selector  = false;
                getGrant();
            }



        });

        grantAdapter = new GrantAdapter(getApplicationContext(), grantModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        grant_recyclerView.setHasFixedSize(true);
        grant_recyclerView.setLayoutManager(linearLayoutManager);
        grant_recyclerView.setAdapter(grantAdapter);

        if(!grant_selector){
            selected_grant_title.setText("All Grant Type");
        }

        getGrant();
        getGrantCategory();
    }

    private void getGrantByCategory(String item) {
        String requestUrl = appLinks.get_grant_by_category;



        myProgressDialog.setMessage("Please wait ...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {


            myProgressDialog.dismiss();



            List<GrantModel> listingModels = new ArrayList<>();

            try {


                if(response.contains("status")){
                    JSONObject  jsonObject  = new JSONObject(response);
                    String status   = jsonObject.getString("status");
                    String msg   = jsonObject.getString("msg");

                    Toast.makeText(GrantActivity.this, msg, Toast.LENGTH_LONG).show();

                    grantModelList.clear();
                    grantAdapter.notifyDataSetChanged();
                }else{
                    JSONArray   jsonArray   = new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONObject  dataObj     = jsonArray.getJSONObject(i);
                        Log.e("Grant", String.valueOf(i));

                        String grant_id         = dataObj.getString("grant_id");
                        String grant_name         = dataObj.getString("grant_name");
                        String grant_desc         = dataObj.getString("grant_desc");
                        String grant_img         = dataObj.getString("grant_img");

                        listingModels.add(new GrantModel(grant_id,grant_name,grant_desc,grant_img));
                    }



                    grantModelList.clear();
                    grantModelList.addAll(listingModels);
                    // refreshing recycler view
                    grantAdapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("grant_cat_name", item);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void getGrant() {

        myProgressDialog.setMessage("Please wait ...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_grant;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.e("getEvent",response.toString());
                    List<GrantModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<GrantModel>>() {
                    }.getType());



                    grantModelList.clear();
                    grantModelList.addAll(list);

                    // refreshing recycler view
                    grantAdapter.notifyDataSetChanged();

                }, error -> {
                    // error in getting
                    Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
                    myProgressDialog.dismiss();

                });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }
    private void getGrantCategory() {

        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_grant_category;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="All Grant Type";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int grant_cat_id        = detailObj.getInt("grant_cat_id");
                            String grant_cat_name        = detailObj.getString("grant_cat_name");
                            arrayList.add(grant_cat_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset = new LinkedList<>(arrayList);
                    niceSpinner.attachDataSource(dataset);

                }, error -> {
            // error in getting
            Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
            Log.e("BusinessListing",error.toString());

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onItemClicked(GrantModel grantModel, int position) {
        Intent intent = new Intent(GrantActivity.this, GrantDetailActivity.class);
        intent.putExtra("grant_id",grantModel.getGrant_id());
        intent.putExtra("grant_name",grantModel.getGrant_name());
        intent.putExtra("grant_desc",grantModel.getGrant_desc());
        intent.putExtra("grant_img",grantModel.getGrant_img());
        intent.putExtra("position",position);
        startActivity(intent);
    }
}
