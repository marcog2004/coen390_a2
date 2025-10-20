package com.example.a40285114_ass2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar = null;

    private TextView summaryTextView;

    private boolean displayByName = true;
    private ArrayAdapter<String> adapter;
    private final List<Integer> idIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // set up toolbar
        toolbar = findViewById(R.id.toolbarMainActivity);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Main Activity");

        summaryTextView = findViewById(R.id.summaryTextView);
        ListView listView = findViewById(R.id.listView);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(view -> {
            addProfileDialog dialog = new addProfileDialog();
            dialog.show(getSupportFragmentManager(), "AddProfileFragment");
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int profileId = idIndex.get(position);
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("profileId", profileId);
            startActivity(i);
        });

        updateList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @SuppressLint("SetTextI18n")
    public void updateList(){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Profile> profiles = displayByName ? db.getProfilesByName() : db.getProfilesById();

        List<String> rows = new ArrayList<>();
        idIndex.clear();
        int line = 1;
        for (Profile p : profiles) {
            String row = displayByName
                    ? (line + ". " + p.getSurname() + ", " + p.getName())
                    : (line + ". " + p.getProfileId());
            rows.add(row);
            idIndex.add(p.getProfileId());
            line++;
        }

        adapter.clear();
        adapter.addAll(rows);
        adapter.notifyDataSetChanged();

        String displayMode = displayByName ? "by Surname" : "by ID";
        if (db.getAmtProfiles() == 1){
            summaryTextView.setText(db.getAmtProfiles() + " Profile, "  + displayMode);
        }else{
            summaryTextView.setText(db.getAmtProfiles() + " Profiles, "  + displayMode);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem toggle = menu.findItem(R.id.display_toggle);
        if (toggle != null) toggle.setTitle(displayByName ? "Display by ID" : "Display by Name");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.display_toggle) {
            displayByName = !displayByName;
            invalidateOptionsMenu();
            updateList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

