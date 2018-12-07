package com.example.manuj.chatfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OnRegister extends AppCompatActivity {

    EditText userName;
    EditText email;
    EditText password;
    Button button;

    private FirebaseAuth mAuth;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference rootRef=database.getReference();
    DatabaseReference userRef=rootRef.child("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_register);


        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        button=findViewById(R.id.button);

        //Get the instance of mAuth.
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this,"Already in",Toast.LENGTH_SHORT).show();
    }

    //To register the acount
    public void onRegister(View view){
        final String MyUserName=userName.getText().toString();
        final String Myemail=email.getText().toString();
        final String Mypass=password.getText().toString();

        mAuth.createUserWithEmailAndPassword(Myemail, Mypass)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("TAG", "createUserWithEmail:success");
                            Toast.makeText(OnRegister.this, "Authentication Success.",Toast.LENGTH_SHORT).show();
                            FirebaseUser user=mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(MyUserName)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                            String uid=mAuth.getCurrentUser().getUid();
                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("Email",Myemail);
                            map.put("Name",MyUserName);
                            map.put("Image","default");
                            userRef.child(uid).setValue(map);

                            Intent intent=new Intent(OnRegister.this,OnLogin.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(OnRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}
