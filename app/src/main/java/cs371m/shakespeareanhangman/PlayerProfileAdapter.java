package cs371m.shakespeareanhangman;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayerProfileAdapter extends ArrayAdapter<Profile> {
    private static final String TAG = "Profiles Adapter";

    private final Context context;
    private final Profile[] profiles;

    public PlayerProfileAdapter(Context context, Profile[] profiles) {
        super(context, R.layout.profile_row_view, profiles);
        Log.d(TAG, " construct");
        this.context = context;
        this.profiles = profiles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.profile_row_view, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.profileName);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(profiles[position].getName());

        Log.d(TAG, " GET VIEW FOR ROW " + profiles[position].getName() + " PROFILE");
        return rowView;
    }
}
