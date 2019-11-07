package xyz.esuku.startupingnitex.Controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import xyz.esuku.startupingnitex.Controller.Settings.ChangePassword;
import xyz.esuku.startupingnitex.Controller.Settings.Contact;
import xyz.esuku.startupingnitex.Controller.Settings.Notification;
import xyz.esuku.startupingnitex.R;

public class SettingActivity extends AppCompatActivity {

    TextView notification,invite_friends,contact_ignite_startupX,change_password,logout,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        notification        = findViewById(R.id.notification);
        invite_friends        = findViewById(R.id.invite_friends);
        contact_ignite_startupX        = findViewById(R.id.contact_ignite_startupX);
        change_password        = findViewById(R.id.change_password);
        logout        = findViewById(R.id.logout);
        delete        = findViewById(R.id.delete);


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Notification.class);
                startActivity(intent);
            }
        });

        invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        contact_ignite_startupX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, Contact.class);
                startActivity(intent);
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
