package xyz.esuku.startupingnitex.Controller.Settings;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import xyz.esuku.startupingnitex.R;

public class Contact extends AppCompatActivity {

    AppCompatEditText msg_title,msg_body;
    AppCompatButton send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        msg_title       = findViewById(R.id.msg_title);
        msg_body       = findViewById(R.id.msg_body);
        send_btn       = findViewById(R.id.send_btn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
