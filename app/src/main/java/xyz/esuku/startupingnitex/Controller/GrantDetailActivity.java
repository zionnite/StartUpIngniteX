package xyz.esuku.startupingnitex.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import net.gotev.uploadservice.MultipartUploadRequest;
import org.json.JSONException;
import org.json.JSONObject;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import xyz.esuku.startupingnitex.Controller.UsersCategoryForm.Business;
import xyz.esuku.startupingnitex.MyApplication;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;
import xyz.esuku.startupingnitex.Util.Upload;
import xyz.esuku.startupingnitex.Util.VolleyMultipartRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GrantDetailActivity extends AppCompatActivity implements PickiTCallbacks {

    ImageView back_btn,grant_image;
    TextView grant_topic,grant_content;


    MyProgressDialog myProgressDialog;
    private UserDb_Helper userDbHelper;
    AppLinks appLinks = new AppLinks();

    FloatingTextButton upload_business_blan,upload_slide_deck;
    FloatingActionButton upload_bussines_plan_fab,upload_slide_deck_fab;
    String grant_id;
    String user_name;
    String upload_type;
    private String selectedFilePath;

    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    //Declare PickiT
    PickiT pickiT;

    private int serverResponseCode = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String SERVER_URL = appLinks.upload_business_plan;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNotificationBar();
        setContentView(R.layout.activity_grant_detail);
        requestStoragePermission();

        //Initialize PickiT
        pickiT = new PickiT(this, this);

        //back_btn    =findViewById(R.id.back_btn);
        grant_image    =findViewById(R.id.grant_image);
        grant_topic    =findViewById(R.id.grant_topic);
        grant_content    =findViewById(R.id.grant_content);

        upload_business_blan    =findViewById(R.id.business_plan_btn);
        upload_slide_deck       =findViewById(R.id.slide_deck_btn);

        upload_bussines_plan_fab       =findViewById(R.id.upload_bussines_plan_fab);
        upload_slide_deck_fab       =findViewById(R.id.upload_slide_deck_fab);



        myProgressDialog    = new MyProgressDialog(GrantDetailActivity.this);
        userDbHelper = new UserDb_Helper(GrantDetailActivity.this);

        HashMap<String, String> user = userDbHelper.getUserDetails();
        user_name    = user.get("user_name");

//        back_btn.setOnClickListener(v -> {
//            Intent intent = new Intent(GrantDetailActivity.this, GrantActivity.class);
//            startActivity(intent);
//            finish();
//        });




        upload_business_blan.setOnClickListener(v -> {

            upload_bussines_plan_fab.setVisibility(View.VISIBLE);
            showFileChooser();
        });

        upload_slide_deck.setOnClickListener(v -> {

            upload_slide_deck_fab.setVisibility(View.VISIBLE);
            showFileChooser();
        });

        upload_bussines_plan_fab.setOnClickListener(v ->{

            upload_type     ="business_plan";
            uploadVideo();
        });


        upload_slide_deck_fab.setOnClickListener(v ->{

            upload_type     ="slide_deck";
            uploadVideo();
        });



        setIniView();
    }

    private void setIniView() {
        grant_id    = getIntent().getExtras().getString("grant_id");
        String grant_name    = getIntent().getExtras().getString("grant_name");
        String grant_desc    = getIntent().getExtras().getString("grant_desc");
        String grant_img    = getIntent().getExtras().getString("grant_img");
        int position    = getIntent().getExtras().getInt("position");


        Glide.with(getApplicationContext()).load(grant_img).centerCrop().placeholder(R.drawable.start_up_logo).into(grant_image);
        grant_topic.setText(grant_name);
        grant_content.setText(stripHtml(grant_desc));
    }
    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }
    private void hideNotificationBar(){

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
//        intent.setType("application/pdf");
        intent.setType("*/*");
//        intent.setType("docx/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
    }

    //handling the ima chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //filePath = data.getData();

            pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);

        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



    //Here evertything goes for now
    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {

        filePath    = Uri.parse(path);
    }




    private void uploadVideo() {


        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;
            MyProgressDialog myProgressDialog;
            FloatingActionButton upload_bussines_plan_fab,upload_slide_deck_fab;


            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                myProgressDialog    = new MyProgressDialog(GrantDetailActivity.this);
                myProgressDialog.create_dialog("Uploading File, Please wait...");

                upload_bussines_plan_fab       =findViewById(R.id.upload_bussines_plan_fab);
                upload_slide_deck_fab       =findViewById(R.id.upload_slide_deck_fab);
                upload_bussines_plan_fab.setVisibility(View.GONE);
                upload_slide_deck_fab.setVisibility(View.GONE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myProgressDialog.dismiss();



                try {
                    JSONObject result   = new JSONObject(s);

                    String status   = result.getString("status");
                    String msg      = result.getString("msg");

                    if(status.equals("fail")){
                        AlertDialog.Builder alertDialg  = new AlertDialog.Builder(GrantDetailActivity.this);
                        alertDialg.setTitle("Oops!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();
                    }else if(status.equals("success")){
                        AlertDialog.Builder alertDialg  = new AlertDialog.Builder(GrantDetailActivity.this);
                        alertDialg.setTitle("Congratulation!");
                        alertDialg.setMessage(msg);
                        alertDialg.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            protected String doInBackground(Void... params) {

                Map<String, String> dataparams = new HashMap<String, String>(2);
                dataparams.put("user_name", user_name);
                dataparams.put("upload_type", upload_type);
                dataparams.put("grant_id", grant_id);

                Upload u = new Upload();
                String msg = u.upLoad2Server(String.valueOf(filePath),dataparams);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }






}
