package xyz.esuku.startupingnitex.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import xyz.esuku.startupingnitex.Controller.UsersCategoryForm.*;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.UserDb_Helper;
import xyz.esuku.startupingnitex.Util.AppLinks;
import xyz.esuku.startupingnitex.Util.MyProgressDialog;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView profile_btn;

    Dialog prefDialog;
    LinearLayout community_btn,ask_for__help,business_listing_btn,event_btn,resource_btn,coworking_btn,knowledge_btn,webinars_btn,startJob_btn,surveys_btn,toolkit_btn,settings_btn,freelancing_btn,intenship_btn,grant_btn;
    AppCompatButton already_bussiness_btn,service_provider_btn, entrepreneur_btn,government_agency_btn,investor_btn,accelerator_btn,student_btn;


    MyProgressDialog myProgressDialog;
    AppLinks appLinks = new AppLinks();
    private UserDb_Helper userDb_helper;
    String user_name;
    String service_status;
    String service_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userDb_helper   = new UserDb_Helper(MainActivity.this);
        myProgressDialog    = new MyProgressDialog(MainActivity.this);
        HashMap<String, String> user = userDb_helper.getUserDetails();
        user_name    = user.get("user_name");
        service_status    = user.get("service_status");
        service_type    = user.get("service_type");


        profile_btn   = findViewById(R.id.profile_btn);


        community_btn   = findViewById(R.id.community_btn);
        ask_for__help   = findViewById(R.id.ask_for__help);
        business_listing_btn   = findViewById(R.id.business_listing_btn);
        event_btn   = findViewById(R.id.event_btn);
        resource_btn   = findViewById(R.id.resource_btn);
        coworking_btn   = findViewById(R.id.coworking_btn);
        knowledge_btn   = findViewById(R.id.knowledge_btn);
        webinars_btn   = findViewById(R.id.webinars_btn);
        startJob_btn   = findViewById(R.id.startJob_btn);
        surveys_btn   = findViewById(R.id.surveys_btn);
        toolkit_btn   = findViewById(R.id.toolkit_btn);
        settings_btn   = findViewById(R.id.settings_btn);
        freelancing_btn   = findViewById(R.id.freelancing_btn);
        intenship_btn   = findViewById(R.id.intenship_btn);
        grant_btn   = findViewById(R.id.grant_btn);


        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        community_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
                startActivity(intent);
            }
        });

        ask_for__help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Forum.class);
                startActivity(intent);
            }
        });


        business_listing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BussinessListActivity.class);
                startActivity(intent);
            }
        });

        event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                startActivity(intent);
            }
        });

        resource_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResourceActivity.class);
                startActivity(intent);
            }
        });

        coworking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoWorkingActivity.class);
                startActivity(intent);
            }
        });

        knowledge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KnowledgeActivity.class);
                startActivity(intent);
            }
        });


        webinars_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebinarActivity.class);
                startActivity(intent);
            }
        });

        startJob_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JobPostingActivity.class);
                startActivity(intent);
            }
        });


        surveys_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivity(intent);
            }
        });

        toolkit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TookkitActivity.class);
                startActivity(intent);
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        freelancing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreelanceActivity.class);
                startActivity(intent);
            }
        });

        intenship_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InternshipActivity.class);
                startActivity(intent);
            }
        });

        grant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GrantActivity.class);
                startActivity(intent);
            }
        });


        if(service_status.equals("not_set")) {

            popUpDialogPreference();
        }
        popUpDialogPreference();
    }

    public void popUpDialogPreference(){
        prefDialog  = new Dialog(MainActivity.this);
        prefDialog.setContentView(R.layout.modal_preference);
        prefDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        prefDialog.setCancelable(false);
        prefDialog.show();

        already_bussiness_btn           = prefDialog.findViewById(R.id.business_preference);
        service_provider_btn            = prefDialog.findViewById(R.id.provider_preference);
        entrepreneur_btn                = prefDialog.findViewById(R.id.entrepreneur_preference);
        government_agency_btn           = prefDialog.findViewById(R.id.government_agency);
        investor_btn                    = prefDialog.findViewById(R.id.investor);
        accelerator_btn                 = prefDialog.findViewById(R.id.accelerator);
        student_btn                 = prefDialog.findViewById(R.id.student);

        already_bussiness_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Business.class);
                startActivity(intent);
            }
        });

        service_provider_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServiceProvider.class);
                startActivity(intent);
            }
        });

        entrepreneur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AspiringEntraprenuar.class);
                startActivity(intent);

            }
        });

        government_agency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GovermentAgency.class);
                startActivity(intent);

            }
        });

        investor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Investors.class);
                startActivity(intent);

            }
        });

        accelerator_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Accelerator.class);
                startActivity(intent);

            }
        });

        student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InternActivity.class);
                startActivity(intent);

            }
        });
    }
}
