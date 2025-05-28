package com.example.don_de_son.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.don_de_son.MainActivity;
import com.example.don_de_son.R;
import com.example.don_de_son.databinding.ActivityReproBinding;
import com.example.don_de_son.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ReproActivity extends AppCompatActivity {
    private ActivityReproBinding binding;

    private String name, email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReproBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.registerButton.setOnClickListener(v -> {
            name = Objects.requireNonNull(binding.inputName.getText()).toString();

            email = Objects.requireNonNull(binding.inputEmail.getText()).toString();
            password = Objects.requireNonNull(binding.inputPassword.getText()).toString();

            if (name.isEmpty()) {
                binding.inputName.setError("Name is required");
                binding.inputName.requestFocus();
            } else if (email.isEmpty()) {
                binding.inputEmail.setError("Email is required");
                binding.inputEmail.requestFocus();
            } else if (password.isEmpty()) {
                binding.inputPassword.setError("Password is required");
                binding.inputPassword.requestFocus();
            } else {
                auth = FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createUser();
                    }
                });
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createUser() {
        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(currentUser);

        UserModel userModel = new UserModel(name, email, password);

        databaseReference.setValue(userModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}