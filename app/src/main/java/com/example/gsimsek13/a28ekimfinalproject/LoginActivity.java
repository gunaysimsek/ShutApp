package com.example.gsimsek13.a28ekimfinalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "as";
    private FirebaseAuth mAuth;
    private String email;
    private String password;

    private EditText emailText;
    private EditText passwordText;

    private Customer customer;

    private FirebaseDatabase database ;
    private DatabaseReference myRef;
    String[] parts;

    FirebaseAuth myAuth;

    public void verifyBtnClicked(View view){

        myAuth = FirebaseAuth.getInstance();

        myAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if(!myAuth.getCurrentUser().isEmailVerified()){
                    Toast.makeText(LoginActivity.this, "Verfication mail is resent!", Toast.LENGTH_SHORT).show();
                    myAuth.getCurrentUser().sendEmailVerification();

                }


            }
        });



    }

    public void signInClicked(View view){

        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        parts = email.split("@");

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.wtf(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                        {

                            myRef.child("Drivers").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child(parts[0]).exists()) {

                                        Intent intent = new Intent(getApplicationContext(), DriverMainActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }else if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){

                            //FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            Toast.makeText(getApplicationContext(),"Please verify your account!",Toast.LENGTH_LONG).show();

                        }
                        if (!task.isSuccessful()) {
                            Log.wtf(TAG, "signInWithEmail:failed", task.getException());

                        }

                    }
                });

    }

    private void checkIfEmailVerified()
    {
     //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (mAuth.getCurrentUser().isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            //Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
            startActivity(intent);

        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            //Toast.makeText(LoginActivity.this, "Please verify your account to continue.", Toast.LENGTH_SHORT).show();
           // mAuth.getCurrentUser().sendEmailVerification();
           // mAuth.signOut();
            //recreate();

            //restart this activity

        }
    }

    public void signUpClicked(View view){

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void forgetBtnClicked(View view){

        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       /* Intent intent = new Intent(this, CustomerMainActivity.class);

        startActivity(intent);*/

        emailText   = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();



    }

}