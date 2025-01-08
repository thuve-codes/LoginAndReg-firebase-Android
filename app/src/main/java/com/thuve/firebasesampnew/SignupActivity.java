package com.thuve.firebasesampnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password, cpassword;
    TextView Logintxt;
    Button signup;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        cpassword = findViewById(R.id.confirmPasswordEditText);

        signup = findViewById(R.id.signUpButton);

        Logintxt=findViewById(R.id.loginText);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Users");

                String dbname = name.getText().toString();
                String dbemail = email.getText().toString().replaceAll("\\.", ","); // Replace '.' for Firebase
                String dbpass = password.getText().toString();
                String dbCpass = cpassword.getText().toString();

                // Validate input fields
                if (dbname.isEmpty() || dbemail.isEmpty() || dbpass.isEmpty() || dbCpass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!dbpass.equals(dbCpass)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create DBHelper object
                DBHelper helperClass = new DBHelper(dbname, dbemail, dbpass);

                // Save to Firebase
                myRef.child(dbemail).setValue(helperClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Close SignupActivity
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Go to Login page if user already have account
        Logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
