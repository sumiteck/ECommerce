package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button main_login;
    private EditText username_login,password_login;
    private CheckBox remember_me;
    private ProgressDialog loader;
    private String parentDbname = "Users";
    private TextView AdminLink,NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        main_login =(Button)findViewById(R.id.login_btn);
        username_login = (EditText)findViewById(R.id.login_username);
        remember_me = (CheckBox) findViewById(R.id.remember_me);
        password_login =(EditText)findViewById(R.id.login_password);
        loader =new ProgressDialog(this);
        AdminLink = (TextView)findViewById(R.id.admin);
        
        main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_login.setText("ADMIN LOGIN");
                AdminLink.setVisibility(View.INVISIBLE);
                parentDbname = "Admins";
            }
        });
    }

    private void onLogin() {

        String email_login = username_login.getText().toString();
        String pass_login = password_login.getText().toString();
        if(TextUtils.isEmpty(email_login)){
            Toast.makeText(this,"Please Enter your Email",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(pass_login)){
            Toast.makeText(this,"Please Enter your Password",Toast.LENGTH_SHORT).show();

        }


        else{
            loader.setTitle("Create Account");
            loader.setMessage("PleaseWait,while we are checking your Credentials");
            loader.setCanceledOnTouchOutside(false);
            allowAccesstoAccount(email_login, pass_login);
        }
    }

    private void allowAccesstoAccount(final String email_login, final String pass_login) {

        if(remember_me.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, email_login);
            Paper.book().write(Prevalent.UserPassKey, pass_login);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child(parentDbname).child(email_login).exists())) {
                    Users usersdata = dataSnapshot.child(parentDbname).child(email_login).getValue(Users.class);
                    if(usersdata.getEmail().equals(email_login))
                    {
                        if(usersdata.getPassword().equals(pass_login)){
                            if(parentDbname.equals("Admins")){

                                Toast.makeText(LoginActivity.this,"Welcome Admin,You are logged in Successfully",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(LoginActivity.this,"You are logged in Successfully",Toast.LENGTH_SHORT).show();
                            Prevalent.currentonlineUsers = usersdata;

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                }
                else
                    {
                    Toast.makeText(LoginActivity.this, "Username Not EXist", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

}
}

