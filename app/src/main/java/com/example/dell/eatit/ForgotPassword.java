package com.example.dell.eatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText inputemail;
    FButton resetpass;
    TextView btnback;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputemail=(EditText)findViewById(R.id.inputemail);
        resetpass=(FButton)findViewById(R.id.resetpass);
        btnback=(TextView)findViewById(R.id.btnback);
        auth=FirebaseAuth.getInstance();
        resetpass.setOnClickListener(this);
        btnback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnback)
        {
            Intent intent=new Intent(ForgotPassword.this,SignIn.class);
            startActivity(intent);
            finish();
        }
        if(v.getId()==R.id.resetpass)
        {
            resetpassword(inputemail.getText().toString());
        }
    }

    private void resetpassword(String email) {

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this,"We Have Sent Password Link To Your Mail",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ForgotPassword.this,"Failed To Send Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
