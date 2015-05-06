package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class AddNewProfileActivity extends Activity {

    String TAG = "Add New Profile Activity";
    EditText editText;
    Profile newProfile;
    private SharedPreferences prefs;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);
        newProfile = new Profile();
        editText = (EditText) findViewById(R.id.profile_title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_profile, menu);
        return true;
    }

    public void buttonClick(View view) throws SQLException {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.cancel_add_new_profile_button:
                Log.d(TAG, "cancelling adding new profile");
                finish();
                break;

            case R.id.commit_add_new_profile_button:
                Log.d(TAG, "adding new profile");
                String name = editText.getText().toString();
                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "must enter a name",
                    Toast.LENGTH_SHORT).show();
                    return;
                }
                saveProfile();
                finish();
                break;
            case R.id.choose_profile_photo_button:
                Log.d(TAG, "Trying to add photo");
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {

                Bitmap d = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.profile_pic);

                // Scale the image so it's not too big to upload
                int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
                Log.d(TAG, "Scaled image");

                // Put the image into the ImageView
                imageView.setImageBitmap(scaled);

            } catch (IOException e) {
                Log.d(TAG, "In Exception");
                e.printStackTrace();
            }
        }
    }

    private void saveProfile() throws SQLException {

        String name = editText.getText().toString();
        newProfile.setName(name);
        DBHelper database = new DBHelper(this);
        database.createProfile(newProfile.getName(), new byte[0]);
    }



}
