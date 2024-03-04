package com.example.ShoppingApp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ShoppingApp.Adapter.CustomAdapter;
import com.example.ShoppingApp.Data.DataModel;
import com.example.ShoppingApp.Data.Product;
import com.example.ShoppingApp.Data.ProductsData;
import com.example.ShoppingApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentShop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentShop extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentShop() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentShop.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentShop newInstance(String param1, String param2) {
        FragmentShop fragment = new FragmentShop();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<DataModel> dataset;
    private CustomAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference productsRef;
    private Button buyBtn;
    private int receiptNumber = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
        productsRef.removeValue();

        String bundleFromFragmentOne = null;

        try {
            bundleFromFragmentOne = getArguments().getString("username");
        }catch (NullPointerException e)
        {
            Toast.makeText(getContext(), "Username cannot be retrieved", Toast.LENGTH_SHORT).show();
        }

        TextView emailDisplay = view.findViewById(R.id.fragmentTwo_welcomeUsername);

        if (bundleFromFragmentOne != null) {
            emailDisplay.setText("Welcome " + bundleFromFragmentOne);
        } else {
            emailDisplay.setText("Welcome Guest");
        }

        recyclerView = view.findViewById(R.id.resview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dataset = new ArrayList<>();

        for (int i = 0; i < ProductsData.drawableArray.length; i++) {
            DataModel dataModel = new DataModel(
                    ProductsData.productsNames[i],
                    ProductsData.productsPrices[i],
                    ProductsData.id_[i],
                    ProductsData.drawableArray[i]
            );
            dataset.add(dataModel);

            Product product = new Product(
                    ProductsData.productsNames[i],
                    ProductsData.productsPrices[i],
                    ProductsData.drawableArray[i],
                    String.valueOf(ProductsData.id_[i])
            );
            productsRef.child(String.valueOf(i)).setValue(product);
        }



        adapter = new CustomAdapter(dataset);
        recyclerView.setAdapter(adapter);

        buyBtn = view.findViewById(R.id.fragmentTwo_buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Use the receipt number as the key for the receipt
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(userId)
                        .child("receipts");

                userRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int lastReceiptNumber = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            lastReceiptNumber = Integer.parseInt(snapshot.getKey());
                        }

                        int newReceiptNumber = lastReceiptNumber + 1;

                        for (int i = 0; i < dataset.size(); i++) {
                            DataModel data = dataset.get(i);
                            if (data.getQuantity() > 0) {
                                String name = data.getProductName();
                                String price = data.getProductPrice();
                                String quantity = String.valueOf(data.getQuantity());
                                String total = String.valueOf(data.getQuantity() * Integer.parseInt(price));

                                DatabaseReference productRef = userRef.child(String.valueOf(newReceiptNumber)).child(name);
                                productRef.child("price").setValue(price);
                                productRef.child("quantity").setValue(quantity);
                                productRef.child("total").setValue(total);
                            }
                        }

                        receiptNumber++;

                        Toast.makeText(getContext(), "Receipt Saved", Toast.LENGTH_SHORT).show();

                        // pass to fragment receipt
                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentTwo_to_fragmentReceipt);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Toast.makeText(getContext(), "Database error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}