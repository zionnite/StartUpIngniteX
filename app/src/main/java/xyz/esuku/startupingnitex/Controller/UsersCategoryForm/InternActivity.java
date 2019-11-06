package xyz.esuku.startupingnitex.Controller.UsersCategoryForm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Controller.MainActivity;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class InternActivity extends AppCompatActivity {

    NiceSpinner interest_spinner;
    List<String> dataset;

    ImageView back_btn;

    AppCompatEditText aspire_business_name_text,expertise_text,week_to_start_text,previous_experience_text,name_of_contact_text,
            work_email_text,work_phone_text,full_name_text,city_text;

    TextView businessName_error,expertise_error,interest_error,week_to_start_error,previous_experience_error,name_of_contact_error,
            work_email_error,work_phone_error,full_name_error,city_error,mentor_error;

    AppCompatButton Save;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name,selected_interest;

    RadioGroup mentor_radioGroup;
    AppCompatRadioButton mentor_radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern);
        userDb_helper   = new UserDb_Helper(InternActivity.this);
        myProgressDialog    = new MyProgressDialog(InternActivity.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");

        full_name_text           = findViewById(R.id.full_name_text);
        full_name_error                  = findViewById(R.id.full_name_error);



        interest_spinner                       = findViewById(R.id.interest_spinner);
        interest_error                      = findViewById(R.id.interest_error);


        work_email_text                     = findViewById(R.id.work_email_text);
        work_email_error                    = findViewById(R.id.work_email_error);

        work_phone_text                     = findViewById(R.id.work_phone_text);
        work_phone_error                    = findViewById(R.id.work_phone_error);

        city_text                     = findViewById(R.id.city_text);
        city_error                    = findViewById(R.id.city_error);

        mentor_radioGroup                    = findViewById(R.id.mentor_radioGroup);
        mentor_error                         = findViewById(R.id.mentor_error);


        Save                    = findViewById(R.id.Save);
        back_btn                    = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(InternActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        interest_spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            if(item.equals("Select")){
                selected_interest  = null;
                Toast.makeText(InternActivity.this, "Selected value not accepted", Toast.LENGTH_LONG).show();
            }
            else if (!item.equals("Select")){
                selected_interest  = item;
            }

            Log.d("Grant ID", String.valueOf(id)+" POSITION "+String.valueOf(position)+ "NAME "+item);
        });
        Save.setOnClickListener(v -> {
            //Validate empty input

            String full_name         = full_name_text.getText().toString().trim();
            if(full_name.isEmpty() || full_name.equals("")){
                full_name_error.setVisibility(View.VISIBLE);
            }
            if(!full_name.isEmpty()){
                full_name_error.setVisibility(View.GONE);
            }



            String interest         = selected_interest;
            if(interest == null || interest.equals("")){
                interest_error.setVisibility(View.VISIBLE);
            }
            if(interest !=null){
                interest_error.setVisibility(View.GONE);
            }





            String email         = work_email_text.getText().toString().trim();
            if(email.isEmpty() || email.equals("")){
                work_email_error.setVisibility(View.VISIBLE);
            }
            if(!email.isEmpty()){
                work_email_error.setVisibility(View.GONE);
            }

            String phone         = work_phone_text.getText().toString().trim();
            if(phone.isEmpty() || phone.equals("")){
                work_phone_error.setVisibility(View.VISIBLE);
            }
            if(!phone.isEmpty()){
                work_phone_error.setVisibility(View.GONE);
            }

            String city         = city_text.getText().toString().trim();
            if(city.isEmpty() || city.equals("")){
                city_error.setVisibility(View.VISIBLE);
            }
            if(!city.isEmpty()){
                city_error.setVisibility(View.GONE);
            }

            int selectedMentor  = mentor_radioGroup.getCheckedRadioButtonId();
            mentor_radioButton  = findViewById(selectedMentor);
            String  mentor = null;


            if(mentor_radioButton == null || mentor_radioButton.equals("")){
                mentor_error.setVisibility(View.VISIBLE);
            }
            if(mentor_radioButton !=null){
                mentor_error.setVisibility(View.GONE);
                mentor      = mentor_radioButton.getText().toString().trim();
            }





            if(!interest.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !full_name.isEmpty() && !city.isEmpty() && mentor !=null){

                update_profile_setting(interest,email,phone,full_name, city, mentor);

            }
        });

        get_list_of_department();
    }

    private void update_profile_setting(String interest, String email, String phone, String full_name, String city, String mentor) {
        myProgressDialog.setMessage("Updating, wait ...");

        String requestUrl = appLinks.update_aspiring_profile;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("login",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);

                    String status   = result.getString("status");
                    String msg      = result.getString("msg");
                    if(status.equals("fail")){

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(InternActivity.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(InternActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialg.show();
                    }else if(status.equals("success")){

                        String service_type       = result.getString("service_type");
                        String service_status       = result.getString("service_type");
                        userDb_helper.update_service_stauts(user_name,service_status,service_type);

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(InternActivity.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(InternActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialg.show();

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
                postMap.put("interest", interest);
                postMap.put("email", email);
                postMap.put("phone", phone);
                postMap.put("full_name", full_name);
                postMap.put("city", city);
                postMap.put("mentor", mentor);
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

    private void get_list_of_department(){
        myProgressDialog.setMessage("Please wait...");

        String requestUrl = appLinks.get_apiring_entraprenuar_interest_list;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("login",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);

                    String status   = result.getString("status");
                    String msg      = result.getString("msg");
                    if(status.equals("fail")){

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(InternActivity.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();
                    }else if(status.equals("success")){
                        List<String> arrayList = new ArrayList<>();
                        arrayList.add("Select");

                        JSONArray detailArry   = result.getJSONArray("detail");
                        for(int i=0; i < detailArry.length(); i++){
                            JSONObject detailObj = detailArry.getJSONObject(i);

                            String specialty_name        = detailObj.getString("interest_name");
                            arrayList.add(specialty_name);
                        }


                        dataset = new LinkedList<>(arrayList);
                        interest_spinner.attachDataSource(dataset);

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
}
