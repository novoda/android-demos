package com.novoda;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class LongPressList extends ListActivity {

	private String[]	mStrings	= { "Item 1", "Item 2", "Item 3", "Item 4" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.row, mStrings));
		getListView().setTextFilterEnabled(true);
	}
}