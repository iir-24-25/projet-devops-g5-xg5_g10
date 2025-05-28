package com.example.don_de_son;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.don_de_son.adapter.UserAdapter;
import com.example.don_de_son.databinding.ActivityMainBinding;
import com.example.don_de_son.model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<UserModel> list;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        adapter = new UserAdapter(this, list);

        // Set the adapter BEFORE fetching data to avoid rendering issues
        binding.recyclerView.setAdapter(adapter);
        binding.progressBarId.setVisibility(View.VISIBLE); // Ensure visibility before loading

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    if (userModel != null) { // Prevent null data from causing errors
                        list.add(userModel);
                    }
                }

                adapter.notifyDataSetChanged();
                binding.progressBarId.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.e("MainActivity", "Database error: " + error.getMessage());
                binding.progressBarId.setVisibility(View.GONE);
            }
        });
    }
}