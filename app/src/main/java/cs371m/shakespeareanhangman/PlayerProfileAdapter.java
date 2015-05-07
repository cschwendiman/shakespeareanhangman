package cs371m.shakespeareanhangman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PlayerProfileAdapter extends ArrayAdapter<Profile> {
    private static final String TAG = "Profiles Adapter";

    private final Context context;
    private final List<Profile> profiles;

    public PlayerProfileAdapter(Context context, int resource, List<Profile> profiles) {
        super(context, resource, profiles);
        Log.d(TAG, " construct");
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.profile_row_view, parent, false);

        Profile profile = profiles.get(position);

        TextView nameView = (TextView) rowView.findViewById(R.id.profileName);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        nameView.setText(profile.getName());

        TextView statsView = (TextView) rowView.findViewById(R.id.stats);
        String stats = profile.getWins() + " Wins | " + profile.getLosses() + " Losses";
        statsView.setText(stats);

        ImageView i = (ImageView) rowView.findViewById(R.id.profile_image);
        byte[] byteArray = profile.getImage();
        if (byteArray.length > 0) {
            Bitmap profileImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            i.setImageBitmap(profileImage);
        }

        Log.d(TAG, " GET VIEW FOR ROW " + profile.getName() + " PROFILE");
        return rowView;
    }
}
