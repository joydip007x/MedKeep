package com.example.medx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medikeep-abf40-default-rtdb.firebaseio.com/");

    Button BsignUp2, BsignIn2;
    private EditText emailET, passET ,con_passET, nameET,numberET;
    private String name,email,pass,conpass,number;
    static String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BsignIn2 = findViewById(R.id.btn_signin2);
        BsignUp2 = findViewById(R.id.btn_signup2);
        emailET = findViewById(R.id.email);
        passET = findViewById(R.id.password);
        con_passET = findViewById(R.id.con_password);
        nameET = findViewById(R.id.username);
        numberET = findViewById(R.id.number);

        BsignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), SignInActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });

        BsignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=nameET.getText().toString();
                email = emailET.getText().toString();
                pass = passET.getText().toString();
                conpass = con_passET.getText().toString();
                number=numberET.getText().toString();

                if(name.isEmpty()||email.isEmpty()||number.isEmpty()||pass.isEmpty()||conpass.isEmpty())
                {
                    if(email.isEmpty())
                    {
                        emailET.setError("Enter an email address");
                        emailET.requestFocus();
                        return;
                    }
                    if(name.isEmpty())
                    {
                        nameET.setError("Enter your name");
                        nameET.requestFocus();
                        return;
                    }
                    if(number.isEmpty())
                    {
                        numberET.setError("Enter your number");
                        numberET.requestFocus();
                        return;
                    }
                    if(pass.isEmpty())
                    {
                        passET.setError("Enter password");
                        passET.requestFocus();
                        return;
                    }
                    if(conpass.isEmpty())
                    {
                        con_passET.setError("Re-enter your password");
                        con_passET.requestFocus();
                        return;
                    }
                }
                else if(number.length()!=11){

                    numberET.setError("Enter a correct phone number of 11 Digits");
                    numberET.requestFocus();
                    return;
                }
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailET.setError("Enter a valid email address");
                    emailET.requestFocus();
                    return;
                }
                else if( pass.length()<6){
                    passET.setError("Password too short ");
                    passET.requestFocus();
                    return;
                }
                else if( pass.compareTo(conpass)!=0 )
                {
                    passET.setError("Password doesn't match");
                    passET.requestFocus();
                    return;
                }
                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(number)){
                                Toast.makeText(SignUpActivity.this,"This number is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(number).child("name").setValue(name);
                                databaseReference.child("users").child(number).child("email").setValue(email);
                                databaseReference.child("users").child(number).child("number").setValue(number);
                                databaseReference.child("users").child(number).child("password").setValue(pass);

                                Toast.makeText(SignUpActivity.this,"Registration is Successfull ", Toast.LENGTH_SHORT).show();
                                Intent a = new Intent(getApplicationContext(), MainActivity.class);
                                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

}