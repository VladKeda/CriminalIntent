package com.example.vlad.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private Crime crime;
    private EditText titleField;
    private Button dateButton;
    private Button timeButton;
    private CheckBox solvedCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        titleField = (EditText) view.findViewById(R.id.crime_title);
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment fragment = DatePickerFragment.newInstance(crime.getDate());
                fragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                fragment.show(manager, DIALOG_DATE);
            }
        });

        timeButton = (Button) view.findViewById(R.id.crime_time);
        updateTime();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment fragment = TimePickerFragment.newInstance(crime.getDate());
                fragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                fragment.show(manager, DIALOG_TIME);
            }
        });

        solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked(crime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            updateTime();
        }
    }

    private void updateDate() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        dateButton.setText(dateFormat.format(crime.getDate()));
    }

    private void updateTime() {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        timeButton.setText(dateFormat.format(crime.getDate()));
    }

    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }
}
