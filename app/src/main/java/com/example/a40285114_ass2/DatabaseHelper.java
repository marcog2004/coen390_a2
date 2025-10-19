package com.example.a40285114_ass2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE =
                "CREATE TABLE " + Config.TABLE_PROFILES + " (" +
                        Config.COLUMN_PROFILE_ID    + " INTEGER PRIMARY KEY, " +
                        Config.COLUMN_SURNAME       + " TEXT NOT NULL, " +
                        Config.COLUMN_NAME          + " TEXT NOT NULL, " +
                        Config.COLUMN_GPA           + " REAL NOT NULL, " +
                        Config.COLUMN_CREATION_TIME + " TEXT NOT NULL" +
                        ");";

        String CREATE_ACCESS =
                "CREATE TABLE " + Config.TABLE_ACCESS + " (" +
                        Config.COLUMN_ACCESS_ID         + " INTEGER NOT NULL, " +
                        Config.COLUMN_ACC_PROFILE_ID    + " INTEGER NOT NULL, " +
                        Config.COLUMN_TYPE              + " TEXT NOT NULL, " +
                        Config.COLUMN_TIMESTAMP         + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + Config.COLUMN_ACC_PROFILE_ID + ") REFERENCES " +
                        Config.TABLE_PROFILES + "(" + Config.COLUMN_PROFILE_ID + ")" +
                        ");";

        db.execSQL(CREATE_PROFILE);
        db.execSQL(CREATE_ACCESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_ACCESS);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_PROFILES);
        onCreate(db);
    }

    private String now(){
        return new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.CANADA).format(new Date());
    }

    public long addProfile(Profile profile){
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PROFILE_ID, profile.getProfileId());
        contentValues.put(Config.COLUMN_SURNAME, profile.getSurname());
        contentValues.put(Config.COLUMN_NAME, profile.getName());
        contentValues.put(Config.COLUMN_GPA, profile.getGpa());
        contentValues.put(Config.COLUMN_CREATION_TIME, now());

        try {
            id = db.insertOrThrow(Config.TABLE_PROFILES, null, contentValues);
        }catch (SQLiteException e){
            Toast.makeText(context, "Insert Profile Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public int deleteProfile(int profileId){
        int rows = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            addAccess(profileId, "deleted");
            rows = db.delete(Config.TABLE_PROFILES, Config.COLUMN_PROFILE_ID + "=?", new String[]{String.valueOf(profileId)});
        } catch (SQLiteException e){
            Toast.makeText(context, "Delete Profile Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return rows;
    }

    public Profile getProfileById(int profileId){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        Profile profile = null;
        try {
            cursor = db.query(Config.TABLE_PROFILES, null, Config.COLUMN_PROFILE_ID+"=?", new String[]{ String.valueOf(profileId) }, null, null, null);
            if (cursor != null && cursor.moveToFirst()){
                @SuppressLint("Range") int pId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROFILE_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NAME));
                @SuppressLint("Range") String surname = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SURNAME));
                @SuppressLint("Range") float gpa = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_GPA));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CREATION_TIME));
                profile = new Profile(pId, name, surname, gpa, time);
            }
        } catch (SQLiteException e){
            Toast.makeText(context, "Get Profile Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally { if (cursor != null) cursor.close(); }
        return profile;
    }

    public List<Profile> getProfilesByName(){
        List<Profile> profileListName = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try{
            cursor = db.query(Config.TABLE_PROFILES, null, null, null, null, null, Config.COLUMN_SURNAME + " COLLATE NOCASE ASC, " + Config.COLUMN_NAME + " COLLATE NOCASE ASC");

            if (cursor.moveToFirst()){
                do {
                    @SuppressLint("Range") int pId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROFILE_ID));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NAME));
                    @SuppressLint("Range") String surname = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SURNAME));
                    @SuppressLint("Range") float gpa = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_GPA));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CREATION_TIME));
                    Profile profile = new Profile(pId, name, surname, gpa, time);
                    profileListName.add(profile);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "Get Profiles by Name Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally { if (cursor != null) cursor.close(); }
        return profileListName;
    }

    public List<Profile> getProfilesById(){
        List <Profile> profileListId = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(Config.TABLE_PROFILES, null, null, null, null, null, Config.COLUMN_PROFILE_ID + " ASC");
            if (cursor.moveToFirst()){
                do{
                    @SuppressLint("Range") int pId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PROFILE_ID));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NAME));
                    @SuppressLint("Range") String surname = cursor.getString(cursor.getColumnIndex(Config.COLUMN_SURNAME));
                    @SuppressLint("Range") float gpa = cursor.getFloat(cursor.getColumnIndex(Config.COLUMN_GPA));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CREATION_TIME));
                    Profile profile = new Profile(pId, name, surname, gpa, time);
                    profileListId.add(profile);
                }while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, "Get Profiles by ID Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {if (cursor != null) cursor.close(); }
        return profileListId;
    }

    public int getAmtProfiles(){
        int amt = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + Config.TABLE_PROFILES, null);
            if (cursor.moveToFirst()) amt = cursor.getInt(0);
        } catch (SQLiteException e){
            Toast.makeText(context, "Amount Profiles Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally { if (cursor != null) cursor.close(); }
        return amt;
    }

    public long addAccess(int profileId, String type){
        long id = -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ACC_PROFILE_ID, profileId);
        contentValues.put(Config.COLUMN_TYPE, type);
        contentValues.put(Config.COLUMN_TIMESTAMP, now());
        try {
            id = db.insertOrThrow(Config.TABLE_ACCESS, null, contentValues);
        } catch (SQLiteException e){
            Toast.makeText(context, "Insert Access Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public List<Access> getAccessesForProfile(int profileId){
        List <Access> accessesForProfile = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(Config.TABLE_ACCESS, null, Config.COLUMN_ACC_PROFILE_ID + "=?", new String[]{ String.valueOf(profileId)}, null, null, Config.COLUMN_ACCESS_ID + " ASC");
            if (cursor.moveToFirst()){
                do {
                    @SuppressLint("Range") long accessId = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_ACCESS_ID));
                    @SuppressLint("Range") int pId = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ACC_PROFILE_ID));
                    @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(Config.COLUMN_TYPE));
                    @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex(Config.COLUMN_TIMESTAMP));
                    Access access = new Access(accessId, pId, type, timestamp);
                    accessesForProfile.add(access);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, "Get Accesses Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally { if (cursor != null) cursor.close(); }
        return accessesForProfile;
    }
}
