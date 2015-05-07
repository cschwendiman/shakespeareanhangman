package cs371m.shakespeareanhangman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;


public class AddNewProfileActivity extends Activity {

    String TAG = "Add New Profile Activity";
    EditText editText;
    ImageView imageView;
    Profile newProfile;
    private SharedPreferences prefs;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_profile);
        newProfile = new Profile();
        editText = (EditText) findViewById(R.id.profile_title);
        imageView = (ImageView) findViewById(R.id.profile_image);
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
                imageView.setImageBitmap(profileImage);

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

        String name = editText.getText().toString();

        byte[] byteArray = new byte[0];
        if (null != profileImage) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profileImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        DBHelper database = new DBHelper(this);
        newProfile = database.createProfile(name, byteArray);
    }



}
