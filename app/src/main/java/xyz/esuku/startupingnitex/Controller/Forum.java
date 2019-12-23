package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Adapter.ForumAdapter;
import xyz.esuku.startupingnitex.ItemClicked.ForumCheckListener;
import xyz.esuku.startupingnitex.ItemClicked.ForumItemClickListener;
import xyz.esuku.startupingnitex.Model.ForumModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forum extends AppCompatActivity implements ForumItemClickListener {

    ImageView back_btn,search_forum;
    FloatingActionButton create_contentFAB;

    RecyclerView forum_recyclerView;
    ForumAdapter forumAdapter;
    List<ForumModel> forumModelList = new ArrayList<>();

    TextView goBack;
    AppCompatEditText search_box;
    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    Boolean searching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        myProgressDialog    = new MyProgressDialog(Forum.this);
        userDbHelper = new UserDb_Helper(Forum.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        forum_recyclerView      = findViewById(R.id.ask_RecyclerView);
        back_btn   = findViewById(R.id.back_btn);
        //search_forum   = findViewById(R.id.search_forum);
        search_box   = findViewById(R.id.search_box);
        goBack   = findViewById(R.id.goBack);
        create_contentFAB   = findViewById(R.id.create_contentFAB);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Forum.this, MainActivity.class);
            startActivity(intent);
        });

        //search_forum.setOnClickListener(v -> Toast.makeText(Forum.this,"Search Clicked",Toast.LENGTH_LONG).show());

        create_contentFAB.setOnClickListener(v -> {
            Intent intent = new Intent(Forum.this, ForumAskPost.class);
            startActivity(intent);
        });

        search_box.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                String search_term  = search_box.getText().toString().trim();
                Log.d("Bus", "Action Search" + search_box.getText().toString().trim());
                performSearch(search_term);
                return true;
            }
            return false;
        });


