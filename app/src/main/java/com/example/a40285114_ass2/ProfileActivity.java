package com.example.a40285114_ass2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar = null;

    private int profileId;
    private DatabaseHelper db;

    private ArrayAdapter<String> accessAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // set up toolbar
        toolbar = findViewById(R.id.toolbarProfileActivity);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Activity");

        db = new DatabaseHelper(getApplicationContext());
        profileId = getIntent().getIntExtra("profileId", -1);

        db.addAccess(profileId, "Opened");

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView surnameTextView = findViewById(R.id.surnameTextView);
        TextView idTextView = findViewById(R.id.idTextView);
        TextView gpaTextView = findViewById(R.id.gpaTextView);
        TextView headerTextView = findViewById(R.id.headerTextView);
        ListView accessListView = findViewById(R.id.accessListView);
        TextView createdTextView = findViewById(R.id.createdTextView);
        Button deleteButton = findViewById(R.id.deleteButton);


        accessAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        accessListView.setAdapter(accessAdapter);

        Profile p = db.getProfileById(profileId);
        if (p != null){
            nameTextView.setText("Name: " + p.getName());
            surnameTextView.setText("Surname: "+ p.getSurname());
            idTextView.setText("ID: " + p.getProfileId());
            gpaTextView.setText("GPA: " + p.getGpa());
            createdTextView.setText("Profile created: " + p.getCreationTime());
        }
        headerTextView.setText("Access History");

        refreshAccess();

        deleteButton.setOnClickListener(v -> {
            db.addAccess(profileId, "profile deleted");
            db.deleteProfile(profileId);
            finish();

        });

    }

    private void refreshAccess(){
        accessAdapter.clear();
        for (Access a : db.getAccessesForProfile(profileId)){
            accessAdapter.add(a.getTimestamp() + " - " + a.getType());
        }
        accessAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (isFinishing()){
            db.addAccess(profileId, "Closed");
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}