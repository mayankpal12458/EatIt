package com.example.dell.eatit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Profile extends AppCompatActivity {
    static final int choose_img=101;
    ImageView profile_pic;
    EditText edit_profile;
    Button fbtnprofile;
    //ProgressBar progressbar;
    Uri uri;
    String download;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth=FirebaseAuth.getInstance();
        profile_pic=(ImageView)findViewById(R.id.profile_pic);
        edit_profile=(EditText)findViewById(R.id.edit_profile);
       // progressbar=(ProgressBar)findViewById(R.id.progressbar);
        fbtnprofile=(Button)findViewById(R.id.fbtnprofile);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagechooser();

            }
        });

        fbtnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String displayname=edit_profile.getText().toString();
                if(displayname.isEmpty()){
                    edit_profile.setError("Name Required");
                    edit_profile.requestFocus();
                    return;
                }
                FirebaseUser user=auth.getCurrentUser();
                if(user!=null && uri!=null){
                    UserProfileChangeRequest profilechange=new UserProfileChangeRequest.Builder().setDisplayName(displayname)
                            .setPhotoUri(uri).build();

                    user.updateProfile(profilechange).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==choose_img && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                profile_pic.setImageBitmap(bitmap);
                uploadimagetostorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadimagetostorage() {
        StorageReference storageref= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uri!=null){
          //  progressbar.setVisibility(View.VISIBLE);
            storageref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //  progressbar.setVisibility(View.GONE);
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();



                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  //  progressbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void imagechooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Pic"),choose_img);
    }
}
