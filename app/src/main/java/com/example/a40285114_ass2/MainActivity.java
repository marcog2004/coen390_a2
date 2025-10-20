package com.example.a40285114_ass2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private ListView listView;
    private FloatingActionButton floatingActionButton;

    private boolean displayByName = true;
    private ArrayAdapter<String> adapter;
    private final List<Integer> idIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbarMainActivity);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Main Activity");

        summaryTextView = findViewById(R.id.summaryTextView);
        listView = findViewById(R.id.listView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addProfileDialog dialog = new addProfileDialog();
                dialog.show(getSupportFragmentManager(), "AddProfileFragment");
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int profileId = idIndex.get(position);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);
        });

        updateList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

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

        String displayMode = displayByName ? "By Name (Surname, Name)" : "By ID";
        summaryTextView.setText("Profiles: " + db.getAmtProfiles() + " | Display: " + displayMode);
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
            invalidateOptionsMenu();  // FIX: not invalidateMenu()
            updateList();             // or updateList()
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

