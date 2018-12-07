package com.example.manuj.chatfire;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OnLogin extends AppCompatActivity {

    EditText email;
    EditText password;
    Button button;
    TextView signUp;
    private FirebaseAuth mAuth;



    //write to the database
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference rootRef=database.getReference();
    DatabaseReference userRef=rootRef.child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_login);

        email=findViewById(R.id.emailId);
        password=findViewById(R.id.password);
        button=findViewById(R.id.button);
        signUp=findViewById(R.id.signUpText);
        //Get the instance of mAuth.
        mAuth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAGGGG","wrong");
                Intent intent=new Intent(OnLogin.this,OnRegister.class);
                startActivity(intent);
            }
        });
    }



    public void onLogin(View view){
        final String myEmail = email.getText().toString();
        final String myPass = password.getText().toString();
        if (myEmail==null&&myPass==null){
            Toast.makeText(this, "Enter the EmailId/Password", Toast.LENGTH_SHORT).show();
        }

        mAuth.signInWithEmailAndPassword(myEmail, myPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(OnLogin.this, "Auth Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId= user.getDisplayName();
                            UserDetails.username=userId;
                            Toast.makeText(OnLogin.this, "userid="+userId, Toast.LENGTH_SHORT).show();

                            //For Transition in activity intent.
                            // get the common element for the transition in this activity
                            final View androidRobotView = findViewById(R.id.button);
                            Intent intent=new Intent(OnLogin.this,UsersRoom.class);
                            ActivityOptions options = ActivityOptions
                                    .makeSceneTransitionAnimation(OnLogin.this, androidRobotView, "robot");
                            // start the new activity
                            startActivity(intent, options.toBundle());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Tag", "signInWithEmail:failure", task.getException());
                            Toast.makeText(OnLogin.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

//    public void logout(View view){
//        FirebaseAuth.getInstance().signOut();
//        Toast.makeText(this, "SIGNOUT", Toast.LENGTH_SHORT).show();
//    }


}
