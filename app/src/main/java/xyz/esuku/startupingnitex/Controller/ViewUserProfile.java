package xyz.esuku.startupingnitex.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
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

public class ViewUserProfile extends AppCompatActivity {


    ImageView back_btn;

    CircleImageView profile_image;

    CardView other_profile_CardView;

    TextView fullname_text,phone_text,email_text,city_text,website_text,linkedin_text,business_name_text,business_phone_text,business_email_text,
            busienss_address_text,business_service_text,business_charges_text,expertise_text,interest_text,week_to_start_text,previous_text,
            contact_person_text,department_text,other_profile,business_special_text,user_profile_name;

    LinearLayout busname_LinearLayout,phone_LinearLayout,email_LinearLayout,buss_add_LinearLayout,service_LinearLayout,charges_LinearLayout,special_LinearLayout,
                 expertise_LinearLayout,interest_LinearLayout,week_LinearLayout,previous_LinearLayout,person_LinearLayout,department_LinearLayout;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);
        userDb_helper   = new UserDb_Helper(ViewUserProfile.this);
        myProgressDialog    = new MyProgressDialog(ViewUserProfile.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");

        init();

        String dis_user_name    = getIntent().getExtras().getString("user_name");
        get_user(dis_user_name);


    }

    private void init(){
        fullname_text               = findViewById(R.id.fullname_text);
        phone_text                  = findViewById(R.id.phone_text);
        email_text                  = findViewById(R.id.email_text);
        city_text                   = findViewById(R.id.city_text);
        website_text                = findViewById(R.id.website_text);
        linkedin_text               = findViewById(R.id.linkedin_text);
        business_name_text          = findViewById(R.id.business_name_text);
        business_phone_text         = findViewById(R.id.business_phone_text);
        business_email_text         = findViewById(R.id.business_email_text);
        busienss_address_text       = findViewById(R.id.busienss_address_text);
        business_service_text       = findViewById(R.id.business_service_text);
        business_charges_text       = findViewById(R.id.business_charges_text);
        expertise_text              = findViewById(R.id.expertise_text);
        interest_text               = findViewById(R.id.interest_text);
        week_to_start_text          = findViewById(R.id.week_to_start_text);
        previous_text               = findViewById(R.id.previous_text);
        contact_person_text         = findViewById(R.id.contact_person_text);
        department_text             = findViewById(R.id.department_text);
        other_profile               = findViewById(R.id.other_profile);
        other_profile_CardView      = findViewById(R.id.other_profile_CardView);
        busname_LinearLayout        = findViewById(R.id.busname_LinearLayout);
        phone_LinearLayout        = findViewById(R.id.phone_LinearLayout);
        email_LinearLayout        = findViewById(R.id.email_LinearLayout);
        buss_add_LinearLayout        = findViewById(R.id.buss_add_LinearLayout);
        service_LinearLayout        = findViewById(R.id.service_LinearLayout);
        charges_LinearLayout        = findViewById(R.id.charges_LinearLayout);
        expertise_LinearLayout        = findViewById(R.id.expertise_LinearLayout);
        interest_LinearLayout        = findViewById(R.id.interest_LinearLayout);
        week_LinearLayout        = findViewById(R.id.week_LinearLayout);
        previous_LinearLayout        = findViewById(R.id.previous_LinearLayout);
        person_LinearLayout        = findViewById(R.id.person_LinearLayout);
        department_LinearLayout        = findViewById(R.id.department_LinearLayout);
        business_special_text        = findViewById(R.id.business_special_text);
        special_LinearLayout        = findViewById(R.id.special_LinearLayout);
        user_profile_name        = findViewById(R.id.user_profile_name);
        profile_image        = findViewById(R.id.profile_image);
    }

    private void get_user(String dis_user_name) {

        myProgressDialog.setMessage("Please wait ...");

        String requestUrl = appLinks.view_user_profile;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("login",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);

                    String status   = result.getString("status");

                    if(status.equals("fail")){

                        String msg      = result.getString("msg");
                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ViewUserProfile.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();
                    }else if(status.equals("success")){
                        JSONObject basic_detail     = result.getJSONObject("basic_detail");
                        String user_name            = basic_detail.getString("user_name");
                        String user_img            = basic_detail.getString("user_img");
                        String full_name            = basic_detail.getString("full_name");
                        String sex            = basic_detail.getString("sex");
                        String age            = basic_detail.getString("age");
                        String user_status            = basic_detail.getString("status");
                        String phone            = basic_detail.getString("phone");
                        String email            = basic_detail.getString("email");
                        String address            = basic_detail.getString("address");
                        String city            = basic_detail.getString("city");
                        String state            = basic_detail.getString("state");
                        String website            = basic_detail.getString("website");
                        String service_status            = basic_detail.getString("service_status");

                        fullname_text.setText(full_name);
                        phone_text.setText(phone);
                        email_text.setText(email);
                        city_text.setText(city);
                        website_text.setText(website);
                        user_profile_name.setText(user_name);

                        RequestOptions options = new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round);

                        Glide.with(getApplicationContext()).load(user_img).apply(options).into(profile_image);

                        if(service_status.equals("set")){
                            other_profile.setVisibility(View.VISIBLE);
                            other_profile_CardView.setVisibility(View.VISIBLE);

                            JSONObject work_detail      = result.getJSONObject("work_detail");
                            String bus_name             = work_detail.getString("bus_name");
                            String bus_phone             = work_detail.getString("bus_phone");
                            String bus_email             = work_detail.getString("bus_email");
                            String bus_add             = work_detail.getString("bus_add");
                            String bus_service             = work_detail.getString("bus_service");
                            String bus_charges             = work_detail.getString("bus_charges");
                            String bus_specialty             = work_detail.getString("bus_specialty");
                            String expertise             = work_detail.getString("expertise");
                            String interest             = work_detail.getString("interest");
                            String weeks_to_start             = work_detail.getString("weeks_to_start");
                            String previous_exp             = work_detail.getString("previous_exp");
                            String name_of_person             = work_detail.getString("name_of_person");
                            String department             = work_detail.getString("department");

                            if(!bus_name.isEmpty()){
                                busname_LinearLayout.setVisibility(View.VISIBLE);
                                business_name_text.setText(bus_name);
                            }

                            if(!bus_phone.isEmpty()){
                                phone_LinearLayout.setVisibility(View.VISIBLE);
                                business_phone_text.setText(bus_phone);
                            }

                            if(!bus_email.isEmpty()){
                                email_LinearLayout.setVisibility(View.VISIBLE);
                                business_email_text.setText(bus_email);
                            }

                            if(!bus_add.isEmpty()){
                                buss_add_LinearLayout.setVisibility(View.VISIBLE);
                                busienss_address_text.setText(bus_add);
                            }

                            if(!bus_service.isEmpty()){
                                service_LinearLayout.setVisibility(View.VISIBLE);
                                business_service_text.setText(bus_service);
                            }

                            if(!bus_charges.isEmpty()){
                                charges_LinearLayout.setVisibility(View.VISIBLE);
                                business_charges_text.setText(bus_charges);
                            }

                            if(!bus_specialty.isEmpty()){
                                special_LinearLayout.setVisibility(View.VISIBLE);
                                business_special_text.setText(bus_specialty);
                            }

                            if(!expertise.isEmpty()){
                                expertise_LinearLayout.setVisibility(View.VISIBLE);
                                expertise_text.setText(expertise);
                            }

                            if(!interest.isEmpty()){
                                interest_LinearLayout.setVisibility(View.VISIBLE);
                                interest_text.setText(interest);
                            }

                            if(!weeks_to_start.isEmpty()){
                                week_LinearLayout.setVisibility(View.VISIBLE);
                                week_to_start_text.setText(weeks_to_start);
                            }

                            if(!previous_exp.isEmpty()){
                                previous_LinearLayout.setVisibility(View.VISIBLE);
                                previous_text.setText(previous_exp);
                            }

                            if(!name_of_person.isEmpty()){
                                person_LinearLayout.setVisibility(View.VISIBLE);
                                contact_person_text.setText(name_of_person);
                            }

                            if(!department.isEmpty()){
                                department_LinearLayout.setVisibility(View.VISIBLE);
                                department_text.setText(department);
                            }
                        }
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
                postMap.put("user_name", dis_user_name);
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
