package com.example.prakharagarwal.binge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by prakharagarwal on 20/08/17.
 */

public class SignUpFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 8001;
    TextView loginstatus;
    FirebaseAuth mAuth;
    //CallbackManager mCallbackManager;
    TextView textViewError;
    EditText user_email;
    EditText user_name;
    EditText user_password;
    Button signup;
    FirebaseDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView= inflater.inflate(R.layout.fragment_signup,container,false);
        rootView.findViewById(R.id.login_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_activity_container,new LoginFragment()).commit();
            }
        });

        ((LoginActivity) getActivity()).setActionBarTitle("Sign Up");

        textViewError=(TextView) rootView.findViewById(R.id.signup_error);
        user_name=(EditText) rootView.findViewById(R.id.user_name);
        user_email=(EditText) rootView.findViewById(R.id.user_email);
        user_password=(EditText) rootView.findViewById(R.id.user_password);
        signup=(Button)rootView.findViewById(R.id.signup_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_name.getText().toString().equals("") && user_email.getText().toString().equals("") && user_password.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Above fields cannot be blank");

                }
                else if(user_name.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Name cannot be blank");

                }else if(user_email.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Email cannot be blank");

                }
                else if(user_password.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Password cannot be blank");
                }
                else
                {
                    userSignUpFirebase(user_name.getText().toString(),user_email.getText().toString(),user_password.getText().toString());
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();





        mDatabase=FirebaseDatabase.getInstance();
        return rootView;
    }
    public FirebaseDatabase getDatabase(){
        return mDatabase;
    }
    private void userSignUpFirebase(final String name,final String email, final String password) {


        DatabaseReference ref = mDatabase.getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkExistingUser(dataSnapshot,name,email,password);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void checkExistingUser(DataSnapshot dataSnapshot,String name,String email,String password){
        boolean flag=true;
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals(encodeEmail(email))) {
                flag=false;
                break;
            }


        }
        if(flag)
            updateUser(name,email,password);
        else{
            updateUser(null,null,null);
        }

    }

    private void updateUser(String name,String email, String password) {

        if(name!=null && email!=null && password!=null) {
            DatabaseReference ref1 = mDatabase.getReference().child("users").child(encodeEmail(email));
            User user = new User();
            user.setName(name);
            user.setPassword(password);
            ref1.setValue(user);
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

            editor.putString("username", email);
            editor.apply();
            getActivity().onBackPressed();

        }else{
            Toast.makeText(getActivity(), "User exists. Please Enter A Different Email", Toast.LENGTH_SHORT).show();
        }
    }

public String encodeEmail(String email){
    return email.replace(".",getString(R.string.encode_period))
                .replace("@",getString(R.string.encode_attherate))
                .replace("$",getString(R.string.encode_dollar))
                .replace("[",getString(R.string.encode_left_square_bracket))
                .replace("]",getString(R.string.encode_right_square_bracket));
}
    public String decodeEmail(String email){
        return email.replace(getString(R.string.encode_period),".")
                .replace(getString(R.string.encode_attherate),"@")
                .replace(getString(R.string.encode_dollar),"$")
                .replace(getString(R.string.encode_left_square_bracket),"[")
                .replace(getString(R.string.encode_right_square_bracket),"]");
    }




}
