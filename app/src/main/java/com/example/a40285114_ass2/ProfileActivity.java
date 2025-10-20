package com.example.a40285114_ass2;

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

    private TextView nameTextView, surnameTextView, idTextView, gpaTextView, headerTextView, createdTextView;
    private ListView accessListView;
    private ArrayAdapter<String> accessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarProfileActivity);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile Activity");

        db = new DatabaseHelper(getApplicationContext());
        profileId = getIntent().getIntExtra("profileId", -1);

        db.addAccess(profileId, "opened");

        nameTextView = findViewById(R.id.nameTextView);
        surnameTextView = findViewById(R.id.surnameTextView);
        idTextView = findViewById(R.id.idTextView);
        gpaTextView = findViewById(R.id.gpaTextView);
        headerTextView = findViewById(R.id.headerTextView);
        accessListView = findViewById(R.id.accessListView);
        createdTextView = findViewById(R.id.createdTextView);
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
            db.addAccess(profileId, "profile deleted");  // or "deleted"
            db.deleteProfile(profileId);                 // keep history
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
            db.addAccess(profileId, "closed");
        }
    }
}