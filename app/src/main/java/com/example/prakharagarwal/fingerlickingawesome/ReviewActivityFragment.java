package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    Button videoBtn;
    View rootView;

    File videoFile;
    Uri videoUri;
    String video=null;

    private StorageReference storageRef;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();




    public ReviewActivityFragment() {
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

        rootView = inflater.inflate(R.layout.fragment_review, container, false);

        videoBtn=(Button)rootView.findViewById(R.id.btnSelectVideo);
        videoBtn.performClick();
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);

            }
        });

        Button uploadBtn=(Button)rootView.findViewById(R.id.btnUploadVideo);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                Uri file = Uri.fromFile(videoFile);
                StorageReference riversRef = storageRef.child("videos/river3.mp4");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Log.e("Success","true");

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            videoUri = data.getData();
            videoFile=new File(getRealPathFromUri(videoUri));
            video=getRealPathFromUri(videoUri);
            //Log.i("Strng", file.toString());
            Log.d("data video",""+videoFile);
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getActivity().getContentResolver().query(tempUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            //Log.d("data video",""+cursor.getString(column_index));

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(getActivity(), new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
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
