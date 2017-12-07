package com.purpledot.fame.activites;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.purpledot.fame.R;
import com.purpledot.fame.activites.loginModule.ChooseSimActivity;
import com.purpledot.fame.activites.loginModule.LoginActivity;
import com.purpledot.fame.utils.SharedPrefManager;

public class SplashScreenActivity extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    int SPLASH_DISPLAY_LENGTH = 2000;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*imgView = findViewById(R.id.splash_logo);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.fame_logo));
        imgView.startAnimation((Animation) getResources().getAnimation(R.anim.push_up_out));*/
        final SharedPrefManager prefManager = new SharedPrefManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent;
                if (prefManager.getLoginSuccess()) {
                    mainIntent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                } else {
                    mainIntent = new Intent(SplashScreenActivity.this, ChooseSimActivity.class);
                }
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
