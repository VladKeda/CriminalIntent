package com.example.vlad.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vlad.criminalintent.database.CrimeBaseHelper;
import com.example.vlad.criminalintent.database.CrimeCursorWrapper;
import com.example.vlad.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context context;
    private SQLiteDatabase database;

    private CrimeLab(Context c){
        this.context = c.getApplicationContext();
        this.database = new CrimeBaseHelper(this.context).getWritableDatabase();
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeTable.NAME,
                null, // all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null); // orderBy

        return new CrimeCursorWrapper(cursor);
    }

    public static CrimeLab getInstance(Context context) {
        if (crimeLab == null){
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                                                        new String[]{id.toString()});
        try {
            if (cursorWrapper.getCount() == 0){
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    public void updateCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        String uuidStr = crime.getId().toString();
        database.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?", new String[]{uuidStr});
    }



    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        database.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(UUID id) {
        database.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
    }
}
