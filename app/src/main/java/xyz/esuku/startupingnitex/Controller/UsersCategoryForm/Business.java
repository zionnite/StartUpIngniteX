package xyz.esuku.startupingnitex.Controller.UsersCategoryForm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.Controller.GrantDetailActivity;
import xyz.esuku.startupingnitex.Controller.MainActivity;
import xyz.esuku.startupingnitex.Controller.ProfileActivity;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Business extends AppCompatActivity {

    ImageView back_btn;

    NiceSpinner specialty_spinner;
    List<String> dataset;

    AppCompatEditText business_name_text,business_address_text,service_rendering_text,work_email_text,work_phone_text;

    TextView business_name_error,business_address_error,service_rendering_error,specialty_error,work_email_error,work_phone_error,business_logo_error, bus_type_error;

    AppCompatButton select_business_log,Save;

    String selected_specialty =null;
    String logo =null;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;

    private final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;

    RadioGroup bus_type_radioGroup;
    AppCompatRadioButton busTypeRadioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        userDb_helper   = new UserDb_Helper(Business.this);
        myProgressDialog    = new MyProgressDialog(Business.this);
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

        bus_type_radioGroup          = findViewById(R.id.bus_type_radioGroup);
        bus_type_error          = findViewById(R.id.bus_type_error);

        select_business_log          = findViewById(R.id.select_business_log);
        business_logo_error          = findViewById(R.id.business_logo_error);

        back_btn          = findViewById(R.id.back_btn);
        Save          = findViewById(R.id.Save);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Business.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        specialty_spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {

            String item = String.valueOf(parent.getItemAtPosition(position));
            //Toast.makeText(GrantActivity.this, item, Toast.LENGTH_SHORT).show();
            selected_specialty  = item;
            Log.d("Grant ID", String.valueOf(id)+" POSITION "+String.valueOf(position)+ "NAME "+item);
        });

        select_business_log.setOnClickListener(v -> {

            ActivityCompat.requestPermissions(
                    Business.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_GALLERY_REQUEST
            );
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

            String industry         = selected_specialty;
            if(industry == null || industry.equals("")){
                specialty_error.setVisibility(View.VISIBLE);
            }
            if(industry !=null){
                specialty_error.setVisibility(View.GONE);
            }


            if(logo == null || logo.equals("")){
                business_logo_error.setVisibility(View.VISIBLE);
            }
            if(logo != null){
                business_logo_error.setVisibility(View.GONE);
            }


            int selectedBus  = bus_type_radioGroup.getCheckedRadioButtonId();
            busTypeRadioButton  = findViewById(selectedBus);
            String  bus_type = null;


            if(busTypeRadioButton == null || busTypeRadioButton.equals("")){
                bus_type_error.setVisibility(View.VISIBLE);
            }
            if(busTypeRadioButton !=null){
                bus_type_error.setVisibility(View.GONE);
                bus_type      = busTypeRadioButton.getText().toString().trim();
            }

            if(!bussines_name.isEmpty() && !bussines_add.isEmpty() && !services.isEmpty()  && !email.isEmpty() && !phone.isEmpty() && logo != null && bus_type !=null && industry !=null){
                update_s(bussines_name,bussines_add,services,email,phone,logo, bus_type, industry);
            }

        });

        get_list_of_specialty();
    }


    public void update_s(String bussines_name, String bussines_add, String services, String email, String phone, String logo, String bus_type, String industry) {

        myProgressDialog.setMessage("Updating, wait...");

        String requestUrl = appLinks.update_already_existing_business;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("user_name", user_name);
        postMap.put("bussines_name", bussines_name);
        postMap.put("business_address", bussines_add);
        postMap.put("services", services);
        postMap.put("industry", industry);
        postMap.put("logo", logo);
        postMap.put("email", email);
        postMap.put("phone", phone);
        postMap.put("bus_type", bus_type);

        JsonObjectRequest req = new JsonObjectRequest(requestUrl, new JSONObject(postMap),
                response -> {

                    myProgressDialog.dismiss();
                    Log.e("RxAndroidServer",response.toString());

                    String event_checker;
                    try {
                        String msg = response.getString("msg");
                        String status = response.getString("status");
                        if(status.equals("success")){

                            String service_type       = response.getString("service_type");
                            String service_status       = response.getString("service_type");
                            userDb_helper.update_service_stauts(user_name,service_status,service_type);

                            AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Business.this);
                            alertDialg.setTitle("Congratulation!");
                            alertDialg.setMessage(msg);
                            alertDialg.setCancelable(false);
                            alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Business.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            alertDialg.show();




                        }else if(status.equals("error")){

                            AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Business.this);
                            alertDialg.setTitle("Oops!");
                            alertDialg.setMessage(msg);
                            alertDialg.setCancelable(false);
                            alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Business.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            alertDialg.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("RxAndroidServer4",e.toString());
                    }


                }, error -> {
            myProgressDialog.dismiss();
            Log.e("RxAndroidServer5",error.toString());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Intent intent   = new Intent(Intent.ACTION_PICK);
//                intent.setType("images/*");

                Intent intent   = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent,"Select An Image"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(),"You don't have permission to access gallery",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data !=null){
            Uri filepath    = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                //prof_imageName.setImageBitmap(bitmap);
                if(bitmap !=null){
                    logo    = imageToString(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);

        //Log.e("StringEncoded", encodedImage);
        return encodedImage;
    }

    private void get_list_of_specialty(){
        myProgressDialog.setMessage("Please wait...");

        String requestUrl = appLinks.get_business_industry;

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

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(Business.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();
                    }else if(status.equals("success")){
                        List<String> arrayList = new ArrayList<>();
                        arrayList.add("Select");

                        JSONArray detailArry   = result.getJSONArray("detail");
                        for(int i=0; i < detailArry.length(); i++){
                            JSONObject detailObj = detailArry.getJSONObject(i);

                            String specialty_name        = detailObj.getString("specialty_name");
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
