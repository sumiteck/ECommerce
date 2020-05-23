package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewProductActivity extends AppCompatActivity {

    private String CategoryName,Description,Price,PName,saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
    private Button AddNewProductBtn;
    private ImageView SelectProductImage;
    private EditText ProductName,ProductDesc,ProductPrice;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_product);
        AddNewProductBtn = (Button)findViewById(R.id.add_new_product);
        SelectProductImage = (ImageView)findViewById(R.id.select_product_img);
        ProductName =(EditText)findViewById(R.id.product_name_editText);
        ProductDesc = (EditText)findViewById(R.id.product_desc_editText);
        ProductPrice = (EditText)findViewById(R.id.product_price_editText);

        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(this,CategoryName,Toast.LENGTH_SHORT).show();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("New Product");

        SelectProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        
        AddNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {

        Description = ProductDesc.getText().toString();
        Price = ProductPrice.getText().toString();
       PName= ProductName.getText().toString();

       if(ImageUri == null){
           Toast.makeText(this,"Image is mandatory...",Toast.LENGTH_SHORT).show();
       }

       else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Please make description...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this,"Price is mandatory...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(PName)){
            Toast.makeText(this,"Product Name is mandatory...",Toast.LENGTH_SHORT).show();
        }
        else
       {
           StoreProductInformation();
       }
    }

    private void StoreProductInformation() {

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentDate.format(calender.getTime());
        productRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey);
        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String messgage = e.toString();
                Toast.makeText(AdminNewProductActivity.this,"Error:" + messgage,Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminNewProductActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImageUrl = task.getResult().toString();
                        if(task.isSuccessful()){
                            Toast.makeText(AdminNewProductActivity.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDatabase();
                            


                        }

                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("categoryName",CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", PName);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(AdminNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            Toast.makeText(AdminNewProductActivity.this,"Product is added successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String message = task.getException().toString();
                            Toast.makeText(AdminNewProductActivity.this,"Error" + message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent.createChooser(galleryIntent,"Select Picture"),GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();

            SelectProductImage.setImageURI(ImageUri);
        }
    }
}
