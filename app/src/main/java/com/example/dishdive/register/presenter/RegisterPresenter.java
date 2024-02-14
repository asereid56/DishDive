package com.example.dishdive.register.presenter;

import com.example.dishdive.network.user.NetworkCallBack;
import com.example.dishdive.network.user.UserService;
import com.example.dishdive.register.view.RegisterView;

public class RegisterPresenter implements NetworkCallBack {
    private UserService userService;
    private RegisterView registerView;

    public RegisterPresenter(UserService userService, RegisterView registerView) {
        this.registerView = registerView;
        this.userService = userService;
    }

    @Override
    public void onSuccess() {
        registerView.showRegistrationSuccess();
    }

    @Override
    public void onFailure(String error) {
        registerView.showRegistrationError(error);
    }
    public void register(String email , String password){
        userService.register(email, password, this);
    }
}
