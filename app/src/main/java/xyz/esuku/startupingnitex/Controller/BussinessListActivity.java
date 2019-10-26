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
import xyz.esuku.startupingnitex.Adapter.BusinessListingAdapter;
import xyz.esuku.startupingnitex.ItemClicked.BusinessListingListener;
import xyz.esuku.startupingnitex.Model.BusinessListingModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BussinessListActivity extends AppCompatActivity implements BusinessListingListener {

    TextView goBack;
    ImageView back_btn,search_btn;
    RecyclerView business_recyclerView;
    BusinessListingAdapter businessListingAdapter;
    List<BusinessListingModel> businessListingModelList = new ArrayList<>();


    AppCompatEditText search_box;
    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    Boolean searching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_list);
        myProgressDialog    = new MyProgressDialog(BussinessListActivity.this);

        back_btn       = findViewById(R.id.back_btn);
        //search_btn       = findViewById(R.id.search_btn);
        goBack           = findViewById(R.id.goBack);
        search_box       = findViewById(R.id.search_box);
        business_recyclerView       = findViewById(R.id.business_RecyclerView);


//        businessListingModelList.add(new BusinessListingModel(R.drawable.user_profile,"zionnite","Benin City","5mins Ago","content goes here"));
//        businessListingModelList.add(new BusinessListingModel(R.drawable.user_profile,"zionnite","Benin City","5mins Ago","content goes here"));
//        businessListingModelList.add(new BusinessListingModel(R.drawable.user_profile,"zionnite","Benin City","5mins Ago","content goes here"));


        businessListingAdapter = new BusinessListingAdapter(getApplicationContext(), businessListingModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        business_recyclerView.setHasFixedSize(true);
        business_recyclerView.setLayoutManager(linearLayoutManager);
        business_recyclerView.setAdapter(businessListingAdapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BussinessListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Search Button Clicked", Toast.LENGTH_LONG).show();
//            }
//        });


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
                getBusinessList();
            }
        });
        getBusinessList();


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


        String requestUrl = appLinks.search_business;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("search_term", search_term);
        myProgressDialog.setMessage("Searching ...");

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();


                    Log.e("BusSA",response.toString());
                    try {

                        List<BusinessListingModel> listingModels = new ArrayList<>();
                        JSONArray detail = response.getJSONArray("detail");
                        for (int i=0; i < detail.length(); i++) {

                            JSONObject detailObj = detail.getJSONObject(i);

                            String bus_logo     = detailObj.getString("bus_logo");
                            String bus_name     = detailObj.getString("bus_name");
                            String bus_phone     = detailObj.getString("bus_phone");
                            String bus_email     = detailObj.getString("bus_email");
                            String bus_address     = detailObj.getString("bus_address");

                            listingModels.add(new BusinessListingModel(bus_logo,bus_name,bus_phone,bus_email,bus_address));
                        }



                        businessListingModelList.clear();
                        businessListingModelList.addAll(listingModels);

                        // refreshing recycler view
                        businessListingAdapter.notifyDataSetChanged();

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

    private void getBusinessList() {
        searching =false;
        if (!searching){
            goBack.setVisibility(View.GONE);
        }
        String requestUrl = appLinks.get_business;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("BusinessListing",response.toString());
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<BusinessListingModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<BusinessListingModel>>() {
                        }.getType());



                        businessListingModelList.clear();
                        businessListingModelList.addAll(list);

                        // refreshing recycler view
                        businessListingAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
    public void OnItemClick(BusinessListingModel businessListingModel) {
        //Start GoogleMapNavigation
    }


}
