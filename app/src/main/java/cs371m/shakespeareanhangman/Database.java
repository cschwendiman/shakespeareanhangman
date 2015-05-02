package cs371m.shakespeareanhangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by mjh2793 on 5/2/15.
 */
public class Database
{
    private static final String DATABASE_NAME = "Profiles";
    private SQLiteDatabase database;
    private SQLiteOpenHelper databaseOpenHelper;

    public Database(Context context)
    {
        databaseOpenHelper = new SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

            @Override
            public void onCreate(SQLiteDatabase db) {

                String createQuery = "CREATE TABLE profiles" +
                        "(_id INTEGER PRIMARY KEY autoincrement, " +
                        "name TEXT, " +
                        "highScore INTEGER, " +
                        "wins INTEGER, " +
                        "losses INTEGER, " +
                        "gamesPlayed INTEGER);";
                db.execSQL(createQuery);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }


    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close () {
        if (database != null)
            database.close();
    }

    public void insertProfile(String name, int highScore, int wins, int loss, int gamesPlayed) throws SQLException {
        ContentValues newProfile = new ContentValues();
        newProfile.put("name", name);
        newProfile.put("highScore", highScore);
        newProfile.put("wins", wins);
        newProfile.put("losses", loss);
        newProfile.put("gamesPlayed", gamesPlayed);

        open();
        database.insert("profiles", null, newProfile);
        close();
    }

}

