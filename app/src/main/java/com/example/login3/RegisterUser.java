package com.example.login3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private TextView backtoLogin;
    private EditText edtName,edtAge,edtEmail,edtPass;
    private Button RegisterBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        backtoLogin = findViewById(R.id.backtoLogin);
        backtoLogin.setOnClickListener(this);
        RegisterBtn = findViewById(R.id.Registerbtn);
        RegisterBtn.setOnClickListener(this);
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassWord);
        mAuth = FirebaseAuth.getInstance();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backtoLogin:{
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            }
            case R.id.Registerbtn:{
                RegisterUser();
                break;
            }
        }
    }


    public void RegisterUser(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        String fullname = edtName.getText().toString().trim();
        String age = edtAge.getText().toString().trim();

        if(fullname.isEmpty()){
            edtName.setError("Full name is required !");
            edtName.requestFocus();
            return;
        }

        if(age.isEmpty()){
            edtAge.setError("Age is required !");
            edtAge.requestFocus();
            return;
        }

        if(email.isEmpty()){
            edtEmail.setError("email is required !");
            edtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please provide valid email !");
            edtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            edtPass.setError("PassWord is required !");
            edtPass.requestFocus();
            return;
        }

        if(password.length()<6){
            edtPass.setError("Min password length should be 6 characters !");
            edtPass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User user = new User(fullname,age,email,password);
                        FirebaseDatabase.getInstance("https://login3-c14ba-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Registed Successfully !", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(RegisterUser.this, "Failed to register ! Please Try Again", Toast.LENGTH_SHORT).show();
                                        //  progressBar.setVisibility(View.GONE);
                                    }
                        });
                    }else {

                        Toast.makeText(RegisterUser.this, "Failed to register ! Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }





}