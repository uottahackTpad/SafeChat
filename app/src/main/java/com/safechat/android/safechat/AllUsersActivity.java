package com.safechat.android.safechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AllUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final int RC_SIGN_IN = 1000;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mainDataBase = database.getReference("safechat-4ca62");

    ArrayList<User> allUsers;
    ArrayList<String> userNames;
    AllUsersAdapter adapter;
    ListView listofUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mAuth = FirebaseAuth.getInstance();

        allUsers = new ArrayList<>();
        userNames = new ArrayList<>();

        adapter = new AllUsersAdapter(this,allUsers);
        listofUsers = (ListView) findViewById(R.id.listofusers);
        listofUsers.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            firebaseUi();
        }

        else{
            showAllUsers();
        }
    }


    public void firebaseUi()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    public void showAllUsers(){
        readUsers();
    }

    public void readUsers(){
        ChildEventListener uniListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                allUsers.add(user);
                adapter.notifyDataSetChanged();

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
        };



        DatabaseReference userReference = database.getReference("users");
        userReference.addChildEventListener(uniListener);
    }
}