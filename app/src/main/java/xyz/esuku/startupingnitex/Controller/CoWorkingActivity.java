package xyz.esuku.startupingnitex.Controller;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.CoWorkingAdapter;
import xyz.esuku.startupingnitex.ItemClicked.CoWorkingItemClickListener;
import xyz.esuku.startupingnitex.Model.CoWorkingModel;
import xyz.esuku.startupingnitex.Model.GrantModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class CoWorkingActivity extends AppCompatActivity implements CoWorkingItemClickListener {

    NiceSpinner select_country,select_state,select_category;
    List<String> dataset_1,dataset_2,dataset_3;
    TextView selected_category;
    String selected_country_name,selected_state_name,selected_category_name;
    boolean selector_checker =false;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    RecyclerView coWorking_recyclerView;
    CoWorkingAdapter coWorkingAdapter;
    List<CoWorkingModel> coWorkingModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_working);

        myProgressDialog    = new MyProgressDialog(CoWorkingActivity.this);
        userDbHelper = new UserDb_Helper(CoWorkingActivity.this);


        select_country  =findViewById(R.id.select_country);
        select_state  =findViewById(R.id.select_state);
        select_category  =findViewById(R.id.select_category);

        selected_category  =findViewById(R.id.selected_category);
        coWorking_recyclerView  =findViewById(R.id.coWorking_recyclerView);

        select_country.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            if(position !=0){
                selected_country_name   = item.replace(" ","_");
                getStateList(selected_country_name);
            }
            if(position ==0){
                Toast.makeText(getApplicationContext(),"You have not select the right option", Toast.LENGTH_LONG);
            }
        });
        select_state.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            String item = String.valueOf(parent.getItemAtPosition(position));
            if(position !=0){
                selected_state_name = item.replace(" ","_");
                getCategory();
            }
            if(position ==0){
                Toast.makeText(getApplicationContext(),"You have not select the right option", Toast.LENGTH_LONG);
            }
        });
        select_category.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            selector_checker  = true;
            String item = String.valueOf(parent.getItemAtPosition(position));
            if(selector_checker && position !=0){
                selected_category.setText(item);
                selected_category_name  = item;
                getSelectedCategory(selected_country_name,selected_state_name,selected_category_name);
            }
//            if(position ==0){
//                selected_category.setText(item);
//                selector_checker  = false;
//            }
        });

        coWorkingAdapter = new CoWorkingAdapter(getApplicationContext(), coWorkingModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        coWorking_recyclerView.setHasFixedSize(true);
        coWorking_recyclerView.setLayoutManager(linearLayoutManager);
        coWorking_recyclerView.setAdapter(coWorkingAdapter);
        if(!selector_checker){
            selected_category.setText("All Platform");
        }


        getCountryList();
        getAll();
    }

    private void getSelectedCategory(String country_name, String state_name, String category_name) {
        String requestUrl = appLinks.get_slected_accelerator_category;



        myProgressDialog.setMessage("Please wait ...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {


            myProgressDialog.dismiss();



            List<CoWorkingModel> listingModels = new ArrayList<>();

            try {


                if(response.contains("status")){
                    JSONObject jsonObject  = new JSONObject(response);
                    String status   = jsonObject.getString("status");
                    String msg   = jsonObject.getString("msg");

                    Toast.makeText(CoWorkingActivity.this, msg, Toast.LENGTH_LONG).show();

                    coWorkingModelList.clear();
                    coWorkingAdapter.notifyDataSetChanged();
                }else{
                    JSONArray jsonArray   = new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONObject  dataObj     = jsonArray.getJSONObject(i);
                        Log.e("Grant", String.valueOf(i));

                        String user_id         = dataObj.getString("user_id");
                        String user_name         = dataObj.getString("user_name");
                        String user_img         = dataObj.getString("user_img");
                        String full_name         = dataObj.getString("full_name");
                        String address         = dataObj.getString("address");
                        String desc         = dataObj.getString("desc");
                        String email         = dataObj.getString("email");
                        String phone         = dataObj.getString("phone");
                        String accelerator         = dataObj.getString("accelerator");
                        String co_working_space         = dataObj.getString("co_working_space");
                        String hub         = dataObj.getString("hub");

                        listingModels.add(new CoWorkingModel(user_id,user_name,user_img,full_name,address,desc,email,phone,accelerator,co_working_space,hub));

                        Log.e("CategoryName",accelerator);
                        Log.e("CategoryName",co_working_space);
                        Log.e("CategoryName",hub);
                    }



                    coWorkingModelList.clear();
                    coWorkingModelList.addAll(listingModels);
                    // refreshing recycler view
                    coWorkingAdapter.notifyDataSetChanged();

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
                postMap.put("country_name", country_name);
                postMap.put("state_name", state_name);
                postMap.put("category_name", category_name);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }

    private void getAll() {

        //myProgressDialog.setMessage("Please wait ...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_works_space_n_accelerator_n_hub_list;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    //myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.e("getEvent",response.toString());
                    List<CoWorkingModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<CoWorkingModel>>() {
                    }.getType());



                    coWorkingModelList.clear();
                    coWorkingModelList.addAll(list);

                    // refreshing recycler view
                    coWorkingAdapter.notifyDataSetChanged();

                }, error -> {
            // error in getting
            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            //myProgressDialog.dismiss();

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void getCountryList() {

        myProgressDialog.create_dialog("Please wait...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_country_list;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="-:-";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int id        = detailObj.getInt("id");
                            String country_name        = detailObj.getString("country_name");
                            arrayList.add(country_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset_1 = new LinkedList<>(arrayList);
                    select_country.attachDataSource(dataset_1);

                }, error -> {
            // error in getting
            myProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
            Log.e("BusinessListing",error.toString());

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }
    private void getStateList(String country_name) {

        myProgressDialog.create_dialog("Please wait...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_state_list +"/"+country_name;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="All Category Type";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int id        = detailObj.getInt("id");
                            String state_name        = detailObj.getString("state_name");
                            arrayList.add(state_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset_2 = new LinkedList<>(arrayList);
                    select_state.attachDataSource(dataset_2);

                }, error -> {
            // error in getting
            myProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
            Log.e("BusinessListing",error.toString());

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }
    private void getCategory() {

        myProgressDialog.create_dialog("Please wait...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_accelerator_category;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="All Category Type";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int cat_id        = detailObj.getInt("cat_id");
                            String cat_name        = detailObj.getString("cat_name");
                            arrayList.add(cat_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset_3 = new LinkedList<>(arrayList);
                    select_category.attachDataSource(dataset_3);

                }, error -> {
            // error in getting
            myProgressDialog.dismiss();
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
    public void OnClick(CoWorkingModel model) {

    }
}
