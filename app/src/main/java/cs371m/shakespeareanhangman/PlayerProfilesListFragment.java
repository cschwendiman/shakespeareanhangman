package cs371m.shakespeareanhangman;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class PlayerProfilesListFragment extends ListFragment {
    private static final String TAG = "Profiles List Fragment";

    private PlayerProfileAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, " onCreate");
        super.onCreate(savedInstanceState);
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Profile> profiles = dbHelper.getAllProfiles();
        adapter = new PlayerProfileAdapter(getActivity(), R.layout.profile_row_view, profiles);
        adapter.addAll(profiles);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        Log.d(TAG, " onResume");
        super.onResume();
        adapter.clear();
        DBHelper dbHelper = new DBHelper(getActivity());
        List<Profile> profiles = dbHelper.getAllProfiles();
        adapter.addAll(profiles);
        adapter.notifyDataSetChanged();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, " list item click!. position: " + position + " id: " + id);
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
