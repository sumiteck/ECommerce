package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private Button creatAccount;
    private EditText username,email,password;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        creatAccount =(Button)findViewById(R.id.sign_up_btn);
        username = (EditText)findViewById(R.id.sign_up_username);
        email = (EditText)findViewById(R.id.sign_up_email);
        password =(EditText)findViewById(R.id.sign_up_password);
        loader =new ProgressDialog(this);

        creatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = username.getText().toString();
        String emailid = email.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please Enter your Name",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(emailid)){
            Toast.makeText(this,"Please Enter your Email",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please Enter your Password",Toast.LENGTH_SHORT);

        }
        else{
            loader.setTitle("Create Account");
            loader.setMessage("PleaseWait,while we are checking your Credentials");
            loader.setCanceledOnTouchOutside(false);
            isEmailValid(name, emailid, pass);
        }

    }
    public boolean isEmailValid(final String name, final String emailid, final String pass )
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(emailid).exists())) {
                    HashMap<String,Object> usermap = new HashMap<>();
                    usermap.put("email",emailid);
                    usermap.put("name",name);
                    usermap.put("password",pass);

                    myRef.child("Users").child(emailid).updateChildren(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Your Account Has Been Created", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loader.dismiss();
                                Toast.makeText(SignUpActivity.this,"Network Problem",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
                else{
                    Toast.makeText(SignUpActivity.this,"This" +email+ "already exists",Toast.LENGTH_SHORT);
                    loader.dismiss();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        return false;
    }
}
