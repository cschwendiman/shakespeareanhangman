package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;


public class EditProfileActivity extends Activity {

    private long id;
    private Profile p;
    private EditText e;
    private ImageView i;
    String TAG = "edit";
    DBHelper database;
    Bitmap profileImage;

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
        e.setHint(p.getName());
        e.setText(p.getName());

        i = (ImageView) findViewById(R.id.profile_image);
        byte[] byteArray = p.getImage();
        if (byteArray.length > 0) {
            profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            i.setImageBitmap(profileImage);
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
                SharedPreferences prefs = getSharedPreferences("shake_prefs", MODE_PRIVATE);
                if(id == prefs.getLong("currentPlayerId", id)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Cannot delete profile because it is selected in Options.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    database.deleteProfile(database.getProfile(id));
                    finish();
                }
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

                profileImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // Log.d(TAG, String.valueOf(bitmap));

                // Rotate the image if it was taken vertically
                if(orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    profileImage = Bitmap.createBitmap(profileImage, 0, 0, profileImage.getWidth(), profileImage.getHeight(), matrix, true);
                }

                // Credit: http://stackoverflow.com/questions/10271020/bitmap-too-large-to-be-uploaded-into-a-texture
                // Scale the image so it's not too big to upload
                int nh = (int) ( profileImage.getHeight() * (512.0 / profileImage.getWidth()) );
                profileImage = Bitmap.createScaledBitmap(profileImage, 512, nh, true);
                Log.d(TAG, "Scaled image");

                // Put the image into the ImageView
                i.setImageBitmap(profileImage);

            } catch (Exception e) {
                Log.d(TAG, "In Exception");
                Toast.makeText(getApplicationContext(), "Oops! Image selection does not work on your device.",
                        Toast.LENGTH_SHORT).show();
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
            if (idx < 0) {
                return contentURI.getPath();
            }
            return cursor.getString(idx);
        }
    }


    private void saveProfile() throws SQLException {
        Log.d(TAG, "ATTEMPTING SAVE");
        String name = e.getText().toString();
        p.setName(name);

        if (null != profileImage) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profileImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            p.setImage(byteArray);
        }

        database.updateProfile(p);
    }
}
