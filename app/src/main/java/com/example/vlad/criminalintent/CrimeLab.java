package com.example.vlad.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private List<Crime> crimes;

    private CrimeLab(Context context){
        crimes = new ArrayList<>();
    }

    public static CrimeLab getInstance(Context context) {
        if (crimeLab == null){
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    public List<Crime> getCrimes() {
        return crimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime: crimes) {
            if (crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    public boolean addCrime(Crime crime) {
        return crimes.add(crime);
    }

    public boolean deleteCrime(UUID id) {
        for (Crime crime: crimes) {
            if (crime.getId().equals(id)) {
                crimes.remove(crime);
                return true;
            }
        }
        return false;
    }
}
