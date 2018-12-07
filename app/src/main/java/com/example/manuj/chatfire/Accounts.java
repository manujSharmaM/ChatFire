package com.example.manuj.chatfire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Accounts extends AppCompatActivity {
    private static final int GALLERY = 5;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorageRef;
    CircleImageView imageView;
    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        imageView=findViewById(R.id.profile_image);
        textView=findViewById(R.id.accountsText);
        progressDialog=new ProgressDialog(this);
        String current_user_id=mCurrentUser.getUid();
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("User").child(current_user_id);
        mImageStorageRef=FirebaseStorage.getInstance().getReference();

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("Name").getValue().toString();
                String image=dataSnapshot.child("Image").getValue().toString();

                textView.setText(name);
                Picasso.get().load(image).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void accountImage(View view) {
        //For uploading user image
        Intent galIntent=new Intent(Intent.ACTION_PICK);
        galIntent.setType("image/*");
        startActivityForResult(galIntent,GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY){
            progressDialog.setTitle("UPLOADING");
            progressDialog.setMessage("Please wait while we upload and process the image.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

             final Uri uri=data.getData();
//            imageView.setImageURI(uri);
            String current_user_id=mCurrentUser.getUid();

            final StorageReference filename=mImageStorageRef.child("Profile/").child(current_user_id+".jpg");

            filename.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mUserDatabase.child("Image").setValue(uri);
                                    Toast.makeText(Accounts.this, "IMAGE UPLOADED", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(Accounts.this, "ERROR,cannot upload", Toast.LENGTH_SHORT).show();
                        }

                    });


        }
    }
}
