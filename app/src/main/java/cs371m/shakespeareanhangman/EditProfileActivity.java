package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;


public class EditProfileActivity extends Activity {

    private long id;
    private Profile p;
    private EditText e;
    String TAG = "edit";
    DBHelper database;

    private int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        database = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getLong("profile_id");
        }
        Log.d(TAG, "PASSED ID " + id);
        p = database.getProfile(id);

        e = (EditText) findViewById(R.id.editProfileText);

        String name = p.getName();
        e.setHint(name);
        e.setText(name);

        if(name.equals("Default"))
        {
            Button btn = (Button) findViewById(R.id.delete_profile);
            btn.setEnabled(false);
        }
    }


    public void buttonClick(View view) throws SQLException {
        Log.d(TAG, " Button Clicked");
        switch (view.getId()) {
            case R.id.cancel_edit_profile_button:
                Log.d(TAG, "cancelling adding new profile");
                finish();
                break;

            case R.id.commit_edit_profile_button:
                Log.d(TAG, "adding new profile");
                String name = e.getText().toString();
                if (name.equals("") || name.equals(null))
                {
                    Toast.makeText(getApplicationContext(), "must enter a name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                saveProfile();
                finish();
                break;
            case R.id.delete_profile:
                database.deleteProfile(database.getProfile(id));
                finish();
                break;
            case R.id.edit_profile_photo_button:
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

                // Useful stackoverflow post: http://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview

                // Get image's rotation
                ExifInterface exif = new ExifInterface(getRealPathFromURI(uri, this));
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                Log.d(TAG, "Orientation is " + orientation);

                Bitmap d = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                // Rotate the image if it was taken vertically
                if(orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    d = Bitmap.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), matrix, true);
                }

                ImageView imageView = (ImageView) findViewById(R.id.edited_profile_pic);

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

    // Source: http://stackoverflow.com/questions/19960790/exifinterface-returns-null-for-all-tags
    private String getRealPathFromURI(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    private void saveProfile() throws SQLException {

        String name = e.getText().toString();
        p.setName(name);
        database.updateProfile(p);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
