package com.example.dishdive.register.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dishdive.R;
import com.example.dishdive.login.view.LoginScreen;
import com.example.dishdive.network.user.UsersRemoteDataSource;
import com.example.dishdive.register.presenter.RegisterPresenter;

public class RegisterScreen extends AppCompatActivity implements RegisterView, OnRegisterClickListener {
    TextView email;
    TextView password;
    TextView confirmPass;
    Button btnRegister;
    Button btnBack;
    RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigester_screen);
        initUI();
        registerPresenter = new RegisterPresenter(new UsersRemoteDataSource(), this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String userConfirmPassword = confirmPass.getText().toString().trim();

                if (!isInternetConnected()) {
                    Toast.makeText(RegisterScreen.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(userEmail)) {
                    Toast.makeText(RegisterScreen.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userPassword.isEmpty() || userConfirmPassword.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userPassword.equals(userConfirmPassword)) {
                    Toast.makeText(RegisterScreen.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                onRegisterClicked(userEmail, userPassword);
            }
        });

    }

    public void initUI() {
        email = findViewById(R.id.textEmailRegister);
        password = findViewById(R.id.textPassRegister);
        confirmPass = findViewById(R.id.textConfirmPassRegister);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
    }

    @Override
    public void showRegistrationSuccess() {
        Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showRegistrationError(String error) {
        Toast.makeText(this, "Something went wrong, try again later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterClicked(String email, String password) {
        registerPresenter.register(email, password);
    }

    private boolean isValidEmail(String email) {
        // Regular expression for validating email addresses
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isInternetConnected() {
        // Check for internet connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
