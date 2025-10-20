package com.example.a40285114_ass2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class addProfileDialog extends DialogFragment {
    protected EditText nameEditText, surnameEditText, idEditText, gpaEditText;
    protected Button cancelButton, saveButton;

    public addProfileDialog(){
        // required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_add_profile_dialog, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        surnameEditText = view.findViewById(R.id.surnameEditText);
        idEditText = view.findViewById(R.id.idEditText);
        gpaEditText = view.findViewById(R.id.gpaEditText);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    String name = nameEditText.getText().toString().trim();
                    String surname = surnameEditText.getText().toString().trim();
                    String idString = idEditText.getText().toString().trim();
                    String gpaString = gpaEditText.getText().toString().trim();

                    int profileId;
                    try{
                        profileId = Integer.parseInt(idString);
                    }catch (Exception e){
                        profileId = -1;
                    }

                    float gpa;
                    try {
                        gpa = Float.parseFloat(gpaString);
                    } catch (Exception e){
                        gpa = -1f;
                    }

                    if (name.isEmpty() || surname.isEmpty() || idString.isEmpty() || gpaString.isEmpty()){
                        Toast.makeText(getContext().getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_LONG).show();
                    }else
                    if (!name.matches("[A-Za-z]+") || !surname.matches("[A-Za-z]+")){
                        Toast.makeText(getContext().getApplicationContext(), "Name/Surname must be letters only", Toast.LENGTH_LONG).show();
                    }else
                    if (profileId < 10000000 || profileId > 99999999){
                        Toast.makeText(getContext().getApplicationContext(), "ID Must be 8 Digits", Toast.LENGTH_LONG).show();
                    }else
                    if (gpa < 0f || gpa > 4.3f){
                        Toast.makeText(getContext().getApplicationContext(), "GPA Must be between 0.0 and 4.3", Toast.LENGTH_LONG).show();
                    }else{
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext().getApplicationContext());
                        dbHelper.addProfile(new Profile(profileId, name, surname, gpa, ""));
                        Toast.makeText(getContext().getApplicationContext(), "Added Profile Successfully", Toast.LENGTH_LONG).show();
                        if (getActivity() != null) ((MainActivity)getActivity()).updateList();
                        dismiss();
                    }
            }
        });
        return view;
    }
}
