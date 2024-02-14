package com.example.dishdive.splashscreen.presenter;

import com.example.dishdive.network.user.NetworkCallBack;
import com.example.dishdive.network.user.UserService;
import com.example.dishdive.network.user.UsersRemoteDataSource;
import com.example.dishdive.splashscreen.view.SplashView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashPresenter {
    FirebaseAuth auth;
    private final SplashView view;
    private final UserService userService;

    // private final UsersRemoteDataSource usersDataSource;
    public SplashPresenter(SplashView view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

//    public void checkUserAuthentication() {
//        FirebaseUser user = auth.getCurrentUser();
//        if (user != null) {
//            view.showMainAcvtivity();
//        } else {
//            view.showLoginScreen();
//        }
//    }
}

