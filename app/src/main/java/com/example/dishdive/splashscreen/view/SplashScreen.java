package com.example.dishdive.splashscreen.view;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.dishdive.MainActivity;
import com.example.dishdive.R;
import com.example.dishdive.login.view.LoginScreen;
import com.example.dishdive.network.user.UsersRemoteDataSource;
import com.example.dishdive.splashscreen.presenter.SplashPresenter;

public class SplashScreen extends AppCompatActivity implements SplashView{
    private static final long SPLASH_DELAY = 4000;
    private static final String PREF_NAME = "AuthState";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private SharedPreferences preferences;
    SplashPresenter splashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Splash);
        setContentView(R.layout.activity_splash_screen);
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SplashScreen.this , LoginScreen.class);
//                startActivity(intent);
//                finish();
             //   splashPresenter.checkUserAuthentication();
                if (isLoggedIn) {
                    showMainAcvtivity();
                } else {
                    showLoginScreen();
                }
            }
        },SPLASH_DELAY);
        splashPresenter = new SplashPresenter(this,new UsersRemoteDataSource());
       // splashPresenter.checkUserAuthentication();
    }

    @Override
    public void showMainAcvtivity() {
        Intent intent = new Intent(SplashScreen.this , MainActivity.class);
        // intent.putExtra("USER_EMAIL" , userEmail);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(SplashScreen.this , LoginScreen.class);
        startActivity(intent);
        finish();

    }
}