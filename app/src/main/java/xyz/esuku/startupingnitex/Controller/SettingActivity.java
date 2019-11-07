package xyz.esuku.startupingnitex.Controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.robertsimoes.shareable.Shareable;
import xyz.esuku.startupingnitex.Controller.Settings.ChangePassword;
import xyz.esuku.startupingnitex.Controller.Settings.Contact;
import xyz.esuku.startupingnitex.Controller.Settings.Notification;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.SQL_DB.SessionManager;

public class SettingActivity extends AppCompatActivity {

    TextView notification,invite_friends,contact_ignite_startupX,change_password,logout,delete;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        session = new SessionManager(getApplicationContext());

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

                Shareable shareAction = new Shareable.Builder(SettingActivity.this)
                        .message("Join Us at Ignite StartupX")
                        .url("https://ignitestartupx.org")
                        .socialChannel(Shareable.Builder.TWITTER)
                        .build();
                shareAction.share();
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

                session.setLogin(false);
                Intent logotIntent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(logotIntent);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialg  = new AlertDialog.Builder(SettingActivity.this);
                alertDialg.setTitle("Delete Account!");
                alertDialg.setMessage("At The moment you can\'t delete your account, you have to write the Administrator for the reason you want your " +
                        "account deleted and then enable the option for you to delete it.");
                alertDialg.setCancelable(true);
                alertDialg.show();
            }
        });
    }
}
