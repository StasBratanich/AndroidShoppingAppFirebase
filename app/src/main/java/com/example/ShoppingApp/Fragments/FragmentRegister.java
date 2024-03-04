package com.example.ShoppingApp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ShoppingApp.Data.UsersData;
import com.example.ShoppingApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegister newInstance(String param1, String param2) {
        FragmentRegister fragment = new FragmentRegister();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FirebaseAuth mAuth;

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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        EditText inputTextPassword = view.findViewById(R.id.FragmentRegister_editText_password);
        EditText inputTextConfirmPassword = view.findViewById(R.id.FragmentRegister_editText_confirmPassword);
        Button registerBtn = view.findViewById(R.id.FragmentRegister_registerbtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = inputTextPassword.getText().toString();
                String confirmPassword = inputTextConfirmPassword.getText().toString();

                if(!checkMatchingPasswords(password, confirmPassword))
                {
                    Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    regFunc();
                }
            }
        });

        return view;
    }

    public void regFunc() {

        String email = ((EditText) getView().findViewById(R.id.FragmentRegister_editText_email)).getText().toString().trim();
        String password = ((EditText) getView().findViewById(R.id.FragmentRegister_editText_password)).getText().toString().trim();
        String id = ((EditText) getView().findViewById(R.id.FragmentRegister_editText_id)).getText().toString().trim();

        if (!id.matches("\\d{9}")) {
            Toast.makeText(getContext(), "ID must be 9 digits long", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(getContext(), "Email must be entered", Toast.LENGTH_SHORT).show();
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(getContext(), "Password must be entered", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // ID already exists in the database
                    Toast.makeText(getContext(), "ID already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // ID does not exist, proceed with registration
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        addUserViaID(email, id);
                                        Toast.makeText(getContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username", email);
                                        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentRegister_to_fragmentTwo, bundle);
                                    } else {
                                        Toast.makeText(getContext(), "Register Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error checking ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserViaID(String email, String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference usersRef = database.getReference("users");

        UsersData data = new UsersData(email, id);
        usersRef.child(uid).setValue(data);
    }

    public boolean checkMatchingPasswords(String password, String ConfirmPassword)
    {
        return password.equals(ConfirmPassword);
    }
}