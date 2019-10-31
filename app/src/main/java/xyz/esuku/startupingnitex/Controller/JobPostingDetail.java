package xyz.esuku.startupingnitex.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import xyz.esuku.startupingnitex.R;

public class JobPostingDetail extends AppCompatActivity {

    AppCompatButton apply_btn;
    ImageView back_btn;
    TextView title,company_name,location,experience,remote,category,description;

    Dialog prefDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_posting_detail);

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
                prefDialog.setContentView(R.layout.modal_preference);
                prefDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                prefDialog.setCancelable(false);
                prefDialog.show();

                AppCompatButton already_bussiness_btn           = prefDialog.findViewById(R.id.business_preference);
            }
        });

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
        String job_id    = getIntent().getExtras().getString("job_id");
        String job_title    = getIntent().getExtras().getString("job_title");
        String job_comp    = getIntent().getExtras().getString("job_comp");
        String job_desc    = getIntent().getExtras().getString("job_desc");
        String job_cat    = getIntent().getExtras().getString("job_cat");
        String job_compensate    = getIntent().getExtras().getString("job_compensate");
        String job_exp    = getIntent().getExtras().getString("job_exp");
        String job_remote    = getIntent().getExtras().getString("job_remote");
        String job_location    = getIntent().getExtras().getString("job_location");


        title.setText(job_title);
        company_name.setText(job_comp);
        description.setText(job_desc);
        location.setText(job_location);
        experience.setText(job_exp);
        category.setText(job_cat);
        remote.setText(job_remote);
    }
}
