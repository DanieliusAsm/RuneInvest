package com.example.danielius.runeinvest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.btn_login)
    Button login;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_login)
    void onLoginClick(){
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(validate(email,password)){
            firebaseLogin(email,password);
        }
    }

    public boolean validate(String email, String password){
        boolean valid = true;

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Enter a valid email!");
            valid = false;
        }else{
            this.email.setError(null);
        }

        if(password.isEmpty() || password.length() <4 || password.length()>10){
            this.password.setError("Password between 4 and 10 characters !");
            valid=false;
        }else{
            this.password.setError(null);
        }

        return valid;
    }

    public void firebaseLogin(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    setResult(RESULT_OK);
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    Log.w("firebase login", task.getException());
                }
            }
        });
    }
}
