package xyz.esuku.startupingnitex.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import xyz.esuku.startupingnitex.R;

public class SplashActivity extends AppCompatActivity {

    ImageView app_logo;
    TextView app_logo_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarTransparent();
        setContentView(R.layout.activity_splash);




        app_logo_Text    =(TextView) findViewById(R.id.app_logo_Text);
        app_logo   =(ImageView) findViewById(R.id.app_logo);

        Animation myAnimation   = AnimationUtils.loadAnimation(this,R.anim.splash_transition);
        app_logo_Text.startAnimation(myAnimation);
        app_logo.startAnimation(myAnimation);

        final Intent i  = new Intent(this, OnboardScreen.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }




    private void statusBarTransparent(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        );
    }
}
