package com.example.ecommerce.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener


{

    public TextView productName,productDesc, productPrice;
    public ImageView productImage;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {

        super(itemView);
        productImage = (ImageView)itemView.findViewById(R.id.product_image);
        productName = (TextView)itemView.findViewById(R.id.product_name_editText);
        productDesc = (TextView)itemView.findViewById(R.id.product_description);
        productPrice = (TextView)itemView.findViewById(R.id.product_price_editText);

    }

    public void setItemListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
listener.onClick(v , getLayoutPosition(),false);
    }
}
