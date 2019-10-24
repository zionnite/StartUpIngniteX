package xyz.esuku.startupingnitex.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import com.github.paolorotolo.appintro.AppIntro;
import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;
import xyz.esuku.startupingnitex.R;
import xyz.esuku.startupingnitex.View.Fragment.IntroFrag_Four;
import xyz.esuku.startupingnitex.View.Fragment.IntroFrag_One;
import xyz.esuku.startupingnitex.View.Fragment.IntroFrag_Three;
import xyz.esuku.startupingnitex.View.Fragment.IntroFrag_Two;

import java.util.ArrayList;

public class OnboardScreen extends AppIntro {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        hideNotificationBar();
        super.onCreate(savedInstanceState);

        if(!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }
        //statusBarTransparent();
        //setIndicatorController(new DotIndicatorController(this));
        setIndicatorColor(Color.parseColor("#85c226"), Color.parseColor("#79b700"));



        addSlide(IntroFrag_One.newInstance(R.layout.intro_frag_one));
        addSlide(IntroFrag_Two.newInstance(R.layout.intro_frag_two));
        addSlide(IntroFrag_Three.newInstance(R.layout.intro_frag_three));
        addSlide(IntroFrag_Four.newInstance(R.layout.intro_frag_four));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#388E3C"));
        setNextArrowColor(Color.parseColor("#85c226"));
        setColorSkipButton(Color.parseColor("#85c226"));
        setSeparatorColor(Color.parseColor("#85c226"));
        setColorDoneText(Color.parseColor("#85c226"));



        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(60);
        setDepthAnimation();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startMainActivity();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        startMainActivity();
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.

    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("IntroSliderActivity", Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean("FirstTimeStartFlag", true);
    }
    private void setFirstTimeStartStatus(boolean stt){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("IntroSliderActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    private void startMainActivity(){
        setFirstTimeStartStatus(false);
        Intent intent = new Intent(OnboardScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void statusBarTransparent(){
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
    private void hideNotificationBar(){

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
