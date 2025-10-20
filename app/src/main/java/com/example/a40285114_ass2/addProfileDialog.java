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

        nameEditText    = view.findViewById(R.id.nameEditText);
        surnameEditText = view.findViewById(R.id.surnameEditText);
        idEditText      = view.findViewById(R.id.idEditText);
        gpaEditText     = view.findViewById(R.id.gpaEditText);
        cancelButton      = view.findViewById(R.id.cancelButton);
        saveButton        = view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(view1 -> dismiss());

        saveButton.setOnClickListener(v -> {
            String name    = nameEditText.getText().toString().trim();
            String surname = surnameEditText.getText().toString().trim();
            String idStr   = idEditText.getText().toString().trim();
            String gpaStr  = gpaEditText.getText().toString().trim();

            if (name.isEmpty() || surname.isEmpty() || idStr.isEmpty() || gpaStr.isEmpty()) {
                Toast.makeText(requireContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!name.matches("[A-Za-z]+") || !surname.matches("[A-Za-z]+")) {
                Toast.makeText(requireContext(), "Name/Surname: letters only.", Toast.LENGTH_LONG).show();
                return;
            }
            int pid;
            try { pid = Integer.parseInt(idStr); } catch (Exception e) { pid = -1; }
            if (pid < 10000000 || pid > 99999999) {
                Toast.makeText(requireContext(), "ID must be 8 digits (10000000–99999999).", Toast.LENGTH_LONG).show();
                return;
            }
            float gpa;
            try { gpa = Float.parseFloat(gpaStr); } catch (Exception e) { gpa = -1f; }
            if (gpa < 0f || gpa > 4.3f) {
                Toast.makeText(requireContext(), "GPA must be between 0 and 4.3.", Toast.LENGTH_LONG).show();
                return;
            }


            DatabaseHelper db = new DatabaseHelper(requireContext());
            long result = db.addProfile(new Profile(pid, name, surname, gpa, ""));
            if (result == -1) {
                Toast.makeText(requireContext(), "Duplicate ID or DB error.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_LONG).show();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).updateList();
                }
                dismiss();
            }
        });

        return view;
    }
}