//        forumModelList.add(new ForumModel(R.drawable.user_profile,"zionnite","Benin City","5mins Ago","content goes here",R.drawable.user_profile_b));
//        forumModelList.add(new ForumModel(R.drawable.user_profile,"zionnite","Benin City","50mins Ago","content goes here",R.drawable.user_profile));
//        forumModelList.add(new ForumModel(R.drawable.user_profile,"zionnite","Benin City","1mins Ago","content goes here",R.drawable.user_profile_b));

        forumAdapter = new ForumAdapter(getApplicationContext(), forumModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        forum_recyclerView.setHasFixedSize(true);
        forum_recyclerView.setLayoutManager(linearLayoutManager);

        forum_recyclerView.setAdapter(forumAdapter);


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForumPost();
                finish();
            }
        });
        getForumPost();
    }

    @Override
    public void OnClicked(ForumModel forumModel, int position) {
        Intent intent = new Intent(Forum.this, ForumDetailActivity.class);
        intent.putExtra("user_name",forumModel.getUser_name());
        intent.putExtra("user_city",forumModel.getCity());
        intent.putExtra("user_time",forumModel.getTime_ago());
        intent.putExtra("user_image",forumModel.getUser_image());
        intent.putExtra("forum_content",forumModel.getContent());
        intent.putExtra("forum_id",forumModel.getId());
        intent.putExtra("position",position);
        startActivity(intent);


    }

    @Override
    public void OnParticipate(ForumModel forumModel, int select_item) {
        int position    = select_item;
        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String ask_id        = forumModel.getId();


        final String[] m_id = new String[1];
        final String[] m_ask_user_image = new String[1];
        final String[] m_ask_user_name = new String[1];
        final String[] m_ask_city = new String[1];
        final String[] m_ask_time = new String[1];
        final String[] m_ask_content = new String[1];
        final String[] m_last_paticipator = new String[1];
        final String[] m_question_status = new String[1];
        final String[] m_counter = new String[1];


        particpate_on_question(ask_id, user_name, new ForumCheckListener() {
            @Override
            public void onChecker(String id, String ask_user_image, String ask_user_name, String ask_city, String ask_time, String ask_content, String last_paticipator, String question_status, String counter) {
                m_id[0] = id;
                m_ask_user_image[0] = ask_user_image;
                m_ask_user_name[0] = ask_user_name;
                m_ask_city[0] = ask_city;
                m_ask_time[0] = ask_time;
                m_ask_content[0] = ask_content;
                m_last_paticipator[0] = last_paticipator;
                m_question_status[0] = question_status;
                m_counter[0] = counter;


            }
        }).subscribeOn(Schedulers.newThread())
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

                        Toast.makeText(Forum.this,s, Toast.LENGTH_LONG).show();
                        forumAdapter.update_item(position,m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_last_paticipator[0],m_question_status[0],m_counter[0]);
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
    public void OnAnswer(ForumModel forumModel, int position) {
        Log.e("Forum_id", String.valueOf(forumModel.getId()));
        Intent intent = new Intent(Forum.this, ForumDetailActivity.class);
        intent.putExtra("user_name",forumModel.getUser_name());
        intent.putExtra("user_city",forumModel.getCity());
        intent.putExtra("user_time",forumModel.getTime_ago());
        intent.putExtra("user_image",forumModel.getUser_image());
        intent.putExtra("forum_content",forumModel.getContent());
        intent.putExtra("forum_id",forumModel.getId());
        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Override
    public void viewUser(String user_name) {
        Log.e("ViewUser","View User Fire");
        Intent intent = new Intent(Forum.this, ViewUserProfile.class);
        intent.putExtra("user_name",user_name);
        intent.putExtra("back_action","Forum.class");
        startActivity(intent);
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


        String requestUrl = appLinks.search_question;
        myProgressDialog.setMessage("Please wait ...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            myProgressDialog.dismiss();
            List<ForumModel> listingModels = new ArrayList<>();

            Log.e("Search 0",response);
            try {
//                if(response.contains("status")){
//                    Log.e("Search 1",response);
//                    JSONObject  jsonObject  = new JSONObject(response);
//                    String status   = jsonObject.getString("status");
//                    String msg   = jsonObject.getString("msg");
//
//                    Toast.makeText(Forum.this, msg, Toast.LENGTH_LONG).show();
//                    forumModelList.clear();
//                    forumAdapter.notifyDataSetChanged();
//                }else{
                    Log.e("Search 2",response);
                    JSONArray   jsonArray   = new JSONArray(response);
                    for (int i=0; i < jsonArray.length(); i++) {
                        JSONObject  detailObj     = jsonArray.getJSONObject(i);

                        String ask_id     = detailObj.getString("ask_id");
                        String ask_user_name     = detailObj.getString("ask_user_name");
                        String ask_user_image     = detailObj.getString("ask_user_image");
                        String ask_city     = detailObj.getString("ask_city");
                        String ask_time     = detailObj.getString("ask_time");
                        String ask_content     = detailObj.getString("ask_content");
                        String last_paticipator     = detailObj.getString("last_paticipator");
                        String question_status     = detailObj.getString("question_status");
                        String counter              = detailObj.getString("question_counter");

                        listingModels.add(new ForumModel(ask_id,ask_user_image,ask_user_name,ask_city,ask_time,ask_content,last_paticipator,question_status,counter));
                    //}



                    forumModelList.clear();
                    forumModelList.addAll(listingModels);
                    // refreshing recycler view
                    forumAdapter.notifyDataSetChanged();

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
                postMap.put("search_term", search_term);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }


    private void getForumPost() {
        searching =false;
        if (!searching){
            goBack.setVisibility(View.GONE);
        }

        myProgressDialog.setMessage("Please wait ...");
        String requestUrl = appLinks.get_question;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    myProgressDialog.dismiss();
                    Log.d("BusinessListing",response.toString());
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<ForumModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<ForumModel>>() {
                    }.getType());




                    forumModelList.clear();
                    forumModelList.addAll(list);

                    // refreshing recycler view
                    forumAdapter.notifyDataSetChanged();

                }, error -> {
                    // error in getting
                    myProgressDialog.dismiss();
                    Log.e("Post", error.toString());
                    Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();


                });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }


    Observable<String> particpate_on_question(final String ask_id, final String user_name, ForumCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.toggle_paticipation;

                Map<String, String> postMap = new HashMap<>();
                postMap.put("question_id", String.valueOf(ask_id));
                postMap.put("user_name", user_name);

                JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                        response -> {

                            Log.e("RxAndroidServer",response.toString());
                            String event_checker;
                            try {
                                String msg = response.getString("msg");
                                String status = response.getString("status");
                                String toggle_status;
                                if(status.equals("participating") || status.equals("unparticipating")){


                                    Log.e("RxAndroidServer3",response.toString());


                                    JSONObject jsonObject   = response.getJSONObject("detail");
                                    String id      = jsonObject.getString("ask_id");
                                    String ask_user_name      = jsonObject.getString("ask_user_name");
                                    String ask_user_image      = jsonObject.getString("ask_user_image");
                                    String ask_city      = jsonObject.getString("ask_city");
                                    String ask_time      = jsonObject.getString("ask_time");
                                    String ask_content      = jsonObject.getString("ask_content");
                                    String last_paticipator      = jsonObject.getString("last_paticipator");
                                    String question_status      = jsonObject.getString("question_status");
                                    String counter  =jsonObject.getString("question_counter");



                                    checkListener.onChecker(id,ask_user_image,ask_user_name,ask_city,ask_time,ask_content,last_paticipator,question_status,counter);


                                    emitter.onNext(msg);

                                }else if(status.equals("unparticipating")){

                                    emitter.onNext(msg);
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
