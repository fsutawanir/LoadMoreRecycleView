package nu.aing.loadmorerecycleview.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import nu.aing.loadmorerecycleview.R;
import nu.aing.loadmorerecycleview.model.Country;
import nu.aing.loadmorerecycleview.task.IEvent;
import nu.aing.loadmorerecycleview.task.LoadTask;

/**
 * @author Fanny Irawan Sutawanir (fannyirawans@gmail.com)
 */
public class MainActivity extends Activity {

    int start = 0;
    MainAdapter mDataAdapter = null;
    RecyclerView mRecyclerView;

    private int previousTotal = 0;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;

    /**
     * android:id="@id/android:empty"
     */
    TextView mEmptyLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rec_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean loading = true;

            @Override
            public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
                Log.d("Aing", "dx:"+dx+", dy:"+dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached

                    Log.i("...", "end called");

                    // Do something
                    new LoadTask(MainActivity.this, start).execute();

                    loading = true;
                }
            }
        });

        mEmptyLabel = (TextView) findViewById(android.R.id.empty);
        mDataAdapter = new MainAdapter(this, new ArrayList<Country>() );

        //prepareRecyclerView();
        new LoadTask(this, start).execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Invoked by EventBus.
     *
     * @param event -
     */
    @SuppressWarnings(value = "unused")
    public void onEventAsync(final IEvent event) {
        start += event.getData().size();
        int i = 0;
        for(final String item : event.getData() ) {
            final Country country = new Country();
            country.setCode(mDataAdapter.getItemCount() + "");
            country.setName(item);
            country.setContinent("continent(" + i + ")");
            country.setPopulation(i);

            mDataAdapter.add(country);

            i++;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setAdapter(mDataAdapter);
                showRecyclerView(mDataAdapter.getItemCount() > 0);
                event.closeDialog();
            }
        });
    }

    /**
     *
     * @param show
     */
    private final void showRecyclerView(final boolean show) {
        mRecyclerView.setVisibility(show ? View.VISIBLE : View.GONE);
        mEmptyLabel.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}