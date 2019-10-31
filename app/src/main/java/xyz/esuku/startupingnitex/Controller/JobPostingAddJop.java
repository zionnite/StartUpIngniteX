package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.*;

public class JobPostingAddJop extends AppCompatActivity {

    NiceSpinner job_category_spinner,job_experience_spinner;
    List<String> dataset_1,dataset_2;

    AppCompatEditText job_title_text,job_desc_text,job_company_text,job_location_text,job_compensation_text;
    TextView job_title_error,job_desc_error,job_company_error,job_location_error,job_remote_error,job_category_error,job_experience_error,job_compensation_error;

    AppCompatButton make_post;
    RadioGroup job_remote_radioGroup;
    AppCompatRadioButton jobRemoteRadio;

    String category_selector,experience_selector =null;

    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_add_jop);
        myProgressDialog    = new MyProgressDialog(JobPostingAddJop.this);
        userDbHelper = new UserDb_Helper(JobPostingAddJop.this);

        init();

        job_category_spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            if(position != 0){
                category_selector  = item;
            }else{
                category_selector = null;
            }

        });
        job_experience_spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            if(position != 0){
                experience_selector  = item;
            }else{
                experience_selector = null;
            }

        });

        make_post.setOnClickListener(v->{

            String job_title         = job_title_text.getText().toString().trim();
            if(job_title.isEmpty() || job_title.equals("")){
                job_title_error.setVisibility(View.VISIBLE);
            }
            if(!job_title.isEmpty()){
                job_title_error.setVisibility(View.GONE);
            }

            String job_desc         = job_desc_text.getText().toString().trim();
            if(job_desc.isEmpty() || job_desc.equals("")){
                job_desc_error.setVisibility(View.VISIBLE);
            }
            if(!job_desc.isEmpty()){
                job_desc_error.setVisibility(View.GONE);
            }

            String job_company         = job_company_text.getText().toString().trim();
            if(job_company.isEmpty() || job_company.equals("")){
                job_company_error.setVisibility(View.VISIBLE);
            }
            if(!job_company.isEmpty()){
                job_company_error.setVisibility(View.GONE);
            }

            String job_location         = job_location_text.getText().toString().trim();
            if(job_location.isEmpty() || job_location.equals("")){
                job_location_error.setVisibility(View.VISIBLE);
            }
            if(!job_location.isEmpty()){
                job_location_error.setVisibility(View.GONE);
            }

            int selectedCat  = job_remote_radioGroup.getCheckedRadioButtonId();
            jobRemoteRadio  = findViewById(selectedCat);
            String  remoteCat = null;


            if(jobRemoteRadio == null || jobRemoteRadio.equals("")){
                job_remote_error.setVisibility(View.VISIBLE);
            }
            if(jobRemoteRadio !=null){
                job_remote_error.setVisibility(View.GONE);
                remoteCat      = jobRemoteRadio.getText().toString().trim();
            }

            if(category_selector == null || category_selector.equals("")){
                job_category_error.setVisibility(View.VISIBLE);
            }
            if(category_selector !=null){
                job_category_error.setVisibility(View.GONE);
            }

            if(experience_selector == null || experience_selector.equals("")){
                job_experience_error.setVisibility(View.VISIBLE);
            }
            if(experience_selector !=null){
                job_experience_error.setVisibility(View.GONE);
            }


            String job_compensation         = job_compensation_text.getText().toString().trim();
            if(job_compensation.isEmpty() || job_compensation.equals("")){
                job_compensation_error.setVisibility(View.VISIBLE);
            }
            if(!job_compensation.isEmpty()){
                job_compensation_error.setVisibility(View.GONE);
            }

            if(!job_title.isEmpty() && !job_desc.isEmpty() && !job_company.isEmpty() && !job_location.isEmpty() && jobRemoteRadio !=null && category_selector !=null &&
                    experience_selector !=null && !job_compensation.isEmpty()){
                submitPost(job_title,job_desc,job_company,job_location,remoteCat,category_selector,experience_selector,job_compensation);
            }
        });

        getCategory();
    }

    private void submitPost(String job_title, String job_desc, String job_company, String job_location, String remoteCat, String category_selector, String experience_selector, String job_compensation) {

        String requestUrl = appLinks.submit_job;
        myProgressDialog.setMessage("Please wait...");

        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        Map<String, String> postMap = new HashMap<>();
        postMap.put("user_name", job_title);
        postMap.put("job_title", job_title);
        postMap.put("job_desc", job_desc);
        postMap.put("job_company", job_company);
        postMap.put("job_location", job_location);
        postMap.put("remote_cat", remoteCat);
        postMap.put("category", category_selector);
        postMap.put("experience", experience_selector);
        postMap.put("compensation", job_compensation);


        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();


                    try {
                        String status   = response.getString("status");
                        String msg   = response.getString("msg");
                        Toast.makeText(JobPostingAddJop.this, msg, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(JobPostingAddJop.this, JobPostingActivity.class);
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


    private void init(){
        job_title_text      = findViewById(R.id.job_title_text);
        job_title_error      = findViewById(R.id.job_title_error);

        job_desc_text      = findViewById(R.id.job_desc_text);
        job_desc_error      = findViewById(R.id.job_desc_error);

        job_company_text      = findViewById(R.id.job_company_text);
        job_company_error      = findViewById(R.id.job_company_error);

        job_location_text      = findViewById(R.id.job_location_text);
        job_location_error      = findViewById(R.id.job_location_error);

        job_remote_radioGroup      = findViewById(R.id.job_remote_radioGroup);
        job_remote_error      = findViewById(R.id.job_remote_error);

        job_category_spinner      = findViewById(R.id.job_category_spinner);
        job_category_error      = findViewById(R.id.job_category_error);

        job_experience_spinner      = findViewById(R.id.job_experience_spinner);
        job_experience_error      = findViewById(R.id.job_experience_error);

        job_compensation_text      = findViewById(R.id.job_compensation_text);
        job_compensation_error      = findViewById(R.id.job_compensation_error);

        make_post      = findViewById(R.id.make_post);

        dataset_2 = new LinkedList<>(Arrays.asList("Select Work Experience","Less than 1 year","1 to 5 years", "6 to 10 years", "More than 10 years"));
        job_experience_spinner.attachDataSource(dataset_2);
    }

    private void getCategory() {

        HashMap<String, String> user = userDbHelper.getUserDetails();
        String user_name    = user.get("user_name");

        String requestUrl = appLinks.get_job_listing_category;
        JsonArrayRequest request = new JsonArrayRequest(requestUrl,
                response -> {
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data from Server! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    List<String> arrayList = new ArrayList<>();
                    String first_category_name  ="Select Category";
                    arrayList.add(first_category_name);

                    for (int i=0; i < response.length(); i++) {

                        try {
                            JSONObject detailObj = response.getJSONObject(i);
                            int cat_id        = detailObj.getInt("id");
                            String cat_name        = detailObj.getString("category_name");
                            arrayList.add(cat_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    dataset_1 = new LinkedList<>(arrayList);
                    job_category_spinner.attachDataSource(dataset_1);

                }, error -> {
            // error in getting
            Toast.makeText(getApplicationContext(), "No Data Found or Check your Internet", Toast.LENGTH_SHORT).show();
            Log.e("BusinessListing",error.toString());

        });


        request.setRetryPolicy(new DefaultRetryPolicy(
                16 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(request);
    }
}
