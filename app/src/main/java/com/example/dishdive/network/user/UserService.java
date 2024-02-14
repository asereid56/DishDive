package com.example.dishdive.network.user;
public interface UserService {
    void register(String email, String password, NetworkCallBack callBack);
    void login(String username, String password, NetworkCallBack callBack);
}
