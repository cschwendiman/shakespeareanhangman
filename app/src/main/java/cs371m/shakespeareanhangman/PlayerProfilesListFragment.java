package cs371m.shakespeareanhangman;


import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class PlayerProfilesListFragment extends ListFragment {
    private static final String TAG = "Profiles List Fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " onCreate");
        super.onCreate(savedInstanceState);
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Profile> profiles = dbHelper.getAllProfiles();
        PlayerProfileAdapter adapter = new PlayerProfileAdapter(getActivity(),
                profiles.toArray(new Profile[profiles.size()]));
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, " list item click!");
    }
}