package com.example.dishdive.login.view;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dishdive.MainActivity;
import com.example.dishdive.R;
import com.example.dishdive.login.presenter.LoginPresenter;
import com.example.dishdive.network.user.UsersRemoteDataSource;
import com.example.dishdive.register.view.RegisterScreen;
import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity implements OnLoginClickListener , LoginView {
    private static final String USERDATA = "USERDATA";
    TextView emailAddress;
    TextView password;
    ImageButton btnGoogle;
    ImageButton btnFacebook;
    TextView btnSignUp;
    Button btnGuest;
    Button btnLogin;
    LoginPresenter loginPresenter;
    SharedPreferences preferences;
    private static final String PREF_NAME = "AuthState";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initUI();
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            navigateToMainActivity();
            finish();
        }

        loginPresenter = new LoginPresenter(this, new UsersRemoteDataSource());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
                startActivity(intent);
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (LoginScreen.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked(emailAddress.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }
    public void initUI(){
        emailAddress = findViewById(R.id.textEmail);
        password = findViewById(R.id.textPass);
        btnFacebook = findViewById(R.id.btnFaceBook);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGuest = findViewById(R.id.btnGuest);
        btnLogin = findViewById(R.id.btnLogin);
    }
    @Override
    public void loginSuccess() {
//        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(LoginScreen.this , MainActivity.class);
//        startActivity(intent);
//        finish();
        Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
        navigateToMainActivity();
    }

    @Override
    public void loginFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if (!isInternetConnected()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check for valid email using regex
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for internet connection


        // Proceed with login
        loginPresenter.loginIn(email,password);
    }

    private boolean isValidEmail(String email) {
        // Regular expression for validating email addresses
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
    private void navigateToMainActivity() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
        Intent intent = new Intent(LoginScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}