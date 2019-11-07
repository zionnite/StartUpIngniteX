package xyz.esuku.startupingnitex.Controller.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    AppCompatEditText old_pass,new_pass,conf_new_pass;
    AppCompatButton send_btn;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        myProgressDialog    = new MyProgressDialog(ChangePassword.this);
        userDbHelper = new UserDb_Helper(ChangePassword.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name    = user.get("user_name");

        old_pass        = findViewById(R.id.old_pass);
        new_pass        = findViewById(R.id.new_pass);
        conf_new_pass        = findViewById(R.id.conf_new_pass);
        send_btn        = findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old_p        = old_pass.getText().toString().trim();
                String new_p        = new_pass.getText().toString().trim();
                String new_repass   = conf_new_pass.getText().toString().trim();

                old_pass.clearFocus();
                old_pass.setText("");

                new_pass.clearFocus();
                new_pass.setText("");

                conf_new_pass.clearFocus();
                conf_new_pass.setText("");


                old_pass.clearFocus();
                old_pass.setText("");

                new_pass.clearFocus();
                new_pass.setText("");

                conf_new_pass.clearFocus();
                conf_new_pass.setText("");

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(old_pass.getWindowToken(), 0);
                in.hideSoftInputFromWindow(new_pass.getWindowToken(), 0);
                in.hideSoftInputFromWindow(conf_new_pass.getWindowToken(), 0);

                change_password(old_p,new_p,new_repass);
            }
        });
    }

    public void change_password(String old_pass, String new_pass, String conf_newpass){


        String requestUrl = appLinks.change_password;
        myProgressDialog.setMessage("Please wait...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ChangePassword.this);
                    alertDialg.setTitle("Oops!");
                    alertDialg.setMessage(msg);
                    alertDialg.setCancelable(true);
                    alertDialg.show();
                }
                else if(status.equals("success")){

                    final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ChangePassword.this);
                    alertDialg.setTitle("Congratulation!");
                    alertDialg.setMessage(msg);
                    alertDialg.setCancelable(true);
                    alertDialg.show();
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
                postMap.put("old_pass",old_pass );
                postMap.put("new_pass", new_pass);
                postMap.put("conf_new_pass", conf_newpass);

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
