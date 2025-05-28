package com.example.don_de_son.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.don_de_son.MainActivity;
import com.example.don_de_son.R;
import com.example.don_de_son.databinding.ActivityDonaterBinding;
import com.example.don_de_son.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonaterActivity extends AppCompatActivity {
    private ActivityDonaterBinding binding;
    private String name, phone, email, password;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDonaterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(currentUser);

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.inputName.getText().toString();
                phone = binding.inputNumber.getText().toString();
                email = binding.inputEmail.getText().toString();
                password = binding.inputPassword.getText().toString();
                if (name.isEmpty()) {
                    binding.inputName.setError("Enter your name");
                    binding.inputName.requestFocus();
                } else if (phone.length() != 10) {
                    binding.inputNumber.setError("Enter valid phone number");
                    binding.inputNumber.requestFocus();
                } else if (email.isEmpty()) {
                    binding.inputEmail.setError("Enter your email");
                    binding.inputEmail.requestFocus();
                } else if (password.isEmpty()) {
                    binding.inputPassword.setError("Enter your password");
                    binding.inputPassword.requestFocus();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                inserData();
                            }
                        }
                    });
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void inserData() {
        UserModel userModel = new UserModel(name, phone, email, password);
        databaseReference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(DonaterActivity.this, MainActivity.class));
                    Toast.makeText(DonaterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}