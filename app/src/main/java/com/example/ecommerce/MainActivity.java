package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowBtn, loginBtn;
    private ImageView logo;
    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = (ImageView)findViewById(R.id.logo);
        joinNowBtn = (Button) findViewById(R.id.main_join_btn);
        loginBtn = (Button) findViewById(R.id.main_login_btn);
        loader = new ProgressDialog(this);

        Paper.init(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPassKey = Paper.book().read(Prevalent.UserPassKey);
        if(UserPhoneKey != "" && UserPassKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPassKey)){
                AllowAccess(UserPhoneKey,UserPassKey);

                loader.setTitle("Already Logged In");
                loader.setMessage("PleaseWait,while we are checking your Credentials");
                loader.setCanceledOnTouchOutside(false);
            }
        }

    }

    private void AllowAccess(final String email_login, final String pass_login) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("Users").child(email_login).exists())) {
                    Users usersdata = dataSnapshot.child("Users").child(email_login).getValue(Users.class);
                    if(usersdata.getEmail().equals(email_login))
                    {
                        if(usersdata.getPassword().equals(pass_login)){
                            Toast.makeText(MainActivity.this,"You are logged in Successfully",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Username Not EXist", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

}
