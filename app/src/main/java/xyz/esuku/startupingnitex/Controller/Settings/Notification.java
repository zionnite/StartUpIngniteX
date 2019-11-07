package xyz.esuku.startupingnitex.Controller.Settings;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatToggleButton;
import xyz.esuku.startupingnitex.R;

public class Notification extends AppCompatActivity {


    AppCompatToggleButton post_btn,tagging_btn,news_btn,res_btn,event_btn,msg_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        post_btn        = findViewById(R.id.post_btn);
        tagging_btn        = findViewById(R.id.tagging_btn);
        news_btn        = findViewById(R.id.news_btn);
        res_btn        = findViewById(R.id.res_btn);
        event_btn        = findViewById(R.id.event_btn);
        msg_btn        = findViewById(R.id.msg_btn);
    }
}
