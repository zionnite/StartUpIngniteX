package xyz.esuku.startupingnitex.Controller;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.CommunityAdapter;
import xyz.esuku.startupingnitex.ItemClicked.CommunityCheckListener;
import xyz.esuku.startupingnitex.ItemClicked.CommunityItemClickListener;
import xyz.esuku.startupingnitex.ItemClicked.ForumCheckListener;
import xyz.esuku.startupingnitex.Model.CommunityModel;
import xyz.esuku.startupingnitex.Model.ForumModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class CommunityActivity extends AppCompatActivity implements CommunityItemClickListener {
    NiceSpinner niceSpinner;
    List<String> dataset;

    ImageView back_btn;
    CardView group_CardView;
    AppCompatButton create_community_group,search_community_group,community_join_btn,community_unjoin_btn;
    String search_term;
    TextView selected_interest_title;

    RecyclerView community_Recyclerview;
    CommunityAdapter communityAdapter;
    List<CommunityModel> communityModelList = new ArrayList<>();

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    boolean grant_selector =false;
    String user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        myProgressDialog    = new MyProgressDialog(CommunityActivity.this);
        userDbHelper = new UserDb_Helper(CommunityActivity.this);

        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name    = user.get("user_name");

        back_btn    =findViewById(R.id.back_btn);
        niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        //create_community_group = findViewById(R.id.create_community_group);
        search_community_group = findViewById(R.id.search_community_group);

        group_CardView      =findViewById(R.id.group_CardView);
        community_join_btn      =findViewById(R.id.community_join_btn);
        community_unjoin_btn      =findViewById(R.id.community_unjoin_btn);
        community_Recyclerview      = findViewById(R.id.community_RecyclerView);
        selected_interest_title      = findViewById(R.id.selected_interest_title);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        create_community_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(CommunityActivity.this,"Make Group Clicked",Toast.LENGTH_LONG).show();
//            }
//        });

        search_community_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityActivity.this,search_term,Toast.LENGTH_LONG).show();
            }
        });


        communityAdapter = new CommunityAdapter(getApplicationContext(), communityModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        community_Recyclerview.setHasFixedSize(true);
        community_Recyclerview.setLayoutManager(linearLayoutManager);

        community_Recyclerview.setAdapter(communityAdapter);

        niceSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                grant_selector  = true;

                String item = String.valueOf(parent.getItemAtPosition(position));
                //Toast.makeText(GrantActivity.this, item, Toast.LENGTH_SHORT).show();
                Log.d("Grant ID", String.valueOf(id)+" POSITION "+String.valueOf(position)+ "NAME "+item);

                if(grant_selector && position !=0){
                    selected_interest_title.setText(item);
                    getGroupByCategory(item);
                }
                if(position ==0){
                    selected_interest_title.setText(item);
                    grant_selector  = false;
                    getGroups();
                }
            }
        });

        if(!grant_selector){
            selected_interest_title.setText("All Community Group");
        }

        getCommunityCategory();
        getGroups();
    }

    @Override
    public void OnJoinToggle(CommunityModel model, int position) {

        String group_id     = model.getGroup_id();
        int id        = position;
        final String[] g_id = new String[1];
        final String[] msg = new String[1];
        final String[] count = new String[1];
        final String[] check = new String[1];
        toggle_join_btn(group_id,user_name, new CommunityCheckListener() {
                    @Override
                    public void checker(String group_id, String response_msg,String counter, String checker) {

                        g_id[0] = group_id;
                        msg[0] = response_msg;
                        count[0] = counter;
                        check[0] = checker;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<String>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                        Log.v("PCom", String.valueOf(position));
                        Log.v("PCom", g_id[0]);
                        Log.v("PCom", count[0]);
                        Log.v("PCom", check[0]);

                        if(s.equals("success")){
                            //Toast.makeText(CommunityActivity.this, msg[0], Toast.LENGTH_LONG).show();
                            communityAdapter.update_item(position,g_id[0],count[0],check[0]);
                        }
                        else if(s.equals("fail")){
                            //Toast.makeText(CommunityActivity.this, msg[0], Toast.LENGTH_LONG).show();
                        }



                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void OnClick(CommunityModel model) {
        
        Intent intent = new Intent(CommunityActivity.this, CommunityGroupActivity.class);
        intent.putExtra("group_id", model.getGroup_id());
        intent.putExtra("group_icon", model.getGroup_icon());
        intent.putExtra("group_desc", model.getGroup_desc());
        intent.putExtra("group_name", model.getGroup_name());
        intent.putExtra("created_by", model.getCreated_by());
        intent.putExtra("date_created", model.getDate_created());
        intent.putExtra("checker", model.getChecker());
        intent.putExtra("counter", model.getCounter());
        startActivity(intent);
    }

    private void getCommunityCategory() {



        String requestUrl = appLinks.get_group_category;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.e("Community", response.toString());
                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="All Community";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int group_id        = detailObj.getInt("group_id");
                            String group_name        = detailObj.getString("group_name");
                            arrayList.add(group_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset = new LinkedList<>(arrayList);
                    niceSpinner.attachDataSource(dataset);

                }, error -> {
            // error in getting
            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            Log.e("BusinessListing",error.toString());

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }

    private void getGroups(){

        myProgressDialog.setMessage("Please wait ...");
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_all_group+"/"+user_name;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {

                    myProgressDialog.dismiss();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.e("getEvent",response.toString());
                    List<CommunityModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<CommunityModel>>() {
                    }.getType());



                    communityModelList.clear();
                    communityModelList.addAll(list);

                    // refreshing recycler view
                    communityAdapter.notifyDataSetChanged();

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

    private void getGroupByCategory(String item) {
        String requestUrl = appLinks.get_group_by_category;



        myProgressDialog.setMessage("Please wait ...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {


            myProgressDialog.dismiss();



            List<CommunityModel> listingModels = new ArrayList<>();

            try {


                if(response.contains("status")){
                    JSONObject  jsonObject  = new JSONObject(response);
                    String status   = jsonObject.getString("status");
                    String msg   = jsonObject.getString("msg");

                    Toast.makeText(CommunityActivity.this, msg, Toast.LENGTH_LONG).show();

                    communityModelList.clear();
                    communityAdapter.notifyDataSetChanged();
                }else{
                    JSONArray jsonArray   = new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONObject  dataObj     = jsonArray.getJSONObject(i);
                        Log.e("Grant", String.valueOf(i));

                        String group_id         = dataObj.getString("group_id");
                        String group_icon         = dataObj.getString("group_icon");
                        String group_desc         = dataObj.getString("group_desc");
                        String group_name         = dataObj.getString("group_name");
                        String created_by         = dataObj.getString("created_by");
                        String date_created         = dataObj.getString("date_created");
                        String checker         = dataObj.getString("checker");
                        String counter         = dataObj.getString("counter");

                        listingModels.add(new CommunityModel(group_id,group_icon,group_desc,group_name,created_by,date_created,checker,counter));
                    }



                    communityModelList.clear();
                    communityModelList.addAll(listingModels);
                    // refreshing recycler view
                    communityAdapter.notifyDataSetChanged();

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
                postMap.put("group_cat", item);
                postMap.put("user_name", user_name);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }


    io.reactivex.Observable<String> toggle_join_btn(final String group_id, final String user_name, CommunityCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.join_group_community_toggle;

                Map<String, String> postMap = new HashMap<>();
                postMap.put("group_id", String.valueOf(group_id));
                postMap.put("user_name", user_name);

                JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                        response -> {

                            Log.e("RxAndroidServer",response.toString());
                            String event_checker;
                            try {
                                String msg = response.getString("msg");
                                String status = response.getString("status");
                                String toggle_status;
                                if(status.equals("success")){


                                    Log.e("RxAndroidServer3",response.toString());


                                    JSONObject jsonObject   = response.getJSONObject("detail");


                                    String group_id         = jsonObject.getString("group_id");
                                    String group_icon         = jsonObject.getString("group_icon");
                                    String group_desc         = jsonObject.getString("group_desc");
                                    String group_name         = jsonObject.getString("group_name");
                                    String created_by         = jsonObject.getString("created_by");
                                    String date_created         = jsonObject.getString("date_created");
                                    String checker         = jsonObject.getString("group_checker");
                                    String counter         = jsonObject.getString("group_counter");


                                    checkListener.checker(group_id,msg,counter,checker);

                                    emitter.onNext(status);

                                }else if(status.equals("fail")){

                                    emitter.onNext(status);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                emitter.onError(e);
                                Log.e("RxAndroidServer4",e.toString());
                            }

                            emitter.onComplete();
                        }, error -> {
                    emitter.onError(error);
                    Log.e("RxAndroidServer5",error.toString());
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
        });
    }
}
