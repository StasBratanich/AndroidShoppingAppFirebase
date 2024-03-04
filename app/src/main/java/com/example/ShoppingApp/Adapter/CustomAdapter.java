package com.example.ShoppingApp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ShoppingApp.Data.DataModel;
import com.example.ShoppingApp.R;
import java.util.ArrayList;
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<DataModel> dataset;

    public CustomAdapter(ArrayList<DataModel> dataSet) {
        this.dataset = dataSet;
    }

    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        DataModel data = dataset.get(position);
        holder.textViewProductName.setText(data.getProductName());
        holder.textViewProductPrice.setText(data.getProductPrice());
        holder.imageView.setImageResource(data.getImage());
        holder.productQuantity.setText(String.valueOf(data.getQuantity()));
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewProductName;
        TextView textViewProductPrice;
        ImageView imageView;
        Button addBtn;
        Button deleteBtn;
        TextView productQuantity;
        TextView TextViewTotal;

        public MyViewHolder (View itemView){
            super(itemView);

            textViewProductName = itemView.findViewById(R.id.cardLayout_productName);
            textViewProductPrice = itemView.findViewById(R.id.cardLayout_productPrice);
            imageView = itemView.findViewById(R.id.cardLayout_imageView);
            addBtn = itemView.findViewById(R.id.cardLayout_addBtn);
            deleteBtn = itemView.findViewById(R.id.cardLayout_deleteBtn);
            productQuantity = itemView.findViewById(R.id.cardLayout_productQuantity);
            TextViewTotal = itemView.findViewById(R.id.cardLayout_totalText);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DataModel data = dataset.get(position);
                        int quantity = data.getQuantity() + 1;
                        data.setQuantity(quantity);
                        notifyItemChanged(position);
                    }
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DataModel data = dataset.get(position);
                        int quantity = data.getQuantity() - 1;
                        if (quantity < 0) {
                            quantity = 0;
                        }
                        data.setQuantity(quantity);
                        notifyItemChanged(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout , parent , false);
        return new MyViewHolder(view);
    }

    public int getItemCount() {
        return dataset.size();
    }

}