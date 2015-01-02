package nu.aing.loadmorerecycleview.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import nu.aing.loadmorerecycleview.R;
import nu.aing.loadmorerecycleview.model.Country;

/**
 * @author Fanny Irawan Sutawanir (fannyirawans@gmail.com)
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Country> countryList;

    public MainAdapter(Context context, ArrayList<Country> countryList) {
        mContext = context;
        this.countryList = new ArrayList<Country>();
        this.countryList.addAll(countryList);
    }

    public void add(Country country) {
        Log.v("AddView", country.getCode());
        this.countryList.add(country);
    }

    public Country get(final int position) {
        return countryList.get(position);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        final View itemView = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Country country = get(position);
        viewHolder.mCode.setText(country.getCode());
        viewHolder.mName.setText(country.getName());
        viewHolder.mContinent.setText(country.getContinent());
        viewHolder.mRegion.setText(country.getRegion());

        viewHolder.mClickListener = new IViewHolderClicks() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, country.getCode(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     *
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected final TextView mCode;
        protected final TextView mName;
        protected final TextView mContinent;
        protected final TextView mRegion;

        public IViewHolderClicks mClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            mCode = (TextView) itemView.findViewById(R.id.code);
            mName = (TextView) itemView.findViewById(R.id.name);
            mContinent = (TextView) itemView.findViewById(R.id.continent);
            mRegion = (TextView) itemView.findViewById(R.id.region);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(view);
        }
    }

    /**
     *
     */
    private interface IViewHolderClicks {
        public void onClick(View view);
    }
}
