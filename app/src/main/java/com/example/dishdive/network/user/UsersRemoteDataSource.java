package com.example.dishdive.network.user;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class UsersRemoteDataSource implements UserService {
    private FirebaseAuth mAuth;
    public UsersRemoteDataSource() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void register(String email, String password, NetworkCallBack callBack) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    callBack.onSuccess();
                } else {
                    callBack.onFailure("error register");
                }
            }
        });
    }
    @Override
    public void login(String email, String password, NetworkCallBack callBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            callBack.onSuccess();

                        } else {
                            callBack.onFailure("Invalid email or password");
                        }
                    }
                });
    }
}
