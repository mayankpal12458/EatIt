package com.example.dell.eatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.eatit.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText editemailsignup,editpasssignup;
    Button btnsignup;
    TextView textforgotpasssignup,textloginme;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editemailsignup=(EditText)findViewById(R.id.editemailsignup);
        editpasssignup=(EditText)findViewById(R.id.editpasssignup);
        btnsignup=(Button) findViewById(R.id.btnsignup);
        textforgotpasssignup=(TextView)findViewById(R.id.textforgotpasssignup);
        textloginme=(TextView)findViewById(R.id.textloginme);
        auth=FirebaseAuth.getInstance();
        btnsignup.setOnClickListener(this);
        textloginme.setOnClickListener(this);
        textforgotpasssignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.textloginme)
        {
            Intent intent=new Intent(SignUp.this,SignIn.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.textforgotpasssignup)
        {
            Intent intent=new Intent(SignUp.this,ForgotPassword.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.btnsignup)
        {
            if(Common.isConnection(getBaseContext())) {
                signupuser(editemailsignup.getText().toString(), editpasssignup.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(),"Please Check your Connection",Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void signupuser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(SignUp.this,"Error Signing Up",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUp.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
