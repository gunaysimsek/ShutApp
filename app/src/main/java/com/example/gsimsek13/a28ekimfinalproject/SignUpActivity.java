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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "adasd";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String email;
    private String password;
    private String passwordValid;

    private String name;
    private String surname;
    private Double phoneNo;


    private EditText emailText;
    private EditText passwordText;
    private EditText passwordValidText;

    private EditText nameText;
    private EditText surnameText;
    private EditText phoneText;




    private Customer customer;

    private String schoolEmailDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        schoolEmailDomain = "ku.edu.tr";


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    sendVerificationEmail();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        emailText   = findViewById(R.id.signUpEmailText);
        passwordText = findViewById(R.id.signUpPasswordText);
        passwordValidText = findViewById(R.id.signUpPasswordValidText);

        nameText =  findViewById(R.id.signUpNameText);
        surnameText = findViewById(R.id.signUpSurnameText);
        phoneText = findViewById(R.id.signUpPhoneText);



    }

    private void sendVerificationEmail()
    {
     //   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            mAuth.signOut();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                         //   intent.putExtra("Customer", customer);
                            startActivity(intent);
                            //finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                           /* overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());*/
                           //recreate();
                           Toast.makeText(SignUpActivity.this,"Verification maili gönderilemedi.",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void signUpSend(View view){
       // if(!(phoneText.getText().toString().isEmpty())){
            phoneNo = Double.parseDouble( phoneText.getText().toString());

        //}else{
        //    phoneNo =  0.0;
        //}
        //if(!(emailText..equals("") || password.equals("") || name.equals("") || surname.equals(""))) {
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            passwordValid = passwordValidText.getText().toString();

            name = nameText.getText().toString();
            surname = surnameText.getText().toString();


            customer = new Customer(1, 1, name, surname, email, phoneNo, 0.0);
        //}

        String[] parts = email.split("@");

        String part1 = parts[0];
        String part2 = parts[1];



         if(!(part2.equals(schoolEmailDomain))){

            Toast.makeText(SignUpActivity.this ,"You need to have ku email to sign up!", Toast.LENGTH_LONG).show();


        }else if(!(password.equals(passwordValid))){

            Toast.makeText(SignUpActivity.this, "Passwords don't match!",Toast.LENGTH_LONG).show();

        }else if(email.equals("") || password.equals("") || name.equals("") || surname.equals("") )
        {
            Toast.makeText(SignUpActivity.this, "Please fill all necessary text fields!",Toast.LENGTH_LONG).show();

        }else{

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authfailed",
                                        Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignUpActivity.this, "Welcome to ShutApp, please verify your account to finish your registration.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });

        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
