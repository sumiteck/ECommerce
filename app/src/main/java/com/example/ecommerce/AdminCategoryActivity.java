package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts, sportsTshirts,femaleDresses,sweaters;
    private ImageView glasses, hatsCaps,walletsPurses,shoes;
    private ImageView headphones, watches,laptops,mobiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        tshirts = (ImageView)findViewById(R.id.t_shirts);
        sportsTshirts = (ImageView)findViewById(R.id.sports_t_shirts);
        sweaters = (ImageView)findViewById(R.id.sweater);
        femaleDresses = (ImageView)findViewById(R.id.dresses);
        glasses = (ImageView)findViewById(R.id.glasses);
        hatsCaps = (ImageView)findViewById(R.id.hats);
        walletsPurses = (ImageView)findViewById(R.id.purse_valet);
        shoes = (ImageView)findViewById(R.id.shoes);
        headphones = (ImageView)findViewById(R.id.headphones);
        watches = (ImageView)findViewById(R.id.watches);
        laptops = (ImageView)findViewById(R.id.laptops);
        mobiles = (ImageView)findViewById(R.id.mobiles);



        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","tshirts");
                startActivity(intent);
            }
        });




      sportsTshirts.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
            intent.putExtra("category"," sportsTshirts");
            startActivity(intent);
        }
    });





  femaleDresses.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
        intent.putExtra("category"," femaleDresses");
        startActivity(intent);
        }
        });


        sweaters.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
        intent.putExtra("category","sweaters");
        startActivity(intent);
        }
        });



        glasses.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
        intent.putExtra("category","glasses");
        startActivity(intent);
        }
        });






        hatsCaps.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
        intent.putExtra("category","hatsCaps");
        startActivity(intent);
        }
        });

        walletsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","walletPurses");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","shoes");
                startActivity(intent);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","headphones");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","laptops");
                startActivity(intent);
            }
        });


        mobiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category","mobiles");
                startActivity(intent);
            }
        });
        }
}
