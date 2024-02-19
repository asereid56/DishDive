package com.example.dishdive.profile.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dishdive.R;
import com.example.dishdive.db.MealLocalDataSource;
import com.example.dishdive.login.view.LoginScreen;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.meal.MealRemoteDataSource;
import com.example.dishdive.profile.presenter.ProfilePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    Button btnLogout;
    TextView userEmail;
    SharedPreferences preferences;
    CardView cardView1 ;
    CardView cardView2 ;
    ProfilePresenter profilePresenter;
    Button btnBackup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("AuthState" , Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        userEmail = view.findViewById(R.id.emailText);
        cardView1 = view.findViewById(R.id.cardForLottie);
        cardView2 = view.findViewById(R.id.card2);
        btnBackup = view.findViewById(R.id.btnBackup);

        preferences = getActivity().getSharedPreferences("AuthState", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isLoggedIn", false)) {
            userEmail.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            cardView1.setVisibility(View.VISIBLE);
            cardView2.setVisibility(View.VISIBLE);
            setupProfileFragment();
        } else {
            showLoginDialog();
            userEmail.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
            cardView1.setVisibility(View.INVISIBLE);
            cardView2.setVisibility(View.INVISIBLE);

        }
        profilePresenter = new ProfilePresenter(MealRepository.getInstance(MealLocalDataSource.getInstance(getContext()) , MealRemoteDataSource.getInstance(getContext())));
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                user = auth.getCurrentUser();
                String email = user.getEmail();
                profilePresenter.syncAllMealFromLocal(email);
            }
        });
    }

    private void setupProfileFragment() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userEmail.setText(user.getEmail().toString());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                clearAuthState();
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Login Required");
        builder.setMessage("Some features are available only when you are logged in.");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Navigate to login screen
                Intent intent = new Intent(getActivity(), LoginScreen.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().onBackPressed();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearAuthState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

}