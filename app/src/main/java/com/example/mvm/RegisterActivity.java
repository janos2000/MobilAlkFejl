package com.example.mvm;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameET;
    EditText emailET;
    EditText pwET;
    EditText pwagainET;
    EditText addressET;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.username);
        emailET = findViewById(R.id.email);
        pwET = findViewById(R.id.password);
        pwagainET = findViewById(R.id.passwordagain);
        addressET = findViewById(R.id.address);

        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void register(View view) {
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String pw = pwET.getText().toString();
        String pwagain = pwagainET.getText().toString();
        String address = addressET.getText().toString();

        if(!pw.equals(pwagain)){
            Toast.makeText(RegisterActivity.this, "Nem megfelelő jelszó páros!" , Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    login();
                }else{
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció!" + task.getException().getMessage() , Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void cancel(View view) {
        finish();
    }

    public void login(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void addNewBill(View view) {
    }
}