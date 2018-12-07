package com.example.manuj.chatfire;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChatRoom extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    ScrollView scrollView;
    ImageView sendButton;
    EditText messageArea;
    boolean myMessage=true;


    // write to Firebase database
    String users;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference userRef = rootRef.child("User");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference msgRef = root.child("Messages").child(UserDetails.username +"_" +UserDetails.chatWith);
    DatabaseReference msg2Ref= root.child("Messages").child(UserDetails.chatWith +"_" +UserDetails.username);

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);

        mAuth = FirebaseAuth.getInstance();


        //when send button is pressed
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageArea.getText().toString();
                Log.i("MSG",msg);
                if(msg.contains(" ")){
                   String messages= msg.replaceAll(" ","-");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Message", messages);
                    map.put("User", UserDetails.username);
                    msgRef.push().setValue(map);
                    msg2Ref.push().setValue(map);
                    messageArea.setText("");
                }

                if (!msg.equals("")) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Message", msg);
                    map.put("User", UserDetails.username);
                    msgRef.push().setValue(map);
                    msg2Ref.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

//        msgRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i("DATA","data"+dataSnapshot);
//                String message=dataSnapshot.child("Message").getValue().toString();
//                Log.i("TAGG",message);
//                String userName=dataSnapshot.child("User").getValue().toString();
//                if(userName.equals(UserDetails.username)){
//                        addMessageBox( message, 1);
//                    }
//                    else{
//                        addMessageBox( message, 2);
//                    }
//
//                }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        msgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue().toString();
                Log.i("KEY",value);

                try
                {
                    JSONObject jsonObject=new JSONObject(value);
                    String message = jsonObject.getString("Message");
                    String msg=message.replaceAll("-"," ");
                    Log.i("TAGG","VALUE="+message);

                    String userName = jsonObject.getString("User");

                    if(userName.equals(UserDetails.username)){
                        addMessageBox( msg, 1);
                    }
                    else{
                        addMessageBox( msg, 2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void addMessageBox(String message, int type)

    {
        TextView textView = new TextView(ChatRoom.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.chat_bubble_shape);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.chat_bubble_shape2);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
