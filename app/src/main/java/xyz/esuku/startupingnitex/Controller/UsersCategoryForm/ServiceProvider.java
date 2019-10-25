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
import com.android.volley.toolbox.JsonObjectRequest;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ServiceProvider extends AppCompatActivity {
    ImageView back_btn;


    AppCompatEditText business_name_text,business_address_text,service_rendering_text,charges_text, prod_name_text,charges_2_text, prod_name_2_text,charges_3_text, prod_name_3_text,charges_4_text,
            prod_name_4_text,charges_5_text, prod_name_5_text, work_email_text,work_phone_text;

    TextView business_name_error,business_address_error,service_rendering_error,prod_one_error,work_email_error,work_phone_error,business_logo_error;

    AppCompatButton select_business_log,Save;

    String logo =null;

    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;

    private final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
        userDb_helper   = new UserDb_Helper(ServiceProvider.this);
        myProgressDialog    = new MyProgressDialog(ServiceProvider.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");


        business_name_text          = findViewById(R.id.business_name_text);
        business_name_error          = findViewById(R.id.business_name_error);


        business_address_text          = findViewById(R.id.business_address_text);
        business_address_error          = findViewById(R.id.business_address_error);

        service_rendering_text          = findViewById(R.id.service_rendering_text);
        service_rendering_error          = findViewById(R.id.service_rendering_error);

        charges_text          = findViewById(R.id.charges_text);
        prod_name_text          = findViewById(R.id.prod_name_text);
        prod_one_error          = findViewById(R.id.prod_one_error);


        charges_2_text          = findViewById(R.id.charges_2_text);
        prod_name_2_text          = findViewById(R.id.prod_name_2_text);

        charges_3_text          = findViewById(R.id.charges_3_text);
        prod_name_3_text          = findViewById(R.id.prod_name_3_text);

        charges_4_text          = findViewById(R.id.charges_4_text);
        prod_name_4_text          = findViewById(R.id.prod_name_4_text);

        charges_5_text          = findViewById(R.id.charges_5_text);
        prod_name_5_text          = findViewById(R.id.prod_name_5_text);



        work_email_text          = findViewById(R.id.work_email_text);
        work_email_error          = findViewById(R.id.work_email_error);

        work_phone_text          = findViewById(R.id.work_phone_text);
        work_phone_error          = findViewById(R.id.work_phone_error);

        select_business_log          = findViewById(R.id.select_business_log);
        business_logo_error          = findViewById(R.id.business_logo_error);

        back_btn          = findViewById(R.id.back_btn);
        Save          = findViewById(R.id.Save);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceProvider.this, MainActivity.class);
            startActivity(intent);
            finish();
        });



        select_business_log.setOnClickListener(v -> {

            ActivityCompat.requestPermissions(
                    ServiceProvider.this,
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


            String prod_1         = prod_name_text.getText().toString().trim();
            if(prod_1.isEmpty() || prod_1.equals("")){
                prod_one_error.setVisibility(View.VISIBLE);
            }
            if(!prod_1.isEmpty()){
                prod_one_error.setVisibility(View.GONE);
            }

            String charges         = charges_text.getText().toString().trim();
            if(charges.isEmpty() || charges.equals("")){
                prod_one_error.setVisibility(View.VISIBLE);
            }
            if(!charges.isEmpty()){
                prod_one_error.setVisibility(View.GONE);
            }



            //charges 2
            String prod_2   = prod_name_2_text.getText().toString().trim();
            if(prod_2.isEmpty()){
                prod_2  ="null";
            }
            String charges_2    = charges_2_text.getText().toString().trim();
            if(charges_2.isEmpty()){
                charges_2  ="null";
            }
            //charges 3
            String prod_3   = prod_name_3_text.getText().toString().trim();
            if(prod_3.isEmpty()){
                prod_3  ="null";
            }
            String charges_3    = charges_3_text.getText().toString().trim();
            if(charges_3.isEmpty()){
                charges_3  ="null";
            }
            //charges 4
            String prod_4   = prod_name_4_text.getText().toString().trim();
            if(prod_4.isEmpty()){
                prod_4  ="null";
            }
            String charges_4    = charges_4_text.getText().toString().trim();
            if(charges_4.isEmpty()){
                charges_4  ="null";
            }
            //charges 5
            String prod_5   = prod_name_5_text.getText().toString().trim();
            if(prod_2.isEmpty()){
                prod_5  ="null";
            }
            String charges_5    = charges_5_text.getText().toString().trim();
            if(charges_5.isEmpty()){
                charges_5  ="null";
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


            //logo    = imageToString(bitmap);
            if(logo == null || logo.equals("")){
                business_logo_error.setVisibility(View.VISIBLE);
            }
            if(logo != null){
                business_logo_error.setVisibility(View.GONE);
            }

            if(!bussines_name.isEmpty() && !bussines_add.isEmpty() && !services.isEmpty() && !charges.isEmpty() && !prod_1.isEmpty() && !email.isEmpty() && !phone.isEmpty() && logo != null){
                update_s(bussines_name,bussines_add,services,email,phone,logo,prod_1,charges,prod_2,charges_2,prod_3,charges_3,prod_4,charges_4,prod_5,charges_5);
            }

        });

    }

    public void update_s(String bussines_name, String bussines_add, String services, String email, String phone, String logo,
                         String prod_1,String charges_1,String prod_2,String charges_2,String prod_3,String charges_3,String prod_4,String charges_4,String prod_5,String charges_5) {

        myProgressDialog.setMessage("Updating, wait...");

        String requestUrl = appLinks.service_provider;


        Map<String, String> postMap = new HashMap<>();
        postMap.put("user_name", user_name);
        postMap.put("bussines_name", bussines_name);
        postMap.put("business_address", bussines_add);
        postMap.put("services", services);
        postMap.put("logo", logo);
        postMap.put("email", email);
        postMap.put("phone", phone);

        postMap.put("prod_one", prod_1);
        postMap.put("charges_one", charges_1);

        postMap.put("prod_two", prod_2);
        postMap.put("charges_two", charges_2);

        postMap.put("prod_three", prod_3);
        postMap.put("charges_three", charges_3);

        postMap.put("prod_four", prod_4);
        postMap.put("charges_four", charges_4);

        postMap.put("prod_five", prod_5);
        postMap.put("charges_five", charges_5);

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

                            AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ServiceProvider.this);
                            alertDialg.setTitle("Congratulation!");
                            alertDialg.setMessage(msg);
                            alertDialg.setCancelable(false);
                            alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ServiceProvider.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            alertDialg.show();




                        }else if(status.equals("error")){

                            AlertDialog.Builder alertDialg  = new AlertDialog.Builder(ServiceProvider.this);
                            alertDialg.setTitle("Oops!");
                            alertDialg.setMessage(msg);
                            alertDialg.setCancelable(false);
                            alertDialg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ServiceProvider.this, MainActivity.class);
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

}
