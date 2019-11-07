package xyz.esuku.startupingnitex.Controller.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.SwitchCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Controller.MainActivity;
import xyz.esuku.startupingnitex.Controller.SettingActivity;
import xyz.esuku.startupingnitex.Controller.UsersCategoryForm.Accelerator;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class Notification extends AppCompatActivity {


    SwitchCompat post_btn,tagging_btn,news_btn,res_btn,event_btn,msg_btn;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    String user_name;

    //String post_status,tag_status,news_status,res_status,event_status,msg_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        myProgressDialog    = new MyProgressDialog(Notification.this);
        userDbHelper = new UserDb_Helper(Notification.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name    = user.get("user_name");

        post_btn        = findViewById(R.id.post_btn);
        tagging_btn        = findViewById(R.id.tagging_btn);
        news_btn        = findViewById(R.id.news_btn);
        res_btn        = findViewById(R.id.res_btn);
        event_btn        = findViewById(R.id.event_btn);
        msg_btn        = findViewById(R.id.msg_btn);



        get_notification_setting();
        post_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String post_status;
                if (b){
                    post_status = "YES";
                }else {
                    post_status = "NO";
                }
                update_post_notification(post_status);
            }
        });
        tagging_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String tag_status;
                if (b){
                    tag_status = "YES";
                }else {
                    tag_status = "NO";
                }
                update_tag_notification(tag_status);
            }
        });
        news_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String news_status;
                if (b){
                    news_status = "YES";
                }else {
                    news_status = "NO";
                }
                update_news_notification(news_status);
            }
        });
        res_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String res_status;
                if (b){
                    res_status = "YES";
                }else {
                    res_status = "NO";
                }
                update_res_notification(res_status);
            }
        });
        event_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String event_status;
                if (b){
                    event_status = "YES";
                }else {
                    event_status = "NO";
                }
                update_event_notification(event_status);
            }
        });
        msg_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String msg_status;
                if (b){
                    msg_status = "YES";
                }else {
                    msg_status = "NO";
                }
                update_msg_notification(msg_status);
            }
        });
    }

    public void update_post_notification(String post_status){
        String requestUrl = appLinks.update_post_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        post_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        post_btn.setChecked(false);
                    }
                    //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", post_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    public void update_tag_notification(String tag_status){
        String requestUrl = appLinks.update_tag_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        tagging_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        tagging_btn.setChecked(false);
                    }
//                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", tag_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    public void update_news_notification(String news_status){
        String requestUrl = appLinks.update_news_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        news_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        news_btn.setChecked(false);
                    }
//                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", news_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    public void update_res_notification(String res_status){
        String requestUrl = appLinks.update_res_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        res_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        res_btn.setChecked(false);
                    }
//                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", res_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    public void update_event_notification(String event_status){
        String requestUrl = appLinks.update_event_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        event_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        event_btn.setChecked(false);
                    }
//                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", event_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }
    public void update_msg_notification(String msg_status){
        String requestUrl = appLinks.update_msg_notification_setting;
        //myProgressDialog.setMessage("Updating...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            //myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){
                    String setting      = result.getString("setting");
                    if(setting.equals("YES")){
                        msg_btn.setChecked(true);
                    }else if(setting.equals("NO")){
                        msg_btn.setChecked(false);
                    }
//                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                //myProgressDialog.dismiss();
                e.printStackTrace();
            }

        }, error -> {
            //myProgressDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("setting", msg_status);

                return postMap;
            }
        };


        req.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(req);
    }

    public void get_notification_setting(){
        String requestUrl = appLinks.get_notification_setting;
        myProgressDialog.setMessage("Please wait ...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            myProgressDialog.dismiss();



            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                if(status.equals("fail")){

                    final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Notification.this);
                    alertDialg.setTitle("Oops!");
                    alertDialg.setMessage("Could not fetch your preferred setting from database");
                    alertDialg.setCancelable(true);
//                    alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Notification.this, SettingActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                    alertDialg.show();
                }
                else if(status.equals("success")){
                    String d_post   = result.getString("post");
                    String d_tag   = result.getString("tag");
                    String d_news   = result.getString("news");
                    String d_res   = result.getString("res");
                    String d_event   = result.getString("event");
                    String d_msg   = result.getString("msg");

                    if(d_post.equals("YES")){
                        post_btn.setChecked(true);
                    }else if(d_post.equals("NO")){
                        post_btn.setChecked(false);
                    }

                    if(d_tag.equals("YES")){
                        tagging_btn.setChecked(true);
                    }else if(d_tag.equals("NO")){
                        tagging_btn.setChecked(false);
                    }

                    if(d_news.equals("YES")){
                        news_btn.setChecked(true);
                    }else if(d_news.equals("NO")){
                        news_btn.setChecked(false);
                    }

                    if(d_res.equals("YES")){
                        res_btn.setChecked(true);
                    }else if(d_res.equals("NO")){
                        res_btn.setChecked(false);
                    }

                    if(d_event.equals("YES")){
                        event_btn.setChecked(true);
                    }else if(d_event.equals("NO")){
                        event_btn.setChecked(false);
                    }

                    if(d_msg.equals("YES")){
                        msg_btn.setChecked(true);
                    }else if(d_msg.equals("NO")){
                        msg_btn.setChecked(false);
                    }


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
}
