package com.example.medx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://medikeep-abf40-default-rtdb.firebaseio.com/");

    Button BsignUp3, BsignIn3;
    private EditText passET1,numberET1;
    private String pass1,number1;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        BsignUp3 = findViewById(R.id.btn_signin3);
        BsignIn3 = findViewById(R.id.btn_signup3);
        passET1 = findViewById(R.id.password1);
        numberET1 = findViewById(R.id.number1);

        BsignUp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), SignUpActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });

        BsignIn3.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                pass1 = passET1.getText().toString();
                number1=numberET1.getText().toString();

                if(number1.isEmpty()||pass1.isEmpty())
                {
                    if(number1.isEmpty())
                    {
                        numberET1.setError("Enter your number");
                        numberET1.requestFocus();
                        return;
                    }
                    else if(pass1.isEmpty())
                    {
                        passET1.setError("Enter password");
                        passET1.requestFocus();
                        return;
                    }
                }
                else if(number1.length()!=11){

                    numberET1.setError("Enter a correct phone number of 11 Digits");
                    numberET1.requestFocus();
                    return;
                }
                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(number1)){
                                final String getPassword = snapshot.child(number1).child("password").getValue(String.class) ;
                                if(getPassword.equals(pass1)){
                                    Toast.makeText(SignInActivity.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                                    MainActivity.phoneNo=number1;

                                    Intent a = new Intent(getApplicationContext(), MainActivity.class);
                                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(a);
                                    finish();
                                }
                                else {
                                    Toast.makeText(SignInActivity.this,"Wrong password", Toast.LENGTH_SHORT).show();

                                }
                            }
                            else {
                                Toast.makeText(SignInActivity.this,"Number is not registered", Toast.LENGTH_SHORT).show();
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