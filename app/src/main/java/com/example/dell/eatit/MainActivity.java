package com.example.dell.eatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dell.eatit.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;


import static com.example.dell.eatit.R.id.editemail;
import static com.example.dell.eatit.R.id.editpass;
import static com.example.dell.eatit.R.id.fbtnsigninmain;
import static com.example.dell.eatit.R.id.fbtnsignupmain;

public class MainActivity extends AppCompatActivity {
    FButton fbtnsigninmain,fbtnsignupmain;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbtnsigninmain=(FButton)findViewById(R.id.fbtnsigninmain);
        fbtnsignupmain=(FButton)findViewById(R.id.fbtnsignupmain);
        auth=FirebaseAuth.getInstance();
        Paper.init(this);
        fbtnsigninmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
            }
        });


        fbtnsignupmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);


            }
        });

        //check remember
        String pwd=Paper.book().read(Common.PWD_KEY);
        String email=Paper.book().read(Common.email_KEY);
        if(email!=null && pwd!=null){

            login(email,pwd);}

    }

    private void login(String email, final String pwd) {

        auth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(pwd.length()<4)
                            {
                                Toast.makeText(MainActivity.this,"Length should be atleast 4",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Intent intent=new Intent(MainActivity.this,Listfoods.class);
                            startActivity(intent);
                        }
                    }
                });
    }
    }




