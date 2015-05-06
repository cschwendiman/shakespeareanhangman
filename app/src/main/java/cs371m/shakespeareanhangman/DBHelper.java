package cs371m.shakespeareanhangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cassie on 5/2/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DB Helper";

    public static final String DATABASE_NAME = "shakespeareanhangman.db";
    public static final String PROFILES_TABLE_NAME = "profiles";
    public static final String PROFILES_COLUMN_ID = "id";
    public static final String PROFILES_COLUMN_NAME = "name";
    public static final String PROFILES_COLUMN_WINS = "wins";
    public static final String PROFILES_COLUMN_LOSSES = "losses";
    public static final String PROFILES_COLUMN_GAMES_PLAYED = "games_played";
    public static final String PROFILES_COLUMN_IMAGE = "image";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, " Created profiles database.");
        db.execSQL(
                "create table profiles " +
                        "(id integer primary key, name text, wins integer, losses integer, games_played integer, image blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, " Upgraded profiles database.");
        db.execSQL("DROP TABLE IF EXISTS profiles");
        onCreate(db);
    }

    public Profile createProfile(String name, byte[] image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROFILES_COLUMN_NAME, name);
        contentValues.put(PROFILES_COLUMN_WINS, 0);
        contentValues.put(PROFILES_COLUMN_LOSSES, 0);
        contentValues.put(PROFILES_COLUMN_GAMES_PLAYED, 0);
        contentValues.put(PROFILES_COLUMN_IMAGE, image);

        long id = db.insert("profiles", null, contentValues);
        Log.d(TAG, " Profile created for '" + name + "'");
        return getProfile(id);
    }

    public void deleteProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = profile.getId();
        Log.d(TAG, " Profile created for '" + profile.getName() + "'");
        db.delete(PROFILES_TABLE_NAME, PROFILES_COLUMN_ID
                + " = " + id, null);
    }

    public Profile getProfile(long id){
        Log.d(TAG, " Get profile.");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from profiles where id="+id+"", null );
        cursor.moveToFirst();
        Profile profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public boolean updateProfile(Profile profile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILES_COLUMN_NAME, profile.getName());
        contentValues.put(PROFILES_COLUMN_WINS, profile.getWins());
        contentValues.put(PROFILES_COLUMN_LOSSES, profile.getLosses());
        contentValues.put(PROFILES_COLUMN_GAMES_PLAYED, profile.getGamesPlayed());
        contentValues.put(PROFILES_COLUMN_IMAGE, profile.getImage());
        db.update("profiles", contentValues, "id = ? ", new String[] { Long.toString(profile.getId()) } );
        Log.d(TAG, " Profile updated for '" + profile.getName() + "'");
        return true;
    }

    public List<Profile> getAllProfiles() {
        Log.d(TAG, " Get all profiles.");
        SQLiteDatabase db = this.getWritableDatabase();
        List<Profile> profiles = new ArrayList<Profile>();

        Cursor cursor = db.rawQuery( "select * from profiles", null );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Profile profile = cursorToProfile(cursor);
            profiles.add(profile);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return profiles;
    }

    private Profile cursorToProfile(Cursor cursor) {
        if (cursor.getCount() == 0)
            return null;
        Profile profile = new Profile();
        profile.setId(cursor.getLong(0));
        profile.setName(cursor.getString(1));
        profile.setWins(cursor.getInt(2));
        profile.setLosses(cursor.getInt(3));
        profile.setGamesPlayed(cursor.getInt(4));
        profile.setImage(cursor.getBlob(5));
        return profile;
    }
}
