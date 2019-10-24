package xyz.esuku.startupingnitex.Controller;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.SessionManager;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;
import xyz.esuku.startupingnitex.Util.StatusBar;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton login_btn;
    TextView create_account,forget_password,password_error,username_error;
    AppCompatEditText login_password,login_user_name;

    MyProgressDialog myProgressDialog;
    private SessionManager session;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidestatusBar();
        setContentView(R.layout.activity_login);


        userDb_helper   = new UserDb_Helper(this);
        myProgressDialog    = new MyProgressDialog(LoginActivity.this);
        // Session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        login_btn           = findViewById(R.id.login_btn);
        create_account      = findViewById(R.id.create_account);
        forget_password     = findViewById(R.id.forget_password);
        login_user_name     = findViewById(R.id.login_user_name);
        login_password      = findViewById(R.id.login_password);
        username_error      = findViewById(R.id.username_error);
        password_error      = findViewById(R.id.password_error);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name       = login_user_name.getText().toString().trim();
                String password        = login_password.getText().toString();

                if(user_name.isEmpty() || user_name.equals("")){
                    username_error.setVisibility(View.VISIBLE);
                }
                if(password.isEmpty() || password.equals("")){
                    password_error.setVisibility(View.VISIBLE);
                }
                if(!user_name.isEmpty()){
                    username_error.setVisibility(View.GONE);
                }
                if(!password.isEmpty()){
                    password_error.setVisibility(View.GONE);
                }

                //Now Authenticate
                if(!user_name.isEmpty() && !password.isEmpty()){
                    login_now(user_name,password);
                }
            }
        });



    }



    private void login_now(final String user_name, final String password){
        myProgressDialog.setMessage("Logging in ...");

        String requestUrl = appLinks.login_user;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("login",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);


                    if(result.getBoolean("status") != true){
                        //Toast.makeText(getApplicationContext(), "Will start SQLite NOw", Toast.LENGTH_SHORT).show();

                        userDb_helper.DeleteUserDB_n_otherTableifExist();

                        SQLiteDatabase db  = userDb_helper.getWritableDatabase();
                        ContentValues values    = new ContentValues();
                        values.put("user_id", result.getInt("user_id"));
                        values.put("user_name", result.getString("user_name"));
                        values.put("full_name", result.getString("full_name"));
                        values.put("phone_no", result.getString("phone_no"));
                        values.put("age", result.getString("age"));
                        values.put("sex", result.getString("sex"));
                        values.put("city", result.getString("city"));
                        values.put("business_name", result.getString("business_name"));
                        values.put("website_url", result.getString("website_url"));
                        values.put("user_image", result.getString("user_image"));
                        values.put("user_interest", result.getString("user_interest"));
                        values.put("user_experience", result.getString("user_experience"));
                        values.put("user_stage", result.getString("user_stage"));
                        values.put("email", result.getString("email"));
                        values.put("service_status", result.getString("service_status"));


                        Log.e("Content values", String.valueOf(values));

                        db.insert("userTbl", null, values);





                        session.setLogin(true);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    myProgressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                myProgressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("user_name", user_name);
                postMap.put("password", password);
                return postMap;
            }
        };
        //make the request to your server as indicated in your request url
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }


    public void login(final String user_name, final String password){
        Map<String, String> postMap = new HashMap<>();
        postMap.put("user_name", user_name);
        postMap.put("password", password);

        myProgressDialog.setMessage("Logging in ...");
        String requestUrl = appLinks.login_user;

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        myProgressDialog.dismiss();
                        try {
                            String status = null;
                            String msg =null;

                            if(response.has("Status")) {
                                status = response.getString("Status");
                                msg    = response.getString("Message");
                            }
                            else if (response.has("status")){
                                status = response.getString("status");
                                msg    = response.getString("message");
                            }

                            switch (status) {
                                case "fail": {


                                }
                                case "success": {

                                    userDb_helper.DeleteUserDB_n_otherTableifExist();

                                    SQLiteDatabase db  = userDb_helper.getWritableDatabase();
                                    ContentValues values    = new ContentValues();
                                    values.put("user_id", response.getInt("user_id"));
                                    values.put("user_name", response.getString("user_name"));
                                    values.put("full_name", response.getString("full_name"));
                                    values.put("phone_no", response.getString("phone_no"));
                                    values.put("age", response.getString("age"));
                                    values.put("sex", response.getString("sex"));
                                    values.put("city", response.getString("city"));
                                    values.put("business_name", response.getString("business_name"));
                                    values.put("website_url", response.getString("website_url"));
                                    values.put("user_image", response.getString("user_image"));
                                    values.put("user_interest", response.getString("user_interest"));
                                    values.put("user_experience", response.getString("user_experience"));
                                    values.put("user_stage", response.getString("user_stage"));
                                    values.put("email", response.getString("email"));


                                    Log.e("Content values", String.valueOf(values));

                                    db.insert("userTbl", null, values);





                                    session.setLogin(true);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                    break;
                                }
                                default:{


                                    break;
                                }
                            }


                        } catch (JSONException e) {


                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
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
    public void hidestatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }




}
