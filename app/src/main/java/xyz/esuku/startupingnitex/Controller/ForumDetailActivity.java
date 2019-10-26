package xyz.esuku.startupingnitex.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import de.hdodenhof.circleimageview.CircleImageView;
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
import xyz.esuku.startupingnitex.Adapter.ForumDetailAdapter;
import xyz.esuku.startupingnitex.ItemClicked.ForumCheckListener;
import xyz.esuku.startupingnitex.ItemClicked.ForumDetailCheckerListener;
import xyz.esuku.startupingnitex.ItemClicked.ForumItemClickListener;
import xyz.esuku.startupingnitex.Model.ForumDetailModel;
import xyz.esuku.startupingnitex.Model.ForumModel;
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

public class ForumDetailActivity extends AppCompatActivity{

    CircleImageView ask_user_image;
    TextView ask_user_name,ask_user_city,ask_user_time,ask_content,ask_counter;
    ImageView back_btn,forum_add_attachment,forum_send_help,forum_send_help_2;
    AppCompatEditText Forum_inputText,Forum_inputText_2;

    RecyclerView  forum_detailRecyclerView;
    ForumDetailAdapter forumDetailAdapter;
    List<ForumDetailModel> forumDetailModelList = new ArrayList<>();

    String forum_id,user_name;
    int selected_item;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    private final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        myProgressDialog    = new MyProgressDialog(ForumDetailActivity.this);
        userDbHelper = new UserDb_Helper(ForumDetailActivity.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name   = user.get("user_name");

        back_btn        = findViewById(R.id.back_btn);
        forum_add_attachment        = findViewById(R.id.forum_add_attachment);
        forum_send_help        = findViewById(R.id.forum_send_help);
        forum_send_help_2        = findViewById(R.id.forum_send_help_2);
        Forum_inputText        = findViewById(R.id.Forum_inputText);
        Forum_inputText_2        = findViewById(R.id.Forum_inputText_2);

        forum_detailRecyclerView        = findViewById(R.id.forumDetail_RecyclerView);

        setIniView();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumDetailActivity.this, Forum.class);
                startActivity(intent);
                finish();
            }
        });
        forum_add_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(
                        ForumDetailActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
        forum_send_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputedText  = Forum_inputText.getText().toString().trim();

                Forum_inputText.clearFocus();
                Forum_inputText.setText("");

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(Forum_inputText.getWindowToken(), 0);
                answerQuestion(inputedText, forum_id,user_name,selected_item);


            }
        });

        forum_send_help_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputedText  = Forum_inputText_2.getText().toString().trim();

                Forum_inputText_2.clearFocus();
                Forum_inputText_2.setText("");

                forum_add_attachment.setVisibility(View.VISIBLE);
                forum_send_help.setVisibility(View.VISIBLE);
                Forum_inputText.setVisibility(View.VISIBLE);

                forum_send_help_2.setVisibility(View.GONE);
                Forum_inputText_2.setVisibility(View.GONE);

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(Forum_inputText.getWindowToken(), 0);
                answerQuestion(inputedText, bitmap, forum_id,user_name,selected_item);


            }
        });

        //forumDetailModelList.add(new ForumDetailModel(1,"user_image","zionnite","Benin City","5mins Ago","content goes here","1",true));

        forumDetailAdapter = new ForumDetailAdapter(getApplicationContext(), forumDetailModelList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        forum_detailRecyclerView.setHasFixedSize(true);
        forum_detailRecyclerView.setLayoutManager(linearLayoutManager);
        forum_detailRecyclerView.setAdapter(forumDetailAdapter);


        get_answer(forum_id);
    }

    private void setIniView() {
        String user_name    = getIntent().getExtras().getString("user_name");
        String user_city    = getIntent().getExtras().getString("user_city");
        String user_time    = getIntent().getExtras().getString("user_time");
        String user_image    = getIntent().getExtras().getString("user_image");
        String forum_content    = getIntent().getExtras().getString("forum_content");
        forum_id    = getIntent().getExtras().getString("forum_id");
        String counter    = getIntent().getExtras().getString("counter");
        selected_item       = getIntent().getExtras().getInt("position");

        ask_user_image      = findViewById(R.id.ask_user_image);
        ask_user_name      = findViewById(R.id.ask_user_name);






        ask_user_city      = findViewById(R.id.ask_user_city);
        ask_user_time      = findViewById(R.id.ask_user_time);
        ask_content      = findViewById(R.id.ask_content);
        ask_counter      = findViewById(R.id.ask_counter);

        Glide.with(getApplicationContext()).load(user_image).centerCrop().placeholder(R.drawable.start_up_logo).into(ask_user_image);
        ask_user_name.setText(user_name);
        ask_user_city.setText(user_city);
        ask_user_time.setText(user_time);
        ask_content.setText(forum_content);
        ask_counter.setText(counter);
    }

    private void answerQuestion(String inputedText, String ask_id, String user_name,int selected_item) {

        int position    = selected_item;

        final int[] m_id = new int[1];
        final int[] m_q_id = new int[1];
        final String[] m_ask_user_image = new String[1];
        final String[] m_ask_user_name = new String[1];
        final String[] m_ask_city = new String[1];
        final String[] m_ask_time = new String[1];
        final String[] m_ask_content = new String[1];
        final String[] m_isText = new String[1];
        final String[] m_counter = new String[1];
        final String[] m_comment_img = new String[1];



        particpate_on_question(ask_id, user_name, inputedText, new ForumDetailCheckerListener() {
            @Override
            public void onChecker(int a_id, int question_id,String user_image, String user_name, String city, String time_ago, String content,String counter, String isText, String comment_img) {
                m_id[0] = a_id;
                m_q_id[0] = question_id;
                m_ask_user_image[0] = user_image;
                m_ask_user_name[0] = user_name;
                m_ask_city[0] = city;
                m_ask_time[0] = time_ago;
                m_ask_content[0] = content;
                m_isText[0] = isText;
                m_counter[0] = counter;
                m_comment_img[0] = comment_img;

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

                        Toast.makeText(ForumDetailActivity.this,s, Toast.LENGTH_LONG).show();

                        //add to the Model
                        //ForumDetailAdapter.update_item(position, m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_isText[0],m_counter[0]);

                        ForumDetailModel model = new ForumDetailModel(); //getting object from the list


                        model.setA_id(m_id[0]);
                        model.setQuestion_id(m_q_id[0]);
                        model.setUser_image(m_ask_user_image[0]);
                        model.setUser_name(m_ask_user_name[0]);
                        model.setCity(m_ask_city[0]);
                        model.setTime_ago(m_ask_time[0]);
                        model.setContent(m_ask_content[0]);
                        model.setIsText(m_isText[0]);
                        model.setCounter(m_counter[0]);
                        model.setA_content_img(m_comment_img[0]);
                        forumDetailModelList.add(model);
                        forumDetailAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void answerQuestion(String inputedText, Bitmap bitmap, String ask_id, String user_name,int selected_item) {

        int position    = selected_item;

        final int[] m_id = new int[1];
        final int[] m_q_id = new int[1];
        final String[] m_ask_user_image = new String[1];
        final String[] m_ask_user_name = new String[1];
        final String[] m_ask_city = new String[1];
        final String[] m_ask_time = new String[1];
        final String[] m_ask_content = new String[1];
        final String[] m_isText = new String[1];
        final String[] m_counter = new String[1];
        final String[] m_comment_img = new String[1];



        particpate_on_question(ask_id, user_name, inputedText, bitmap, new ForumDetailCheckerListener() {
            @Override
            public void onChecker(int a_id, int question_id,String user_image, String user_name, String city, String time_ago, String content,String counter, String isText, String comment_img) {
                m_id[0] = a_id;
                m_q_id[0] = question_id;
                m_ask_user_image[0] = user_image;
                m_ask_user_name[0] = user_name;
                m_ask_city[0] = city;
                m_ask_time[0] = time_ago;
                m_ask_content[0] = content;
                m_isText[0] = isText;
                m_counter[0] = counter;
                m_comment_img[0] = comment_img;

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

                        Toast.makeText(ForumDetailActivity.this,s, Toast.LENGTH_LONG).show();

                        //add to the Model
                        //ForumDetailAdapter.update_item(position, m_id[0],m_ask_user_image[0],m_ask_user_name[0],m_ask_city[0],m_ask_time[0],m_ask_content[0],m_isText[0],m_counter[0]);

                        ForumDetailModel model = new ForumDetailModel(); //getting object from the list


                        model.setA_id(m_id[0]);
                        model.setQuestion_id(m_q_id[0]);
                        model.setUser_image(m_ask_user_image[0]);
                        model.setUser_name(m_ask_user_name[0]);
                        model.setCity(m_ask_city[0]);
                        model.setTime_ago(m_ask_time[0]);
                        model.setContent(m_ask_content[0]);
                        model.setIsText(m_isText[0]);
                        model.setCounter(m_counter[0]);
                        model.setA_content_img(m_comment_img[0]);
                        forumDetailModelList.add(model);
                        forumDetailAdapter.notifyDataSetChanged();



                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    Observable<String> particpate_on_question(final String ask_id, final String user_name, String answer, ForumDetailCheckerListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.comment_on_ask_question;


//

                Map<String, String> postMap = new HashMap<>();
                postMap.put("question_id", ask_id);
                postMap.put("user_name", user_name);
                postMap.put("answer", answer);
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


                                    int id      = detail.getInt("a_id");
                                    int question_id      = detail.getInt("question_id");
                                    String ask_user_name      = detail.getString("a_user_name");
                                    String ask_user_image      = detail.getString("a_user_image");
                                    String ask_city      = detail.getString("a_city");
                                    String ask_time      = detail.getString("a_time");
                                    String ask_content      = detail.getString("a_content");
                                    String counter  =detail.getString("counter");
                                    String is_text   =detail.getString("is_text");
                                    String comment_img   =detail.getString("a_content_img");

                                    checkListener.onChecker(id,question_id,ask_user_image,ask_user_name,ask_city,ask_time,ask_content,counter,is_text,comment_img);


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

    Observable<String> particpate_on_question(final String ask_id, final String user_name, String answer, Bitmap bitmap, ForumDetailCheckerListener checkListener){

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String requestUrl = appLinks.comment_on_ask_question;


//
                String image_name   = imageToString(bitmap);

                Map<String, String> postMap = new HashMap<>();
                postMap.put("question_id", ask_id);
                postMap.put("user_name", user_name);
                postMap.put("answer", answer);
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


                                    int id      = detail.getInt("a_id");
                                    int question_id      = detail.getInt("question_id");
                                    String ask_user_name      = detail.getString("a_user_name");
                                    String ask_user_image      = detail.getString("a_user_image");
                                    String ask_city      = detail.getString("a_city");
                                    String ask_time      = detail.getString("a_time");
                                    String ask_content      = detail.getString("a_content");
                                    String counter  =detail.getString("counter");
                                    String is_text   =detail.getString("is_text");
                                    String comment_img   =detail.getString("a_content_img");

                                    checkListener.onChecker(id,question_id,ask_user_image,ask_user_name,ask_city,ask_time,ask_content,counter,is_text,comment_img);


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



    public void get_answer(String ask_id){
        myProgressDialog.setMessage("Please wait ...");
        String requestUrl = appLinks.get_answer+"/"+ask_id;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    myProgressDialog.dismiss();
                    Log.d("BusinessListing",response.toString());
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<ForumDetailModel> list = new Gson().fromJson(response.toString(), new TypeToken<List<ForumDetailModel>>() {
                    }.getType());




                    forumDetailModelList.clear();
                    forumDetailModelList.addAll(list);

                    // refreshing recycler view
                    forumDetailAdapter.notifyDataSetChanged();

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
                    forum_add_attachment.setVisibility(View.GONE);
                    forum_send_help.setVisibility(View.GONE);
                    Forum_inputText.setVisibility(View.GONE);

                    forum_send_help_2.setVisibility(View.VISIBLE);
                    Forum_inputText_2.setVisibility(View.VISIBLE);



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
