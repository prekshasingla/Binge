package com.example.prakharagarwal.binge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by prakharagarwal on 20/08/17.
 */

public class LoginFragment extends Fragment implements
        View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 8001;
    TextView loginstatus;
    EditText user_email;
    EditText user_password;
    Button login;
    TextView textViewError;
    private boolean passwordVerified=false;
    private boolean emailVerified=false;
    FirebaseDatabase mDatabase;
    CheckBox showPassword;

    View rootView;
    private ProgressDialog dialog;

    public LoginFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        ((LoginActivity) getActivity()).setActionBarTitle("Login");

        textViewError=(TextView) rootView.findViewById(R.id.login_error);
        showPassword=(CheckBox)rootView.findViewById(R.id.fsu_show_password);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    user_password.setTransformationMethod(new PasswordTransformationMethod());
                }else {
                    user_password.setTransformationMethod(null);

                }
            }
        });
        user_email=(EditText) rootView.findViewById(R.id.user_email);
        user_password=(EditText) rootView.findViewById(R.id.user_password);
        login=(Button)rootView.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_email.getText().toString().equals("") && user_password.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Above fields cannot be blank");

                }
                else  if(user_email.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Name cannot be blank");

                }
                else  if(user_password.getText().toString().equals(""))
                {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Password cannot be blank");

                }else if(!isValidEmail(user_email.getText().toString().trim())){
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Please Enter a valid Email Address");
                }

                else{
                    dialog = new ProgressDialog(getActivity());
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("Logging In ....");
                    dialog.setCancelable(false);
                    dialog.show();
                    userLoginFirebase(user_email.getText().toString(),user_password.getText().toString());
                }
            }
        });

        rootView.findViewById(R.id.signup_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_activity_container,new SignUpFragment()).commit();
            }
        });

        mDatabase=FirebaseDatabase.getInstance();


        mGoogleApiClient=((LoginActivity)getActivity()).getGAC();
        rootView.findViewById(R.id.google_sign_in_button).setOnClickListener(this);


        return rootView;
    }
    private void userLoginFirebase(final String email, final String password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                checkLogin(dataSnapshot,email,password);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
public void checkLogin(DataSnapshot dataSnapshot,String email,String password){

    for (DataSnapshot child : dataSnapshot.getChildren()) {
        if (child.getKey().equals(encodeEmail(email))) {
            emailVerified=true;
            for(DataSnapshot child1 : child.getChildren()){
                if(child1.getKey().equals("password")){
                    if(password.equals(child1.getValue().toString()))
                        passwordVerified=true;
                }
            }
        }


    }
    if(emailVerified && passwordVerified) {
        dialog.dismiss();
        Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

        editor.putString("username", email);
        editor.apply();
        emailVerified=false;
        passwordVerified=false;
        getActivity().onBackPressed();

    }else{
        dialog.dismiss();
        Toast.makeText(getActivity(), "Invalid Credentials. Please Try Again", Toast.LENGTH_LONG).show();
        emailVerified=false;
        passwordVerified=false;
    }
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                String email=account.getEmail();
                String name=account.getDisplayName();
                DatabaseReference ref1 = mDatabase.getReference().child("users").child(encodeEmail(email));
                User user1 = new User();
                user1.setName(name);
                user1.setPassword("google_login");
                ref1.setValue(user1);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

                editor.putString("username",email);
                editor.apply();
                getActivity().onBackPressed();
                //firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(getActivity(), "Login Failed. Please Retry", Toast.LENGTH_SHORT).show();

            }
        }else {
            //mCallbackManager.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_sign_in_button:
                signIn();
                break;

        }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public  boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
