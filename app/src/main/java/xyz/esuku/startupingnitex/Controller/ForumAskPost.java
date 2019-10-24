package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class ForumAskPost extends AppCompatActivity {

    ImageView back_btn;
    AppCompatButton ask_question_btn;
    AppCompatEditText ask_question;
    TextView ad_text;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ask_post);

        myProgressDialog    = new MyProgressDialog(ForumAskPost.this);
        userDbHelper = new UserDb_Helper(ForumAskPost.this);


        back_btn    = findViewById(R.id.back_btn);
        ask_question_btn    = findViewById(R.id.ask_question_btn);
        ask_question    = findViewById(R.id.ask_question);
        ad_text    = findViewById(R.id.ad_text);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumAskPost.this, Forum.class);
                startActivity(intent);
            }
        });

        ask_question_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionAsk  = ask_question.getText().toString().trim();
                submitQuestion(questionAsk);
            }
        });
        ad_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Advert Position not Available in your Locality",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void submitQuestion(String content) {
        ask_question.clearFocus();
        ask_question.setText("");

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(ask_question.getWindowToken(), 0);
        //...perform search

        String requestUrl = appLinks.post_question;
        myProgressDialog.setMessage("Please wait...");

        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        Map<String, String> postMap = new HashMap<>();
        postMap.put("content", content);
        postMap.put("user_name", user_name);


        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();


                    try {
                        String status   = response.getString("status");
                        String msg   = response.getString("msg");
                        Toast.makeText(ForumAskPost.this, msg, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ForumAskPost.this, Forum.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                }, error -> {

            myProgressDialog.dismiss();
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

}
