package com.novoda;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

public class DynamicListItems extends ListActivity {
	private static final String			ITEM_KEY	= "key";
	ArrayList<HashMap<String, String>>	list		= new ArrayList<HashMap<String, String>>();
	private SimpleAdapter				adapter;
	private EditText					newValue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_list);
		newValue = (EditText) findViewById(R.id.new_value_field);

		setListAdapter(new SimpleAdapter(this, list, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value }));
		((ImageButton) findViewById(R.id.button)).setOnClickListener(getBtnClickListener());
	}

	private OnClickListener getBtnClickListener() {
		return new OnClickListener() {
			public void onClick(View view) {
				try {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put(ITEM_KEY, newValue.getText().toString());
					list.add(item);
					adapter.notifyDataSetChanged();
				} catch (NullPointerException e) {
					Log.i("[Dynamic Items]", "Tried to add null value");
				}
			}
		};
	}
}