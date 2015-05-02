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

    private long id;
    private String name;
    private int highScore;
    private int wins;
    private int losses;
    private int gamesPlayed;
    private byte[] image;

    public Profile()
    {
        name = "New Profile";
        highScore = 0;
        wins = 0;
        losses = 0;
        gamesPlayed = 0;
        image = new byte[0];
    }

    public Profile(String name, int highScore, int wins, int losses, int gamesPlayed, byte[] image)
    {
        this.name = name;
        this.highScore = highScore;
        this.wins = wins;
        this.losses = losses;
        this.gamesPlayed = gamesPlayed;
        this.image = image;
    }

    public void setId(long id) {this.id = id;}

    public void setName(String name){
        this.name = name;
    }

    public void setHighScore(int highScore){
        this.highScore = highScore;
    }

    public void setWins(int wins){
        this.wins = wins;
    }

    public void setLosses(int losses){
        this.losses = losses;
    }

    public void setGamesPlayed(int gamesPlayed){
        this.gamesPlayed = gamesPlayed;
    }

    public void setImage(byte[] image) { this.image = image; }

    public long getId() { return id; }

    public String getName() { return name; }

    public int getHighScore() { return highScore; }

    public int getWins() { return wins; }

    public int getLosses() { return losses; }

    public int getGamesPlayed() { return gamesPlayed; }

    public byte[] getImage() { return image; }
}
