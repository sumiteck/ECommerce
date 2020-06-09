package com.example.ecommerce;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolders.CartViewHolder;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalPrice,txtMsg1;
    private  int OverTotalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn=findViewById(R.id.next_process_btn);
        txtTotalPrice=findViewById(R.id.total_price);
        txtMsg1=findViewById(R.id.msg1);
        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(OverTotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        OverTotalPrice=0;
    }

    @Override
    protected void onStart() {

        super.onStart();

        checkOrderStore();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child(Prevalent.currentonlineUsers.getEmail()).child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                cartViewHolder.txtProductQuantity.setText("Quantity="+cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = $" +cart.getPrice());
                cartViewHolder.txtProductName.setText(cart.getPname());

                int oneTypeProductTotalPrice =(Integer.parseInt(cart.getPrice().replaceAll("\\D+","")) )*( Integer.parseInt(cart.getQuantity()));
                OverTotalPrice = OverTotalPrice + oneTypeProductTotalPrice;
                txtTotalPrice.setText("Total Price = $" + String.valueOf(OverTotalPrice));
                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]= new CharSequence[]
                                {
                                        "Edit Quantity",
                                        "Remove from Cart"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }

                                if(which==1){
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentonlineUsers.getEmail())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this,"Item removed successfully",Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(CartActivity.this,HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public void checkOrderStore(){
        DatabaseReference ordersRef;
        ordersRef=FirebaseDatabase.getInstance().getReference().child("AdminOrders").child(Prevalent.currentonlineUsers.getEmail());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState=dataSnapshot.child("state").getValue().toString();
                    String userName=dataSnapshot.child("name").getValue().toString();
                    if(shippingState.equals("shipped")){
                        txtTotalPrice.setText("Dear " + userName + "\n Order is shipped successfully ");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations ,your final order has been shipped successfully.Soon you will receive your order at your doorstep");
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this,"you can purchase more products ,once you received this order",Toast.LENGTH_LONG).show();
                    }
                    if(shippingState.equals("not shipped")){
                        txtTotalPrice.setText("Shipping state= Not shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this,"you can purchase more products ,once you received this order",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
