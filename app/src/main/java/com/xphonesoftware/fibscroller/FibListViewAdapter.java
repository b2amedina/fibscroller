package com.xphonesoftware.fibscroller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.math.BigInteger;

/**
 * Adapter responsible for providing pages of fibonacci sequences for a listview
 *
 * It caches values to ensure scrolling is as smooth as possible
 *
 * Created by davidmedina on 8/1/15.
 */
public class FibListViewAdapter extends BaseAdapter implements ListAdapter {

    private Context mContext;
    private ScrollableFibGen mScrollableFibGen;

    // cache variables
    // use 2 pages to simplify handling boundary conditions
    private int mPageStart0;
    private BigInteger[] mPageData0;
    private BigInteger[] mPageData1;
    private int mCount;

    static class ViewHolder {
        public TextView text;
    }

    public FibListViewAdapter(Context context, int count) {
        mContext = context.getApplicationContext();
        mScrollableFibGen = ((FibApplication)mContext).getScrollableFibGen();
        if (count != -1)  {
            // set the count if specified, this happens during config changes
            mCount = count;
        }
        loadPages(0);
    }

    /**
     * Load our cache with sequences
     *
     * @param position
     */
    private void loadPages(int position) {
        mPageStart0 = position;
        mPageData0 = mScrollableFibGen.generateRange(mPageStart0);
        mPageData1 = mScrollableFibGen.generateRange(mPageStart0 + mPageData0.length);
        int maxPos = mPageStart0 + mPageData1.length + mPageData1.length;
        if (maxPos > mCount) mCount = maxPos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCount + 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Object getItem(int position) {
        if (position < mPageStart0) {
            // if the requested position is before our first cached seq
            loadPages((position / mPageData0.length) * mPageData0.length);
            notifyDataSetChanged();

        } else if (position >= (mPageStart0 + mPageData0.length + mPageData1.length)) {
            // if the requested position is after our last cached seq
            loadPages(position);
            notifyDataSetChanged();
        }

        String item;
        if ((position >= mPageStart0 && (position < (mPageStart0 + mPageData0.length)))) {
            // return seq from the first cache page
            item = mPageData0[position - mPageStart0].toString();

        } else if ((position >= mPageStart0 && (position <
                (mPageStart0 + mPageData0.length + mPageData1.length)))) {
            // return seq from the second cache page
            item = mPageData1[position - mPageData0.length - mPageStart0].toString();

        } else {
            throw new IllegalStateException();
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            // if the view does not already exist
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(viewHolder);
        }

        // set data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        String value = "fib(" + position + ") = " + getItem(position);
        holder.text.setText(value);

        return rowView;
    }
}
