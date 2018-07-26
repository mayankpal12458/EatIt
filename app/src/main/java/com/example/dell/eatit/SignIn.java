package com.example.dell.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dell.eatit.Common.Common;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText editemail,editpass;
    TextView editforgotpass;
    FButton fbtnsignin;
    FirebaseAuth auth;
    com.rey.material.widget.CheckBox checkbox;
    SignInButton googlebtn;
    static final int RC_SIGN_IN=1;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        googlebtn=(SignInButton)findViewById(R.id.googlebtn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        fbtnsignin=(FButton)findViewById(R.id.fbtnsignin);
        editemail=(EditText)findViewById(R.id.editemail);
        editpass=(EditText)findViewById(R.id.editpass);
        editforgotpass=(TextView)findViewById(R.id.editforgotpass);
        checkbox= (com.rey.material.widget.CheckBox) findViewById(R.id.checkbox);

        fbtnsignin.setOnClickListener(this);
        editforgotpass.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();
        /*authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Intent intent=new Intent(SignIn.this,Listfoods.class);
                    startActivity(intent);
                }
            }
        };*/
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }*/

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()) {
                        Intent intent = new Intent(SignIn.this, Listfoods.class);
                        startActivity(intent);
                    }


                        if(!task.isSuccessful()) {
                         Toast.makeText(getApplicationContext(),"Not success",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.editforgotpass)
        {
            Intent intent=new Intent(SignIn.this,ForgotPassword.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.fbtnsignin)
        {
            if(Common.isConnection(getBaseContext())==true) {
                if(checkbox.isChecked())
                {
                    Paper.book().write(Common.email_KEY,editemail.getText().toString());
                    Paper.book().write(Common.PWD_KEY,editpass.getText().toString());
                }
                loginuser(editemail.getText().toString(), editpass.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(),"Please Check your Connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void loginuser(String email, final String pass) {
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(pass.length()<4)
                            {
                                Toast.makeText(SignIn.this,"Length should be atleast 4",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Intent intent=new Intent(SignIn.this,Listfoods.class);
                            startActivity(intent);
                            //finish();
                        }
                    }
                });
    }
}
