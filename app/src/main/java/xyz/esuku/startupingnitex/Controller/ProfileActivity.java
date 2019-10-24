package xyz.esuku.startupingnitex.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.SessionManager;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profile_image;
    AppCompatButton interest_btn,experience_btn,stage_btn,save_profile;
    ImageView back_btn;
    AppCompatEditText fullname_text,user_name_text,city_text,phonoNo_text,business_text,website_text;
    TextView fullName_error,user_name_error,city_error,phoneNo_error,business_error,website_error,logout;
    Dialog interest_dialog,experience_dialog,stage_dialog;

    MyProgressDialog myProgressDialog;
    private SessionManager session;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;

    private final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;

    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDb_helper   = new UserDb_Helper(ProfileActivity.this);
        myProgressDialog    = new MyProgressDialog(ProfileActivity.this);
        // Session manager
        session = new SessionManager(getApplicationContext());

//        interest_btn    = findViewById(R.id.interest_btn);
//        experience_btn  = findViewById(R.id.experience_btn);
//        stage_btn       = findViewById(R.id.stage_btn);
        back_btn        = findViewById(R.id.back_btn);
        save_profile    = findViewById(R.id.save_profile);
        profile_image    = findViewById(R.id.profile_image);


        fullname_text    = findViewById(R.id.fullname_text);
        fullName_error    = findViewById(R.id.fullName_error);

        user_name_text    = findViewById(R.id.user_name_text);
        user_name_text.setEnabled(false);
        user_name_error    = findViewById(R.id.user_name_error);

        city_text    = findViewById(R.id.city_text);
        city_error    = findViewById(R.id.city_error);

        phonoNo_text    = findViewById(R.id.phonoNo_text);
        phoneNo_error    = findViewById(R.id.phoneNo_error);

        business_text    = findViewById(R.id.business_text);
        business_error    = findViewById(R.id.business_error);

        website_text    = findViewById(R.id.website_text);
        website_error    = findViewById(R.id.website_error);


        logout    = findViewById(R.id.logout);




        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");

        fullname_text.setText(user.get("full_name"));
        user_name_text.setText(user.get("user_name"));
        city_text.setText(user.get("city"));
        phonoNo_text.setText(user.get("phone_no"));
        business_text.setText(user.get("business_name"));
        website_text.setText(user.get("website_url"));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(getApplicationContext()).load(user.get("user_image")).apply(options).into(profile_image);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        logout.setOnClickListener(v -> {

            session.setLogin(false);
            Intent logotIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(logotIntent);
            finish();
        });

//        interest_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                interest_dialog  = new Dialog(ProfileActivity.this);
//                interest_dialog.setContentView(R.layout.modal_dialog_interest);
//                interest_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                interest_dialog.show();
//            }
//        });
//
//        experience_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                experience_dialog  = new Dialog(ProfileActivity.this);
//                experience_dialog.setContentView(R.layout.modal_dialog_experience);
//                experience_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                experience_dialog.show();
//            }
//        });
//
//        stage_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stage_dialog  = new Dialog(ProfileActivity.this);
//                stage_dialog.setContentView(R.layout.modal_dialog_stage);
//                stage_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                stage_dialog.show();
//            }
//        });

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate all Input Field
                String full_name       = fullname_text.getText().toString().trim();
                String user_name       = user_name_text.getText().toString().trim();
                String city            = city_text.getText().toString().trim();
                String phone           = phonoNo_text.getText().toString().trim();
                String bussiness_name  = business_text.getText().toString().trim();
                String website         = website_text.getText().toString().trim();



                if(full_name.isEmpty() || full_name.equals("")){
                    fullName_error.setVisibility(View.VISIBLE);
                }
                if(user_name.isEmpty() || user_name.equals("")){
                    user_name_error.setVisibility(View.VISIBLE);
                }
                if(city.isEmpty() || city.equals("")){
                    city_error.setVisibility(View.VISIBLE);
                }
                if(phone.isEmpty() || phone.equals("")){
                    phoneNo_error.setVisibility(View.VISIBLE);
                }
                if(bussiness_name.isEmpty() || bussiness_name.equals("")){
                    business_error.setVisibility(View.VISIBLE);
                }
                if(website.isEmpty() || website.equals("")){
                    website_error.setVisibility(View.VISIBLE);
                }


                if(!full_name.isEmpty()){
                    fullName_error.setVisibility(View.GONE);
                }
                if(!user_name.isEmpty()){
                    user_name_error.setVisibility(View.GONE);
                }
                if(!city.isEmpty()){
                    city_error.setVisibility(View.GONE);
                }
                if(!phone.isEmpty()){
                    phoneNo_error.setVisibility(View.GONE);
                }
                if(!bussiness_name.isEmpty()){
                    business_error.setVisibility(View.GONE);
                }
                if(!website.isEmpty()){
                    website_error.setVisibility(View.GONE);
                }

                //Now Authenticate
                if(!full_name.isEmpty() && !user_name.isEmpty() && !city.isEmpty() && !phone.isEmpty() && !bussiness_name.isEmpty() && !website.isEmpty()){
                    update_profile(full_name,user_name,city,phone,bussiness_name,website);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ProfileActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
    }

    private void upload_profile_pic() {
        myProgressDialog.create_dialog("Updating wait...");
        String requestUrl = appLinks.update_profile_pic;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updated",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);

                    HashMap<String, String> user = userDb_helper.getUserDetails();
                    String user_name    = user.get("user_name");



                    String status       = result.getString("status");
                    String msg          = result.getString("msg");

                    if(status.equals("error")){

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();

                    }else if(status.equals("success")){


                        String user_img    = result.getString("user_img");

                        userDb_helper.update_profile_pic(user_name,user_img);
                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
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

                HashMap<String, String> user = userDb_helper.getUserDetails();
                String user_name    = user.get("user_name");
                String  image_name  = imageToString(bitmap);
                postMap.put("user_name", user_name);
                postMap.put("image_name", image_name);
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

    private void update_profile(String full_name, String user_name, String city, String phone, String bussiness_name, String website) {
        myProgressDialog.create_dialog("Updating wait...");
        String requestUrl = appLinks.update_profile;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updated",response.toString());

                myProgressDialog.dismiss();
                try {
                    JSONObject result = new JSONObject(response);

                    HashMap<String, String> user = userDb_helper.getUserDetails();
                    String user_name    = user.get("user_name");



                    String status       = result.getString("status");
                    String msg          = result.getString("msg");

                    if(status.equals("error")){

                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();

                    }else if(status.equals("success")){

                        String full_name    = result.getString("full_name");
                        String phone        = result.getString("phone");
                        String city        = result.getString("city");
                        String business        = result.getString("business");
                        String website        = result.getString("website");
                        String age              = result.getString("age");
                        String sex              = result.getString("sex");
                        String email              = result.getString("email");

                        userDb_helper.update_profile(user_name,full_name,phone,city,business,website);
                        final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ProfileActivity.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();

                    }

                    fullname_text.setText(user.get("full_name"));
                    user_name_text.setText(user.get("user_name"));
                    city_text.setText(user.get("city"));
                    phonoNo_text.setText(user.get("phone_no"));
                    business_text.setText(user.get("business_name"));
                    website_text.setText(user.get("website_url"));

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
                postMap.put("city", city);
                postMap.put("phone", phone);
                postMap.put("bussiness_name", bussiness_name);
                postMap.put("website", website);
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
                    upload_profile_pic();



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
        return encodedImage;
    }

}
