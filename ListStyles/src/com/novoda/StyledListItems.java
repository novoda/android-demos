package com.novoda;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StyledListItems extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        setListAdapter(new StyledListItemAdapter(this));
    }
    
    
    private class StyledListItemAdapter extends BaseAdapter {
        public StyledListItemAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return mTitles.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                sv = new StyledItemView(mContext, mTitles[position]);
            } else {
                sv = (StyledItemView) convertView;
            }

            sv.setContent(mTitles[position]);
            return sv;
        }

        private Context mContext;
        
        private String[] mTitles = 
        {
                "lorem dipsum",   
                "lorem dipsum",
                "lorem dipsum",       
                "lorem dipsum",
                "lorem dipsum",
                "lorem dipsum",  
                "lorem dipsum",
                "lorem dipsum"
        };
        
    }
    
    private class StyledItemView extends LinearLayout {
        private LayoutInflater mInflater;

		public StyledItemView(Context context, String title) {
            super(context);
            this.setOrientation(VERTICAL);
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout layout = (LinearLayout)mInflater.inflate(R.layout.list_item, null);
            mTitle = (TextView)layout.findViewById(R.id.txt_item);
            addView(layout);
        }

        public void setContent(String title) {
            mTitle.setText(title);
        }

        private TextView mTitle;
    }
    
    StyledItemView sv;
    
}