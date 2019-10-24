package xyz.esuku.startupingnitex.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import xyz.esuku.startupingnitex.Adapter.ForumDetailAdapter;
import xyz.esuku.startupingnitex.Adapter.GroupPostAdapter;
import xyz.esuku.startupingnitex.ItemClicked.ForumDetailCheckerListener;
import xyz.esuku.startupingnitex.ItemClicked.GroupPOstItemClickListener;
import xyz.esuku.startupingnitex.ItemClicked.GroupPostCheckListener;
import xyz.esuku.startupingnitex.Model.ForumDetailModel;
import xyz.esuku.startupingnitex.Model.GroupPostModel;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityGroupActivity extends AppCompatActivity implements GroupPOstItemClickListener {

    CardView send_feedback;

    ImageView back_btn,post_add_attachment,post_send,post_send_2;
    AppCompatEditText post_input_box,post_input_box_2;

    TextView group_name;
    String Group_id, Group_name, Checker, user_name;


    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    private final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;

    RecyclerView post_RecyclerView;
    GroupPostAdapter groupPostAdapter;
    List<GroupPostModel> groupPostModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_group);
        myProgressDialog    = new MyProgressDialog(CommunityGroupActivity.this);
        userDbHelper = new UserDb_Helper(CommunityGroupActivity.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name   = user.get("user_name");


        init();

        back_btn    = findViewById(R.id.back_btn);
        post_add_attachment    = findViewById(R.id.post_add_attachment);
        post_send    = findViewById(R.id.post_send);
        post_send_2    = findViewById(R.id.post_send_2);
        post_input_box    = findViewById(R.id.post_input_box);
        post_input_box_2    = findViewById(R.id.post_input_box_2);
        post_RecyclerView    = findViewById(R.id.post_RecyclerView);
        send_feedback    = findViewById(R.id.send_feedback);

        if(Checker.equals("false")){
            send_feedback.setVisibility(View.GONE);
        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityGroupActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

        post_add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(
                        CommunityGroupActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        post_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText    = post_input_box.getText().toString().trim();

//                Toast.makeText(CommunityGroupActivity.this,inputText,Toast.LENGTH_LONG).show();
//
                String selected_item    = "";

                post_input_box.clearFocus();
                post_input_box.setText("");

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(post_input_box.getWindowToken(), 0);

                makeGropPost(inputText, Group_id, Group_name, user_name);
            }
        });

        post_send_2.setOnClickListener(v->{
            String inputedText  = post_input_box_2.getText().toString().trim();

            post_input_box_2.clearFocus();
            post_input_box_2.setText("");

            post_add_attachment.setVisibility(View.VISIBLE);
            post_send.setVisibility(View.VISIBLE);
            post_input_box.setVisibility(View.VISIBLE);

            post_send_2.setVisibility(View.GONE);
            post_input_box_2.setVisibility(View.GONE);

            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(post_input_box_2.getWindowToken(), 0);
            makeGropPost(inputedText, bitmap, Group_id, Group_name,user_name);
        });

        groupPostAdapter = new GroupPostAdapter(getApplicationContext(), groupPostModelList, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        post_RecyclerView.setHasFixedSize(true);
        post_RecyclerView.setLayoutManager(linearLayoutManager);
        post_RecyclerView.setAdapter(groupPostAdapter);


    }

    private void makeGropPost(String inputedText, String group_id, String group_name, String user_name) {

        final String[] m_post_id = new String[1];
        final String[] m_post_content = new String[1];
        final String[] m_post_time = new String[1];
        final String[] m_post_by = new String[1];
        final String[] m_post_like_counter = new String[1];
        final String[] m_post_like_checker = new String[1];
        final String[] m_is_text = new String[1];
        final String[] m_post_content_image = new String[1];
        final String[] m_poster_img = new String[1];



        particpate_on_conversation(group_id, group_name, user_name, inputedText, new GroupPostCheckListener() {
                    @Override
                    public void onChecker(String post_id, String post_content, String post_time, String post_by, String post_like_counter, String post_like_checker, String is_text, String post_content_image, String poster_img) {
                        m_post_id[0] =post_id;
                        m_post_content[0] =post_content;
                        m_post_time[0] =post_time;
                        m_post_by[0] =post_by;
                        m_post_like_counter[0] =post_like_counter;
                        m_post_like_checker[0] =post_like_checker;
                        m_is_text[0] =is_text;
                        m_post_content_image[0] =post_content_image;
                        m_poster_img[0] =poster_img;
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

                        Toast.makeText(CommunityGroupActivity.this,s, Toast.LENGTH_LONG).show();

                        //add to the Model
                        //ForumDetailAdapter.update_item(position, m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_isText[0],m_counter[0]);

                        GroupPostModel model = new GroupPostModel(); //getting object from the list


                        model.setPost_id(m_post_id[0]);
                        model.setPost_by(m_post_by[0]);
                        model.setPost_content(m_post_content[0]);
                        model.setPost_content_image(m_post_content_image[0]);
                        model.setPost_like_checker(m_post_like_checker[0]);
                        model.setPost_like_counter(m_post_like_counter[0]);
                        model.setPost_time(m_post_time[0]);
                        model.setPoster_img(m_poster_img[0]);
                        model.setIs_text(m_is_text[0]);

                        groupPostModelList.add(model);
                        groupPostAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void makeGropPost(String inputedText, Bitmap bitmap, String group_id, String group_name, String user_name) {

        final String[] m_post_id = new String[1];
        final String[] m_post_content = new String[1];
        final String[] m_post_time = new String[1];
        final String[] m_post_by = new String[1];
        final String[] m_post_like_counter = new String[1];
        final String[] m_post_like_checker = new String[1];
        final String[] m_is_text = new String[1];
        final String[] m_post_content_image = new String[1];
        final String[] m_poster_img = new String[1];



        particpate_on_conversation(group_id, group_name, user_name, inputedText, bitmap, new GroupPostCheckListener() {
            @Override
            public void onChecker(String post_id, String post_content, String post_time, String post_by, String post_like_counter, String post_like_checker, String is_text, String post_content_image, String poster_img) {
                m_post_id[0] =post_id;
                m_post_content[0] =post_content;
                m_post_time[0] =post_time;
                m_post_by[0] =post_by;
                m_post_like_counter[0] =post_like_counter;
                m_post_like_checker[0] =post_like_checker;
                m_is_text[0] =is_text;
                m_post_content_image[0] =post_content_image;
                m_poster_img[0] =poster_img;
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

                        Toast.makeText(CommunityGroupActivity.this,s, Toast.LENGTH_LONG).show();

                        //add to the Model
                        //ForumDetailAdapter.update_item(position, m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_isText[0],m_counter[0]);

                        GroupPostModel model = new GroupPostModel(); //getting object from the list


                        model.setPost_id(m_post_id[0]);
                        model.setPost_by(m_post_by[0]);
                        model.setPost_content(m_post_content[0]);
                        model.setPost_content_image(m_post_content_image[0]);
                        model.setPost_like_checker(m_post_like_checker[0]);
                        model.setPost_like_counter(m_post_like_counter[0]);
                        model.setPost_time(m_post_time[0]);
                        model.setPoster_img(m_poster_img[0]);
                        model.setIs_text(m_is_text[0]);

                        groupPostModelList.add(model);
                        groupPostAdapter.notifyDataSetChanged();



                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    Observable<String> particpate_on_conversation(final String group_id, final String group_name, final String user_name, String answer, GroupPostCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.insert_to_group_post;

                Log.e("DataEnt", group_id);
                Log.e("DataEnt", user_name);
                Log.e("DataEnt", answer);

                Map<String, String> postMap = new HashMap<>();
                postMap.put("group_id", group_id);
                postMap.put("group_name", group_name);
                postMap.put("user_name", user_name);
                postMap.put("post_content", answer);
                postMap.put("is_text", "true");
                postMap.put("image_name", "null");

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

                                    JSONArray jsonArray = response.getJSONArray("detail");
                                    JSONObject detail     =jsonArray.getJSONObject(0);


                                    String post_id      = detail.getString("post_id");
                                    String post_content      = detail.getString("post_content");
                                    String post_time      = detail.getString("post_time");
                                    String post_by      = detail.getString("post_by");
                                    String post_like_counter      = detail.getString("post_like_counter");
                                    String post_like_checker      = detail.getString("post_like_checker");
                                    String is_text  =detail.getString("is_text");
                                    String post_content_image   =detail.getString("post_content_image");
                                    String poster_img   =detail.getString("poster_img");



                                    checkListener.onChecker(post_id,post_content,post_time,post_by,post_like_counter,post_like_checker,is_text,post_content_image,poster_img);

                                    emitter.onNext(msg);

                                }else if(status.equals("error")){

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

    Observable<String> particpate_on_conversation(final String group_id, final String group_name, final String user_name, String answer, Bitmap bitmap, GroupPostCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.insert_to_group_post;


//
                String image_name   = imageToString(bitmap);

                Map<String, String> postMap = new HashMap<>();
                postMap.put("group_id", group_id);
                postMap.put("group_name", group_name);
                postMap.put("user_name", user_name);
                postMap.put("post_content", answer);
                postMap.put("is_text", "false");
                postMap.put("image_name", image_name);

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

                                    JSONArray jsonArray = response.getJSONArray("detail");
                                    JSONObject detail     =jsonArray.getJSONObject(0);


                                    String post_id      = detail.getString("post_id");
                                    String post_content      = detail.getString("post_content");
                                    String post_time      = detail.getString("post_time");
                                    String post_by      = detail.getString("post_by");
                                    String post_like_counter      = detail.getString("post_like_counter");
                                    String post_like_checker      = detail.getString("post_like_checker");
                                    String is_text  =detail.getString("is_text");
                                    String post_content_image   =detail.getString("post_content_image");
                                    String poster_img   =detail.getString("poster_img");



                                    checkListener.onChecker(post_id,post_content,post_time,post_by,post_like_counter,post_like_checker,is_text,post_content_image,poster_img);


                                    emitter.onNext(msg);

                                }else if(status.equals("error")){

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



    public void init(){

        Group_id    = getIntent().getExtras().getString("group_id");
        String Group_icon    = getIntent().getExtras().getString("group_icon");
        String Group_desc    = getIntent().getExtras().getString("group_desc");
        Group_name    = getIntent().getExtras().getString("group_name");
        String Created_by    = getIntent().getExtras().getString("created_by");
        String Date_created    = getIntent().getExtras().getString("date_created");
        Checker    = getIntent().getExtras().getString("checker");
        String Counter    = getIntent().getExtras().getString("counter");


        group_name          = findViewById(R.id.group_name);
        group_name.setText(Group_name);

        get_post(Group_id);
    }


    public void get_post(String group_id){
        Log.e("PostDetail",group_id);
        myProgressDialog.setMessage("Please wait ...");
        String requestUrl = appLinks.get_group_post+"/"+group_id;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    myProgressDialog.dismiss();
                    Log.d("BusinessListing",response.toString());
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Log.e("PostDetail",response.toString());

                    List<GroupPostModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<GroupPostModel>>() {
                    }.getType());




                    groupPostModelList.clear();
                    groupPostModelList.addAll(list);

                    // refreshing recycler view
                    groupPostAdapter.notifyDataSetChanged();

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


    @Override
    public void onclick(GroupPostModel model) {

    }

    @Override
    public void onLikeToggle(String post_id, int selected_position) {

        final String[] m_post_id = new String[1];
        final String[] m_post_content = new String[1];
        final String[] m_post_time = new String[1];
        final String[] m_post_by = new String[1];
        final String[] m_post_like_counter = new String[1];
        final String[] m_post_like_checker = new String[1];
        final String[] m_is_text = new String[1];
        final String[] m_post_content_image = new String[1];
        final String[] m_poster_img = new String[1];



        toggle_post_like(Group_id, post_id, user_name, new GroupPostCheckListener() {
            @Override
            public void onChecker(String post_id, String post_content, String post_time, String post_by, String post_like_counter, String post_like_checker, String is_text, String post_content_image, String poster_img) {
                m_post_id[0] =post_id;
                m_post_content[0] =post_content;
                m_post_time[0] =post_time;
                m_post_by[0] =post_by;
                m_post_like_counter[0] =post_like_counter;
                m_post_like_checker[0] =post_like_checker;
                m_is_text[0] =is_text;
                m_post_content_image[0] =post_content_image;
                m_poster_img[0] =poster_img;
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

                        Toast.makeText(CommunityGroupActivity.this,s, Toast.LENGTH_LONG).show();

                        //add to the Model
                        //ForumDetailAdapter.update_item(position, m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_isText[0],m_counter[0]);

//

                        //groupPostModelList.add(model);
                        groupPostAdapter.upldate_item(selected_position, m_post_like_checker[0], m_post_like_counter[0]);

                        //eventAdapter.update_item(position,event_check[0],event_count[0]);


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Observable<String> toggle_post_like(final String group_id, final String post_id, final String user_name, GroupPostCheckListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.group_post_like_toggle;

                Map<String, String> postMap = new HashMap<>();
                postMap.put("group_id", group_id);
                postMap.put("user_name", user_name);
                postMap.put("post_id", post_id);

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

                                    JSONArray jsonArray = response.getJSONArray("detail");
                                    JSONObject detail     =jsonArray.getJSONObject(0);


                                    String post_id      = detail.getString("post_id");
                                    String post_content      = detail.getString("post_content");
                                    String post_time      = detail.getString("post_time");
                                    String post_by      = detail.getString("post_by");
                                    String post_like_counter      = detail.getString("post_like_counter");
                                    String post_like_checker      = detail.getString("post_like_checker");
                                    String is_text  =detail.getString("is_text");
                                    String post_content_image   =detail.getString("post_content_image");
                                    String poster_img   =detail.getString("poster_img");



                                    checkListener.onChecker(post_id,post_content,post_time,post_by,post_like_counter,post_like_checker,is_text,post_content_image,poster_img);

                                    emitter.onNext(msg);

                                }else if(status.equals("error")){

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Intent intent   = new Intent(Intent.ACTION_PICK);
//                intent.setType("images/*");

                Intent intent   = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Select An Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(),"You don't have permission to access gallery",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data !=null){
            Uri filepath    = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                //prof_imageName.setImageBitmap(bitmap);
                if(bitmap !=null){
                    //perfomrm_uploading_to_server();
                    post_add_attachment.setVisibility(View.GONE);
                    post_send.setVisibility(View.GONE);
                    post_input_box.setVisibility(View.GONE);

                    post_send_2.setVisibility(View.VISIBLE);
                    post_input_box_2.setVisibility(View.VISIBLE);



                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }
}
