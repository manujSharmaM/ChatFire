
package com.example.manuj.chatfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersRoom extends AppCompatActivity {

    String users;

    TextView noUsersxt;
    ListView listView;
    UsersAdapter usersAdapter;

    private FirebaseAuth mAuth;
    //Firebase database
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = db.getReference();
    DatabaseReference userRef = rootRef.child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_room);

        mAuth=FirebaseAuth.getInstance();

        listView = findViewById(R.id.list_view);
        final ArrayList<Users>usersList=new ArrayList<Users>();
        noUsersxt = findViewById(R.id.no_users);
        usersAdapter=new UsersAdapter(this,usersList);

        // Read from the database
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value=dataSnapshot.getValue().toString();

                try {
                    JSONObject jsonObject=new JSONObject(value);
                    users=jsonObject.getString("Name");

                    if(!users.equals(UserDetails.username)) {

                        usersList.add(new Users(users,R.drawable.businessman));
                        listView.setAdapter(usersAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //when the users clicks any users in the list,then do this
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserDetails.chatWith=usersList.get(position).getUsersName();
                Toast.makeText(UsersRoom.this, ""+UserDetails.chatWith, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UsersRoom.this,ChatRoom.class);
                startActivity(intent);
            }
        });
        //To change the users image

    }
    //ADD ICONN TO MMENU BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.Account){
            Intent intent=new Intent(UsersRoom.this,Accounts.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
