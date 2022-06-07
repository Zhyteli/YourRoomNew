package com.example.yourroom;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edEmail_pass, edPass, edPhone, edName, edPersonName, edAge, edSex;
    private FirebaseAuth mAuth;
    private Button btSignUp, btSignIn, registration, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        mClick();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null){
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        else {

            Toast.makeText(this, "User null", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        edEmail_pass = findViewById(R.id.edEmail_pass);
        edPass = findViewById(R.id.edPass);
        btSignIn = findViewById(R.id.btSignIn);
        btSignUp = findViewById(R.id.btSignUp);
        mAuth = FirebaseAuth.getInstance();

        edPhone = findViewById(R.id.edPhone);
        edName = findViewById(R.id.edName);
        edPersonName = findViewById(R.id.edPersonName);
        edAge = findViewById(R.id.edAge);
        edSex = findViewById(R.id.edSex);
        registration = findViewById(R.id.registration);
        login = findViewById(R.id.login);
    }
    private void mClick(){
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(edEmail_pass.getText().toString()) &&
                        !TextUtils.isEmpty(edPass.getText().toString())) {

                    mAuth.createUserWithEmailAndPassword(edEmail_pass.getText().toString(),
                            edPass.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailMass();
                                noShow();
                                Toast.makeText(getApplicationContext(), "Вы зарегистрировались", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Регистрация не вышла", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Пожалуйста введите свои данный полностью", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSign();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noShow();
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edEmail_pass.getText().toString()) &&
                        !TextUtils.isEmpty(edPass.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(edEmail_pass.getText().toString(),edPass.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Вы вошли", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "У вас нет доступа", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    ///////
    public void sendEmailMass(){
        FirebaseUser user =  mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>(){
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Перейдите на свой email, для авторизации", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Нет email адреса", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showSign(){
        btSignUp.setVisibility(View.GONE);
        btSignIn.setVisibility(View.GONE);

        edPhone.setVisibility(View.VISIBLE);
        edName.setVisibility(View.VISIBLE);
        edPersonName.setVisibility(View.VISIBLE);
        edAge.setVisibility(View.VISIBLE);
        edSex.setVisibility(View.VISIBLE);
        registration.setVisibility(View.VISIBLE);
    }
    private void noShow(){
        btSignUp.setVisibility(View.VISIBLE);
        btSignIn.setVisibility(View.VISIBLE);

        edPhone.setVisibility(View.GONE);
        edName.setVisibility(View.GONE);
        edPersonName.setVisibility(View.GONE);
        edAge.setVisibility(View.GONE);
        edSex.setVisibility(View.GONE);
        registration.setVisibility(View.GONE);
    }
}
