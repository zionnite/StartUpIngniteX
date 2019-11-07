package xyz.esuku.startupingnitex.Controller.Settings;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import xyz.esuku.startupingnitex.R;

public class ChangePassword extends AppCompatActivity {

    AppCompatEditText old_pass,new_pass,conf_new_pass;
    AppCompatButton send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        old_pass        = findViewById(R.id.old_pass);
        new_pass        = findViewById(R.id.new_pass);
        conf_new_pass        = findViewById(R.id.conf_new_pass);
        send_btn        = findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
