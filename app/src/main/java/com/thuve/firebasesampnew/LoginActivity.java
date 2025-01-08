package com.thuve.firebasesampnew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginbtn;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView Errortxt,Regestertxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

        loginbtn = findViewById(R.id.loginButton);

        Errortxt = findViewById(R.id.Errortxt);
        Regestertxt=findViewById(R.id.registerText);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkemail=email.getText().toString() ;
                String checkpassword=password.getText().toString();

                if (valemail(checkemail.replace(".",",")) && valpass(checkpassword)) {

                    checkuser(checkemail.replace(".",","), checkpassword);
                }
            }
        });

        Regestertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    //validate Email
    private boolean valemail(String email) {
        if (email.isEmpty()) {
            Errortxt.setText("Please Enter Email");
            return false;
        } else {
            Errortxt.setText(""); // Clear any previous error
            return true;
        }
    }

    //Validate Password
    private boolean valpass(String password) {
        if (password.isEmpty()) {
            Errortxt.setText("Please Enter Password");
            return false;
        } else {
            Errortxt.setText(""); // Clear any previous error
            return true;
        }
    }


    //Note-Firebase DB Part

    private void checkuser(String email, String password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkuserDb = reference.orderByChild("email").equalTo(email);

        checkuserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDb = userSnapshot.child("password").getValue(String.class);
                        if (passwordFromDb != null && passwordFromDb.equals(password)) {
                            // Correct password, navigate to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close LoginActivity
                        } else {
                            Errortxt.setText("Wrong Password");
                        }
                    }
                } else {
                    Errortxt.setText("User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Errortxt.setText("Database Error: " + error.getMessage());
            }
        });
    }
}
