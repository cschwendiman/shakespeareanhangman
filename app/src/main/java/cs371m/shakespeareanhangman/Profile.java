package cs371m.shakespeareanhangman;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import cs371m.shakespeareanhangman.Profile;
import android.content.SharedPreferences;

/**
 * Created by mjh2793 on 5/1/15.
 */
public class Profile {

    private String profName;
    private int highScore;
    private int wins;
    private int losses;
    private int gamesPlayed;

    public Profile()
    {
        profName = "New Profile";
        highScore = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
    }

    public Profile(String name, int highSc, int w, int l, int gPlayed)
    {
        profName = name;
        highScore = highSc;
        wins = w;
        losses = l;
        gamesPlayed = gPlayed;
    }

    public void setProfName(String name){
        profName = name;
    }

    public void setHighScore(int in){
        highScore = in;
    }

    public void setWins(int in){
        wins = in;
    }

    public void setLosses(int in){
        losses = in;
    }

    public void setGamesPlayed(int in){
        gamesPlayed = in;
    }


}
