package xyz.esuku.startupingnitex.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    AppCompatButton signup_btn;
    TextView fullName_error,phoneNo_error,email_error,username_error,password_error,login_account,signu_forget_password,fullName_short,phoneNo_short,email_short,username_short,password_short;
    AppCompatEditText signup_fullName,signup_phoneNo,signup_email,signup_user_name,signup_password;


    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidestatusBar();
        setContentView(R.layout.activity_sign_up);

        myProgressDialog    = new MyProgressDialog(SignUpActivity.this);

        signup_fullName         = findViewById(R.id.signup_fullName);
        signup_phoneNo          = findViewById(R.id.signup_phoneNo);
        signup_email            = findViewById(R.id.signup_email);
        signup_user_name        = findViewById(R.id.signup_user_name);
        signup_password         = findViewById(R.id.signup_password);

        fullName_error          = findViewById(R.id.fullName_error);
        phoneNo_error           = findViewById(R.id.phoneNo_error);
        email_error             = findViewById(R.id.email_error);
        username_error          = findViewById(R.id.username_error);
        password_error          = findViewById(R.id.password_error);

        fullName_short          = findViewById(R.id.fullName_short);
        phoneNo_short           = findViewById(R.id.phoneNo_short);
        email_short             = findViewById(R.id.email_short);
        username_short          = findViewById(R.id.username_short);
        password_short          = findViewById(R.id.password_short);

        login_account           = findViewById(R.id.login_account);
        signu_forget_password   = findViewById(R.id.signu_forget_password);
        signup_btn              = findViewById(R.id.signup_btn);


        login_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signu_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name       = signup_fullName.getText().toString().trim();
                String phone_no        = signup_phoneNo.getText().toString().trim();
                String email           = signup_email.getText().toString().trim();
                String user_name       = signup_user_name.getText().toString().trim();
                String password        = signup_password.getText().toString();

                if(full_name.isEmpty() || full_name.equals("") || full_name.length() <= 3){
                    if(full_name.isEmpty() || full_name.equals("")){
                        fullName_error.setVisibility(View.VISIBLE);
                    }else if (full_name.length() <= 3){
                        fullName_short.setVisibility(View.VISIBLE);
                    }

                }
                if(phone_no.isEmpty() || phone_no.equals("") || phone_no.length() <= 10){
                    if(phone_no.isEmpty() || phone_no.equals("")){
                        phoneNo_error.setVisibility(View.VISIBLE);
                    }else if (phone_no.length() <= 10){
                        phoneNo_short.setVisibility(View.VISIBLE);
                    }
                }
                if(email.isEmpty() || email.equals("") || email.length() <= 3){
                    if(email.isEmpty() || email.equals("")){
                        email_error.setVisibility(View.VISIBLE);
                    }else if (email.length() <= 3){
                        email_short.setVisibility(View.VISIBLE);
                    }
                }
                if(user_name.isEmpty() || user_name.equals("") || user_name.length() <= 3){
                    if(user_name.isEmpty() || user_name.equals("")){
                        username_error.setVisibility(View.VISIBLE);
                    }else if (user_name.length() <= 3){
                        username_short.setVisibility(View.VISIBLE);
                    }
                }
                if(password.isEmpty() || password.equals("") || password.length() <= 5){
                    if(password.isEmpty() || password.equals("")){
                        password_error.setVisibility(View.VISIBLE);
                    }else if (password.length() <= 5){
                        password_short.setVisibility(View.VISIBLE);
                    }
                }






                //Reset the validate empty
                if(!full_name.isEmpty()){
                    fullName_error.setVisibility(View.GONE);
                }
                if(!phone_no.isEmpty()){
                    phoneNo_error.setVisibility(View.GONE);
                }
                if(!email.isEmpty()){
                    email_error.setVisibility(View.GONE);
                }
                if(!user_name.isEmpty()){
                    username_error.setVisibility(View.GONE);
                }
                if(!password.isEmpty()){
                    password_error.setVisibility(View.GONE);
                }

                //Reset the validate short
                if(full_name.length() >= 4){
                    fullName_short.setVisibility(View.GONE);
                }
                if(phone_no.length() >= 11){
                    phoneNo_short.setVisibility(View.GONE);
                }

                if(email.length() >= 4){
                    email_short.setVisibility(View.GONE);
                }
                if(user_name.length() >= 4){
                    username_short.setVisibility(View.GONE);
                }
                if(password.length() >= 6){
                    password_short.setVisibility(View.GONE);
                }


                //Now Authenticate
                if(!full_name.isEmpty() && !phone_no.isEmpty() && !email.isEmpty() && !user_name.isEmpty() && !password.isEmpty()
                    && full_name.length() >=4 && phone_no.length() >=11 && email.length() >=4 && user_name.length() >=4 && password.length() >=6
                ){
                    signupUser(full_name,phone_no,email,user_name,password);
                }
            }
        });


    }

    private void signupUser(final String full_name, String phone_no, final String email, final String user_name, final String password) {
        myProgressDialog.setMessage("Registering...");

        String requestUrl = appLinks.add_user;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("login",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);


                    if(result.getString("status").equals("success")){


                        signup_fullName.setText("");
                        signup_phoneNo.setText("");
                        signup_email.setText("");
                        signup_user_name.setText("");
                        signup_password.setText("");

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(SignUpActivity.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage("Registration is successful");
                        alertDialg.show();


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
                postMap.put("full_name", full_name);
                postMap.put("email", email);
                postMap.put("phone", phone_no);
                postMap.put("user_name", user_name);
                postMap.put("password", password);
                return postMap;
            }
        };
        //make the request to your server as indicated in your request url
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }


    public void hidestatusBar(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
