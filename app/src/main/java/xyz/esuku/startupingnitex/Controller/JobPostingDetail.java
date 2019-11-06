package xyz.esuku.startupingnitex.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Controller.UsersCategoryForm.Accelerator;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;
import java.util.Map;

public class JobPostingDetail extends AppCompatActivity {

    AppCompatButton apply_btn;
    ImageView back_btn;
    TextView title,company_name,location,experience,remote,category,description;

    Dialog prefDialog;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;
    String job_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_detail);

        userDb_helper   = new UserDb_Helper(JobPostingDetail.this);
        myProgressDialog    = new MyProgressDialog(JobPostingDetail.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");
        setInView();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobPostingDetail.this, JobPostingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefDialog  = new Dialog(JobPostingDetail.this);
                prefDialog.setContentView(R.layout.modal_posting_job);
                prefDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                prefDialog.setCancelable(false);
                prefDialog.show();

                TextView full_name_text              = prefDialog.findViewById(R.id.full_name_text);
                TextView full_name_error              = prefDialog.findViewById(R.id.full_name_error);

                TextView mobile_number_text              = prefDialog.findViewById(R.id.mobile_number_text);
                TextView mobile_number_error              = prefDialog.findViewById(R.id.mobile_number_error);

                TextView email_text              = prefDialog.findViewById(R.id.email_text);
                TextView email_error              = prefDialog.findViewById(R.id.email_error);

                TextView linkedin_text              = prefDialog.findViewById(R.id.linkedin_text);
                TextView linkedin_error              = prefDialog.findViewById(R.id.linkedin_error);

                TextView website_text              = prefDialog.findViewById(R.id.website_text);
                TextView website_error              = prefDialog.findViewById(R.id.website_error);


                AppCompatButton cancel_btn           = prefDialog.findViewById(R.id.cancel_btn);
                AppCompatButton submit           = prefDialog.findViewById(R.id.apply_btn);

                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prefDialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String FullName = full_name_text.getText().toString().trim();
                        if(FullName.isEmpty() || FullName.equals("")){
                            full_name_error.setVisibility(View.VISIBLE);
                        }
                        if(!FullName.isEmpty()){
                            full_name_error.setVisibility(View.GONE);
                        }

                        String MobileNumber     = mobile_number_text.getText().toString().trim();
                        if(MobileNumber.isEmpty() || MobileNumber.equals("")){
                            mobile_number_error.setVisibility(View.VISIBLE);
                        }
                        if(!MobileNumber.isEmpty()){
                            mobile_number_error.setVisibility(View.GONE);
                        }

                        String Email     = email_text.getText().toString().trim();
                        if(Email.isEmpty() || Email.equals("")){
                            email_error.setVisibility(View.VISIBLE);
                        }
                        if(!Email.isEmpty()){
                            email_error.setVisibility(View.GONE);
                        }

                        String Linkedi     = linkedin_text.getText().toString().trim();
                        if(Linkedi == null || Linkedi.equals("")){
                            Linkedi ="null";
                        }
//                        String Linkedi     = linkedin_text.getText().toString().trim();
//                        if(Linkedi.isEmpty() || Linkedi.equals("")){
//                            linkedin_error.setVisibility(View.VISIBLE);
//                        }
//                        if(!Linkedi.isEmpty()){
//                            linkedin_error.setVisibility(View.GONE);
//                        }

                        String Website     = website_text.getText().toString().trim();
                        if(Website.isEmpty() || Website.equals("")){
                            website_error.setVisibility(View.VISIBLE);
                        }
                        if(!Website.isEmpty()){
                            website_error.setVisibility(View.GONE);
                        }

                        if(!FullName.isEmpty() && !MobileNumber.isEmpty() && !Email.isEmpty() && !Linkedi.isEmpty() && !Website.isEmpty()){
                            applyNow(FullName,MobileNumber,Email,Linkedi,Website, user_name);
                        }
                    }
                });
            }
        });

    }

    private void applyNow(String fullName, String mobileNumber, String email, String linkedi, String website, String user_name) {
        myProgressDialog.setMessage("Updating ...");

        String requestUrl = appLinks.apply_job;

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

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(JobPostingDetail.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(JobPostingDetail.this, JobPostingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        alertDialg.show();
                    }else if(status.equals("success")){

                        String job_status       = result.getString("job_status");
                        if(job_status.equals("true")){
                            apply_btn.setBackgroundResource(R.color.colorGray);
                        }

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(JobPostingDetail.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
                        alertDialg.setCancelable(false);
                        alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(JobPostingDetail.this, JobPostingActivity.class);
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
                postMap.put("full_name", fullName);
                postMap.put("phone", mobileNumber);
                postMap.put("email", email);
                postMap.put("linkdin", linkedi);
                postMap.put("website", website);
                postMap.put("job_id", job_id);
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

    private void setInView(){

        back_btn       = findViewById(R.id.back_btn);
        apply_btn       = findViewById(R.id.apply_btn);
        title       = findViewById(R.id.title);
        company_name       = findViewById(R.id.company_name);
        location       = findViewById(R.id.location);
        experience       = findViewById(R.id.experience);
        remote       = findViewById(R.id.remote);
        category       = findViewById(R.id.category);
        description       = findViewById(R.id.description);

        String job_poster    = getIntent().getExtras().getString("job_poster");
        job_id    = getIntent().getExtras().getString("job_id");
        String job_title    = getIntent().getExtras().getString("job_title");
        String job_comp    = getIntent().getExtras().getString("job_comp");
        String job_desc    = getIntent().getExtras().getString("job_desc");
        String job_cat    = getIntent().getExtras().getString("job_cat");
        String job_compensate    = getIntent().getExtras().getString("job_compensate");
        String job_exp    = getIntent().getExtras().getString("job_exp");
        String job_remote    = getIntent().getExtras().getString("job_remote");
        String job_location    = getIntent().getExtras().getString("job_location");
        String job_status    = getIntent().getExtras().getString("job_status");

        if(job_status.equals("true")){
            apply_btn.setBackgroundResource(R.color.colorGray);
            apply_btn.setEnabled(false);
        }

        title.setText(job_title);
        company_name.setText(job_comp);
        description.setText(job_desc);
        location.setText(job_location);
        experience.setText(job_exp);
        category.setText(job_cat);
        remote.setText(job_remote);
    }
}
