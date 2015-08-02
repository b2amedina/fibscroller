package com.xphonesoftware.fibscroller;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Main activity of the application
 *
 * Creates an infinitely scrollable list of fibonacci sequences
 *
 * Created by davidmedina on 8/1/15.
 */
public class MainActivity extends ListActivity {

    private static final String ADAPTER_COUNT = "adapterCount";

    private FibListViewAdapter mAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int count = -1;
        if (savedInstanceState != null) {
            // retrieve saved state
            count = savedInstanceState.getInt(ADAPTER_COUNT);
        }

        mAdapter = new FibListViewAdapter(this, count);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(ADAPTER_COUNT, mAdapter.getCount());
    }
}

