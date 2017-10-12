package com.example.danielius.runeinvest;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.example.danielius.runeinvest.api.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.password_repeat)
    TextInputEditText passwordRepeat;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.btn_register)
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btn_register)
    void onClickRegister(){
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String passwordRepeat = this.passwordRepeat.getText().toString();

        if(validateRegister(email,password,passwordRepeat)){
            firebaseRegister(email,password);
        }
    }

    public void firebaseRegister(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                    writeNewUser(task.getResult().getUser().getUid(),task.getResult().getUser().getDisplayName(),task.getResult().getUser().getEmail());
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    Log.w("firebase register", task.getException());
                }
            }
        });
    }

    public boolean validateRegister(String email,String password,String passwordRepeat){
        boolean valid = true;

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
            this.email.setError("Enter the correct email");
        }else{
            this.email.setError(null);
        }

        if(password.isEmpty() || passwordRepeat.isEmpty() || !password.equals(passwordRepeat) || password.length() <4 || password.length()>10){
            valid = false;
            this.password.setError("no");
        }else{
            this.password.setError(null);
        }
        return valid;
    }

    private void writeNewUser(String userId, String name, String email){
        if(name == null){
            name = this.username.getText().toString();
        }
        User user = new User(name,email);
        DocumentReference ref = FirebaseFirestore.getInstance().document("users/"+userId);
        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("debug","success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("debug","Error adding user",e);
            }
        });
    }
}
