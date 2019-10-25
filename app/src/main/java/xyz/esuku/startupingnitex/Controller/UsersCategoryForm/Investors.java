package xyz.esuku.startupingnitex.Controller.UsersCategoryForm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
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

public class Investors extends AppCompatActivity {

    ImageView back_btn;

    NiceSpinner specialty_spinner;
    List<String> dataset;

    AppCompatEditText business_name_text,business_address_text,service_rendering_text,work_email_text,work_phone_text;

    AppCompatCheckBox ideal,seed,post,series;

    TextView business_name_error,business_address_error,service_rendering_error,charges_error,specialty_error,work_email_error,work_phone_error,business_logo_error;

    AppCompatButton select_business_log,Save;

    String selected_specialty =null;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;

    String m_ideal_stage,m_seed_fund,m_post_seed,m_series;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investors);
        userDb_helper   = new UserDb_Helper(Investors.this);
        myProgressDialog    = new MyProgressDialog(Investors.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");


        business_name_text          = findViewById(R.id.business_name_text);
        business_name_error          = findViewById(R.id.business_name_error);


        business_address_text          = findViewById(R.id.business_address_text);
        business_address_error          = findViewById(R.id.business_address_error);

        service_rendering_text          = findViewById(R.id.service_rendering_text);
        service_rendering_error          = findViewById(R.id.service_rendering_error);



        specialty_spinner          = findViewById(R.id.specialty_spinner);
        specialty_error          = findViewById(R.id.specialty_error);

        work_email_text          = findViewById(R.id.work_email_text);
        work_email_error          = findViewById(R.id.work_email_error);

        work_phone_text          = findViewById(R.id.work_phone_text);
        work_phone_error          = findViewById(R.id.work_phone_error);

        ideal          = findViewById(R.id.ideal);
        seed          = findViewById(R.id.seed);
        post          = findViewById(R.id.post);
        series          = findViewById(R.id.series);



        back_btn          = findViewById(R.id.back_btn);
        Save          = findViewById(R.id.Save);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Investors.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        specialty_spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            //Toast.makeText(GrantActivity.this, item, Toast.LENGTH_SHORT).show();
            selected_specialty  = item;
            Log.d("Grant ID", String.valueOf(id)+" POSITION "+String.valueOf(position)+ "NAME "+item);
        });

        ideal.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b){

                m_ideal_stage   ="yes";

            }else {

                m_ideal_stage  ="no";

            }
        });
        seed.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b){

                m_seed_fund   ="yes";

            }else {

                m_seed_fund  ="no";

            }
        });
        post.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b){

                m_post_seed   ="yes";

            }else {

                m_post_seed  ="no";

            }
        });
        series.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b){

                m_series   ="yes";

            }else {

                m_series  ="no";

            }
        });


        Save.setOnClickListener(v -> {

            String bussines_name         = business_name_text.getText().toString().trim();
            if(bussines_name.isEmpty() || bussines_name.equals("")){
                business_name_error.setVisibility(View.VISIBLE);
            }
            if(!bussines_name.isEmpty()){
                business_name_error.setVisibility(View.GONE);
            }

            String bussines_add         = business_address_text.getText().toString().trim();
            if(bussines_add.isEmpty() || bussines_add.equals("")){
                business_address_error.setVisibility(View.VISIBLE);
            }
            if(!bussines_add.isEmpty()){
                business_address_error.setVisibility(View.GONE);
            }

            String services         = service_rendering_text.getText().toString().trim();
            if(services.isEmpty() || services.equals("")){
                service_rendering_error.setVisibility(View.VISIBLE);
            }
            if(!services.isEmpty()){
                service_rendering_error.setVisibility(View.GONE);
            }



            if(selected_specialty == null || selected_specialty.equals("")){
                specialty_error.setVisibility(View.VISIBLE);
            }
            if(selected_specialty != null){
                specialty_error.setVisibility(View.GONE);
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



            if(m_ideal_stage == null || m_ideal_stage.equals("")){
                m_ideal_stage = "no";
            }
            if(m_seed_fund == null || m_seed_fund.equals("")){
                m_seed_fund = "no";
            }
            if(m_post_seed == null || m_post_seed.equals("")){
                m_post_seed = "no";
            }
            if(m_series == null || m_series.equals("")){
                m_series = "no";
            }


            if(!bussines_name.isEmpty() && !bussines_add.isEmpty() && !services.isEmpty() && selected_specialty != null && !email.isEmpty() && !phone.isEmpty()){
                update_profile_setting(bussines_name,bussines_add,services,selected_specialty,email,phone,m_ideal_stage,m_seed_fund,m_post_seed,m_series);
            }

        });


        get_list_of_investor_interest_list();



    }

    private void update_profile_setting(String full_name, String bussines_add, String services, String selected_specialty, String email,
                                        String phone,String ideal_stage, String seed_fund, String post_seed, String series_a) {

        myProgressDialog.setMessage("Updating ...");

        String requestUrl = appLinks.update_investor_profile;

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

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Investors.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Investors.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialg.show();
                    }else if(status.equals("success")){

                        String service_type       = result.getString("service_type");
                        String service_status       = result.getString("service_type");
                        userDb_helper.update_service_stauts(user_name,service_status,service_type);

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Investors.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Investors.this, MainActivity.class);
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
                postMap.put("full_name", full_name);
                postMap.put("business_address", bussines_add);
                postMap.put("services", services);
                postMap.put("interest", selected_specialty);
                postMap.put("email", email);
                postMap.put("phone", phone);

                postMap.put("ideal_stage", ideal_stage);
                postMap.put("seed_fund", seed_fund);
                postMap.put("post_seed", post_seed);
                postMap.put("series_stage", series_a);


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


    private void get_list_of_investor_interest_list(){
        myProgressDialog.setMessage("Please wait...");

        String requestUrl = appLinks.get_investor_interest_list;

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

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Investors.this);
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
                        specialty_spinner.attachDataSource(dataset);

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
