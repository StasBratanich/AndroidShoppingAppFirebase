package com.example.ShoppingApp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ShoppingApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentReceipt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReceipt extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentReceipt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentReceipt.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentReceipt newInstance(String param1, String param2) {
        FragmentReceipt fragment = new FragmentReceipt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference receiptsRef = userRef.child("receipts");

        receiptsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return; // Exit early if the context is null
                }
                // Clear any existing views
                LinearLayout receiptLayout = view.findViewById(R.id.receiptLayout);
                receiptLayout.removeAllViews();

                // Iterate through receipt data and display it
                for (DataSnapshot receiptSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> receiptData = (Map<String, Object>) receiptSnapshot.getValue();

                    // Create a TextView to display receipt data
                    TextView textView = new TextView(getContext());
                    // Format the receipt data as needed
                    String receiptInfo = "Receipt ID: " + receiptSnapshot.getKey() + "\n";
                    receiptInfo += "Items:\n";
                    for (Map.Entry<String, Object> entry : receiptData.entrySet()) {
                        receiptInfo += entry.getKey() + ": " + entry.getValue() + "\n";
                    }
                    textView.setText(receiptInfo);

                    // Add the TextView to the layout
                    receiptLayout.addView(textView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        return view;
    }
}