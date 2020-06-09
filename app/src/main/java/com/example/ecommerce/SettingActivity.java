package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView profilePic, profile_img;
    private TextView profileChangeTextBtn,closeText,saveTextButton;
    private EditText fullNameEditText,UserPhoneEdit,addressEdit;
private StorageTask uploadTask;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfileReference;
    private String checker= "";
    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        storageProfileReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        profile_img = (CircleImageView)findViewById(R.id.profile_image);
        profilePic = (CircleImageView)findViewById(R.id.setting_profile_img);
        profileChangeTextBtn = (TextView)findViewById(R.id.change_profileimage_text);
        closeText = (TextView)findViewById(R.id.closeText);
        saveTextButton = (TextView)findViewById(R.id.updateText);
        fullNameEditText = (EditText)findViewById(R.id.editText_full_name);
        UserPhoneEdit = (EditText)findViewById(R.id.editText_phone_number);
        addressEdit = (EditText)findViewById(R.id.editText_address);

        userInfoDisplay(profilePic,fullNameEditText,UserPhoneEdit,addressEdit);
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });



        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", childName);
        userMap.put("address", addressEdit.getText().toString());
        userMap.put("email", UserPhoneEdit.getText().toString());
        ref.child(Prevalent.currentonlineUsers.getEmail()).updateChildren(userMap);

        startActivity(new Intent(SettingActivity.this,MainActivity.class));
        Toast.makeText(SettingActivity.this,"Profile info Update",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  && resultCode== RESULT_OK  && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profilePic.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error,Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingActivity.this,SettingActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(this,"Image is mandatory...",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(addressEdit.getText().toString())){
            Toast.makeText(this,"Please make Address...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(UserPhoneEdit.getText().toString())){
            Toast.makeText(this,"Email is mandatory...",Toast.LENGTH_SHORT).show();
        }

        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we are updating account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileRef= storageProfileReference.child(Prevalent.currentonlineUsers.getEmail()+ ".jpeg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWith(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
//                        Uri downloadUrl = task.getResult();
                       // myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("name", childName);
                        userMap.put("address", addressEdit.getText().toString());
                        userMap.put("phone", UserPhoneEdit.getText().toString());
                        //userMap.put("image", myUrl);
                        ref.child(Prevalent.currentonlineUsers.getEmail()).updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(SettingActivity.this,SettingActivity.class));
                        Toast.makeText(SettingActivity.this,"Profile info Update",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(SettingActivity.this,"Image is not Selected",Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(final CircleImageView profilePic, final EditText fullNameEditText, final EditText userPhoneEdit, final EditText addressEdit)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentonlineUsers.getEmail());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(profilePic);
                        fullNameEditText.setText(name);
                        userPhoneEdit.setText(email);
                        addressEdit.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
