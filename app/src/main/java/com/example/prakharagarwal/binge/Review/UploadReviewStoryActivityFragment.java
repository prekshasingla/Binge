package com.example.prakharagarwal.binge.Review;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.prakharagarwal.binge.MainScreen.MainActivity;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.R;
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
        final Long epoch=intent.getLongExtra("epoch",0);
        final String restaurantID=intent.getStringExtra("restaurantID");
        final String restaurantName=intent.getStringExtra("restaurantName");
        final String userId=intent.getStringExtra("user");
        final File videoFile=new File(video);


        final VideoView videoViewStory=(VideoView)rootView.findViewById(R.id.video_view_review_upload);
        videoViewStory.setVideoURI(Uri.parse(video));
        videoViewStory.start();

        videoViewStory.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

//        videoViewStory.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                    //progressBarLandScape.setVisibility(View.GONE);
//                    return true;
//                }
//                else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
//                    //progressBarLandScape.setVisibility(View.VISIBLE);
//                    return true;
//                }
//                return false;
//            }
//        });




        TextView uploadBtn=(TextView)rootView.findViewById(R.id.btn_upload_review_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Uploading...");
                progressDialog.setTitle("Uploading Video");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Uri file = Uri.fromFile(videoFile);
                StorageReference riversRef = storageRef.child("videos/"+userId+restaurantID+".mp4");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                @SuppressWarnings("VisibleForTests")
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("story_reviews").child(restaurantID).child(encodeEmail(userId));

                                myRef.setValue(new StoryReview(downloadUrl.toString(),epoch," "));
                                progressDialog.dismiss();
                                //Toast.makeText(getActivity(),"Story uploaded successfully",Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Upload Story")
                                        .setMessage("Your  story has been successfully uploaded. It will get reflected shortly.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                getActivity().onBackPressed();
                                            }
                                        })
                                        .create()
                                        .show();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                videoFile.delete();
                                Toast.makeText(getActivity(),"Could not Upload. Please try again",Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });

        TextView uploadCancel=(TextView)rootView.findViewById(R.id.btn_upload_review_cancel);
        uploadCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoFile.delete();
                getActivity().onBackPressed();



            }
        });
        return rootView;
    }


    public String encodeEmail(String email) {
        return email.replace(".", getString(R.string.encode_period))
                .replace("@", getString(R.string.encode_attherate))
                .replace("$", getString(R.string.encode_dollar))
                .replace("[", getString(R.string.encode_left_square_bracket))
                .replace("]", getString(R.string.encode_right_square_bracket));
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
