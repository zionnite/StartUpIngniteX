package xyz.esuku.startupingnitex.Controller;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import xyz.esuku.startupingnitex.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    AppCompatButton forget_password_btn;
    TextView email_error;
    AppCompatEditText forget_email_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        forget_email_input      =findViewById(R.id.forget_email_input);
        forget_password_btn     =findViewById(R.id.forget_password_btn);
        email_error             =findViewById(R.id.email_error);

        forget_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email       = forget_email_input.getText().toString().trim();

                if(email.isEmpty() || email.equals("")){
                    email_error.setVisibility(View.VISIBLE);
                }
                if(!email.isEmpty()){
                    email_error.setVisibility(View.GONE);
                }

                //Now Authenticate
                if(!email.isEmpty()){
                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
