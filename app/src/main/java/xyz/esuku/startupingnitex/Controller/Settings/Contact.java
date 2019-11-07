package xyz.esuku.startupingnitex.Controller.Settings;

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

public class Contact extends AppCompatActivity {

    AppCompatEditText msg_title,msg_body;
    AppCompatButton send_btn;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        myProgressDialog    = new MyProgressDialog(Contact.this);
        userDbHelper = new UserDb_Helper(Contact.this);
        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name    = user.get("user_name");

        msg_title       = findViewById(R.id.msg_title);
        msg_body       = findViewById(R.id.msg_body);
        send_btn       = findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title    = msg_title.getText().toString().trim();
                String body    = msg_body.getText().toString().trim();

                msg_title.clearFocus();
                msg_title.setText("");

                msg_body.clearFocus();
                msg_body.setText("");

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(msg_title.getWindowToken(), 0);
                in.hideSoftInputFromWindow(msg_body.getWindowToken(), 0);

                contact_admin(title,body);
            }
        });
    }
    public void contact_admin(String msg_title, String msg_body){


        String requestUrl = appLinks.contact_us;
        myProgressDialog.setMessage("Please wait...");

        StringRequest req   = new StringRequest(Request.Method.POST, requestUrl, response -> {
            myProgressDialog.dismiss();
            try {
                JSONObject result   = new JSONObject(response);
                String status   = result.getString("status");
                String msg   = result.getString("msg");
                if(status.equals("fail")){
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(status.equals("success")){

                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
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
                postMap.put("msg_title", msg_title);
                postMap.put("msg_body", msg_body);

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
