package com.example.prakharagarwal.fingerlickingawesome.Review;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;


import com.example.prakharagarwal.fingerlickingawesome.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class UploadReviewStoryActivityFragment extends Fragment {


    private StorageReference storageRef;

    ProgressDialog progressDialog;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public UploadReviewStoryActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storageRef = FirebaseStorage.getInstance().getReference();


        View rootView=(View)inflater.inflate(R.layout.fragment_upload_review_story, container, false);

        Intent intent=getActivity().getIntent();
        final String video=intent.getStringExtra("video");
        final File videoFile=new File(video);


        final VideoView videoViewStory=(VideoView)rootView.findViewById(R.id.video_view_review_upload);
        //videoViewStory.setVideoPath(video);
        videoViewStory.setVideoURI(Uri.parse(video));
        videoViewStory.start();



        Button uploadBtn=(Button)rootView.findViewById(R.id.btn_upload_review_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("ProgressDialog"); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);



                //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                Uri file = Uri.fromFile(videoFile);
                StorageReference riversRef = storageRef.child("videos/river5.mp4");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("story_reviews");
                                //Map<String, Review> users = new HashMap<String, Review>();
                                //users.put(new Review(review,rating,"restaurant","user123"));
                                //long now = Instant.now().toEpochMilli();


                                myRef.push().setValue(new StoryReview(downloadUrl,"restaurant","user123"));


                                Log.e("Success",""+downloadUrl);
                                progressDialog.dismiss();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                Log.e("Failed","true"+exception);
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });


            }
        });
        return rootView;
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(getActivity(), new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
            }
        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }


}
