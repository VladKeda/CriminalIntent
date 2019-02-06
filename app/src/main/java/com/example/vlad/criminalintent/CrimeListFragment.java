package com.example.vlad.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView recyclerView;
    private CrimeAdapter adapter;
//    private int lastClickPosition;


    private class CrimeHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener{
        private TextView titleTextView;
        private TextView dateTextView;
        private ImageView solvedImageView;
        private Crime crime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            itemView.setOnClickListener(this);

            titleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            dateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            solvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            this.crime = crime;
            titleTextView.setText(this.crime.getTitle());
            dateTextView.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(this.crime.getDate()));
            solvedImageView.setVisibility(this.crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
//            lastClickPosition = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
            startActivity(intent);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> crimes;

        public CrimeAdapter(List<Crime> crimes) {
            this.crimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {
            Crime crime = crimes.get(i);
            crimeHolder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return crimes.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        if (adapter == null) {
            CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
            List<Crime> crimes = crimeLab.getCrimes();
            adapter = new CrimeAdapter(crimes);
            recyclerView.setAdapter(adapter);
        } else {
//            adapter.notifyItemChanged(lastClickPosition);
            adapter.notifyDataSetChanged();
        }

    }
}
