package com.example.dishdive.login.presenter;
import com.example.dishdive.login.view.LoginView;
import com.example.dishdive.model.MealRepository;
import com.example.dishdive.network.user.NetworkCallBack;
import com.example.dishdive.network.user.UserService;
public class LoginPresenter implements NetworkCallBack {
    LoginView view;
    UserService userService;
    public LoginPresenter(LoginView view, UserService userService ) {
        this.view = view;
        this.userService = userService;

    }
    @Override
    public void onSuccess() {
        view.loginSuccess();
    }
    @Override
    public void onFailure(String error) {
        view.loginFailed(error);
    }
    public void loginIn(String username, String password) {
        userService.login(username, password ,this);
    }
}
