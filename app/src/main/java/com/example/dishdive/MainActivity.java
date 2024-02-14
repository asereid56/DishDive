package com.example.dishdive;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // FirebaseApp.initializeApp(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btnNav);
        navController = Navigation.findNavController(this, R.id.mainFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homeFragment) {
                navigateToFragment(R.id.homeFragment);
            } else if (itemId == R.id.favouriteFragment) {
                navigateToFragment(R.id.favouriteFragment);
            } else if (itemId == R.id.planFragment) {
                navigateToFragment(R.id.planFragment);
            } else if (itemId == R.id.profileFragment) {
                navigateToFragment(R.id.profileFragment);
            }
            return true;
        });

    }
    private void navigateToFragment(int fragmentId) {
        if (navController.getCurrentDestination().getId() != fragmentId) {
            navController.popBackStack(R.id.homeFragment, false);
            navController.navigate(fragmentId);
        }
    }
    @Override
    public void onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed();
        }
    }
}