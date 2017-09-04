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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    FirebaseAuth mAuth;
    CallbackManager mCallbackManager;
    EditText user_email;
    EditText user_password;
    Button login;
    private boolean passwordVerified=false;
    private boolean emailVerified=false;
    FirebaseDatabase mDatabase;


    public LoginFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        ((LoginActivity) getActivity()).setActionBarTitle("Login");

        user_email=(EditText) rootView.findViewById(R.id.user_email);
        user_password=(EditText) rootView.findViewById(R.id.user_password);
        login=(Button)rootView.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_email.getText().toString().equals("") && user_password.getText().toString().equals(""))
                {

                }else{
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

        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();


        mGoogleApiClient=((LoginActivity)getActivity()).getGAC();
        rootView.findViewById(R.id.google_sign_in_button).setOnClickListener(this);

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.fb_sign_in_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB_Logn", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FB_Logn", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB_Logn", "facebook:onError", error);
                // ...
            }
        });

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
        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

        editor.putString("username", email);
        editor.apply();
        emailVerified=false;
        passwordVerified=false;
        getActivity().onBackPressed();

    }else{
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
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("G_Logn", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("G_Logn", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference ref1 = mDatabase.getReference().child("users").child(encodeEmail(user.getEmail()));
                            User user1 = new User();
                            user1.setPassword("google_login");
                            ref1.setValue(user);
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

                            editor.putString("username", user.getEmail());
                            editor.apply();
                            getActivity().onBackPressed();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("G_Logn", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Login Failed. Please Retry", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FB_Logn", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("FB_Logn", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DatabaseReference ref1 = mDatabase.getReference().child("users").child(encodeEmail(user.getEmail()));
                            User user1 = new User();
                            user1.setPassword("fb_login");
                            ref1.setValue(user);
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Login", MODE_PRIVATE).edit();

                            editor.putString("username", user.getEmail());
                            editor.apply();
                            getActivity().onBackPressed();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("FB_Logn", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
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


}
